package com.ing.ingterior.ui.site

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ing.ingterior.db.Image
import com.ing.ingterior.db.Site
import com.ing.ingterior.db.Status
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.BluePrintModel
import com.ing.ingterior.util.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class SiteViewModel : ViewModel() {
    companion object{
        private const val TAG = "SiteViewModel"
    }
    var siteName = ""
    var isDefects = true
    var isManagement = false
    val bluePrintModel = MutableLiveData<BluePrintModel>()

    fun requireEnable(): Boolean{
        return siteName.isNotEmpty() && (isDefects || isManagement)
    }

    suspend fun getImageBitmap(context: Context): Bitmap? {
        return if(bluePrintModel.value?.bitmap == null) reScaleBluePrintImage(context)
        else bluePrintModel.value?.bitmap
    }

    suspend fun reScaleBluePrintImage(context: Context): Bitmap? = withContext(Dispatchers.IO) {
        val photoUri = bluePrintModel.value?.uri ?: return@withContext null
        val rotation = bluePrintModel.value?.rotation ?: 0f
        val horizontalInversion = bluePrintModel.value?.horizontalInversion ?: false
        val verticalInversion = bluePrintModel.value?.verticalInversion ?: false

        Log.d(TAG, "reScaleBluePrintImage: rotation=$rotation, horizontalInversion=$horizontalInversion")
        // Uri로부터 Bitmap 로드
        val originalBitmap = ImageUtils.GraphicUtils.loadBitmapFromUri(photoUri, context) ?: return@withContext null

        // 이미지 회전
        val rotatedBitmap = ImageUtils.GraphicUtils.rotateImage(originalBitmap, rotation)
        // 필요에 따라 이미지 좌우 반전
        val flipHorizontalBitmap = ImageUtils.GraphicUtils.flipImageHorizontally(rotatedBitmap, horizontalInversion)

        val rsBitmap = ImageUtils.GraphicUtils.flipImageVertically(flipHorizontalBitmap, verticalInversion)
        bluePrintModel.value?.bitmap = rsBitmap

        rsBitmap
    }


}
