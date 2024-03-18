package com.ing.ingterior.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ing.ingterior.EXTRA_SITE
import com.ing.ingterior.db.Image
import com.ing.ingterior.db.Site
import com.ing.ingterior.db.Status
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.ImageModel
import com.ing.ingterior.util.ImageUtils
import com.ing.ingterior.util.ImageUtils.GraphicUtils.loadBitmapFromFile
import com.ing.ingterior.util.ImageUtils.GraphicUtils.loadBitmapFromUri
import com.ing.ingterior.util.getParcelableCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SiteViewModel : ViewModel() {
    companion object{
        private const val TAG = "SiteViewModel"
        const val UI_DEFECTS_MODE = 1
        const val UI_MANAGING_MODE = 2
    }

    var isCreate = true
    var uiState = UI_DEFECTS_MODE

    var site: Site? = null
    var siteName = ""
    var isDefects = true
    var isManagement = false
    val imageModel = MutableLiveData<ImageModel>()
    val defectImages = arrayListOf<ImageModel>()

    fun requireEnable(): Boolean{
        return siteName.isNotEmpty() && (isDefects || isManagement)
    }

    suspend fun getImageBitmap(context: Context): Bitmap? {
        return if(imageModel.value?.bitmap == null) reScaleBluePrintImage(context)
        else imageModel.value?.bitmap
    }

    suspend fun reScaleBluePrintImage(context: Context): Bitmap? = withContext(Dispatchers.IO) {
        val photoUri = imageModel.value?.uri
        val photoPath = imageModel.value?.location

        val originalBitmap = if(photoUri!=null) {
            loadBitmapFromUri(photoUri, context)
        }
        else if(photoPath != null) {
            loadBitmapFromFile(photoPath)
        }
        else null
        if(originalBitmap == null){
            Log.w(TAG, "reScaleBluePrintImage: originalBitmap is null")
            return@withContext null
        }

        val rotation = imageModel.value?.rotation ?: 0f
        val horizontalInversion = imageModel.value?.horizontalInversion ?: false
        val verticalInversion = imageModel.value?.verticalInversion ?: false

        Log.d(TAG, "reScaleBluePrintImage: photoUri=$photoUri")
        Log.d(TAG, "reScaleBluePrintImage: rotation=$rotation, horizontalInversion=$horizontalInversion")
        // Uri로부터 Bitmap 로드

        // 이미지 회전
        val rotatedBitmap = ImageUtils.GraphicUtils.rotateImage(originalBitmap, rotation)
        // 필요에 따라 이미지 좌우 반전
        val flipHorizontalBitmap = ImageUtils.GraphicUtils.flipImageHorizontally(rotatedBitmap, horizontalInversion)

        val rsBitmap = ImageUtils.GraphicUtils.flipImageVertically(flipHorizontalBitmap, verticalInversion)
        imageModel.value?.bitmap = rsBitmap

        rsBitmap
    }



    val allSiteListData = MutableLiveData<Status<ArrayList<Site>>>()
    fun getAllSiteList(context: Context, forced: Boolean) {
        if(!forced && allSiteListData.value!=null && allSiteListData.value is Status.Success) return
        allSiteListData.postValue(Status.Loading)
        val userId = Factory.get().getSession().getUser()?.id ?: return allSiteListData.postValue(Status.Error("로그인 상태가 아닙니다."))
        val siteList = arrayListOf<Site>()
        val cursor = context.contentResolver.query(Uri.parse(Site.CONTENT_URI), Site.ALL_PROJECTION, null, arrayOf(userId.toString()), null)
        cursor?.let {
            if(cursor.moveToFirst()) {
                do{
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(Site._ID))
                    val creatorId = cursor.getLong(cursor.getColumnIndexOrThrow(Site.CREATOR_ID))
                    val participantIds = cursor.getString(cursor.getColumnIndexOrThrow(Site.PARTICIPANT_IDS))
                    val siteName = cursor.getString(cursor.getColumnIndexOrThrow(Site.NAME))
                    val siteCode = cursor.getString(cursor.getColumnIndexOrThrow(Site.CODE))
                    val defaultIds = cursor.getString(cursor.getColumnIndexOrThrow(Site.DEFECTS_IDS)) ?: ""
                    val managementIds = cursor.getString(cursor.getColumnIndexOrThrow(Site.MANAGEMENT_IDS)) ?: ""
                    val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(Site.BLUEPRINT_ID))
                    val createdDate = cursor.getLong(cursor.getColumnIndexOrThrow(Site.CREATED_DATE))
                    val favorite = cursor.getInt(cursor.getColumnIndexOrThrow(Site.FAVORITE))
                    val imageName = cursor.getString(cursor.getColumnIndexOrThrow(Image.FILENAME)) ?: ""
                    val imageLocation = cursor.getString(cursor.getColumnIndexOrThrow(Image.LOCATION)) ?: ""
                    siteList.add(Site(id, creatorId, participantIds, siteName, siteCode, defaultIds, managementIds, imageId, createdDate, imageName, imageLocation, favorite == 1))
                }while (it.moveToNext())
            }
            cursor.close()
        }
        Log.d(TAG, "getAllSiteList: siteList=$siteList")
        if(siteList.isEmpty()){
            allSiteListData.postValue(Status.Error("참여 중인 현장이 없습니다."))
        }
        else{
            allSiteListData.postValue(Status.Success(siteList))
        }
    }

    fun setIntent(intent: Intent) {
        val site = intent.getParcelableCompat<Site>(EXTRA_SITE) ?: return
        this.site = site
        isCreate = false

        siteName = site.siteName
        isDefects = site.defectsIds.isNotEmpty()
        isManagement = site.managementIds.isNotEmpty()
        imageModel.postValue(ImageModel(site.imageId, null, site.imageLocation, site.imageName, bitmap = loadBitmapFromFile(site.imageLocation)))
    }

    fun addDefectImage(context: Context, defectImage: ImageModel){
        var isContains = false
        for(image in defectImages){
            if(image.uri == defectImage.uri) {
                isContains = true
            }
        }
        if(isContains) Toast.makeText(context, "이미 포함된 이미지 입니다.", Toast.LENGTH_SHORT).show()
        else defectImages.add(defectImage)

    }

    fun removeDefectImage(defectImage: ImageModel){
        defectImages.remove(defectImage)
    }

}
