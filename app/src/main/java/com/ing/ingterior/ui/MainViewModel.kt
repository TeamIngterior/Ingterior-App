package com.ing.ingterior.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ing.ingterior.R
import com.ing.ingterior.db.Image
import com.ing.ingterior.db.Site
import com.ing.ingterior.db.Status
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.main.HomeFragment
import com.ing.ingterior.ui.main.MessageFragment
import com.ing.ingterior.ui.main.SettingFragment
import com.ing.ingterior.ui.main.SiteListFragment
import com.ing.ingterior.ui.site.SiteViewModel

class MainViewModel : ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    var currentPageIndex = 0

    fun getPage(index: Int): Fragment{
        return when(index) {
            0 -> HomeFragment()
            1 -> SiteListFragment()
            2 -> MessageFragment()
            else -> SettingFragment()
        }
    }

    fun getPageId(index: Int): Int{
        return when(index) {
            0 -> R.id.menu_main
            1 -> R.id.menu_site_management
            2 -> R.id.menu_message
            else -> R.id.menu_setting
        }
    }

    fun getUser() : User?{
        return Factory.get().getSession().getUser()
    }

    fun isLogin(): Boolean {
        return getUser() != null
    }


    val allSiteListData = MutableLiveData<Status<ArrayList<Site>>>()
    fun getAllSiteList(context: Context) {
        val userId = Factory.get().getSession().getUser()?.id ?: return allSiteListData.postValue(Status.Error("로그인 상태가 아닙니다."))
        val siteList = arrayListOf<Site>()
        val cursor = context.contentResolver.query(Uri.parse(Site.CONTENT_URI), Site.ALL_PROJECTION, null, arrayOf(userId.toString()), null)
        cursor?.let {
            if(cursor.moveToFirst()) {
                do{
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(Site._ID))
                    val creatorId = cursor.getLong(cursor.getColumnIndexOrThrow(Site.CREATOR_ID))
                    val participantIds = cursor.getString(cursor.getColumnIndexOrThrow(Site.PARTICIPANT_IDS))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(Site.NAME))
                    val code = cursor.getString(cursor.getColumnIndexOrThrow(Site.CODE))
                    val defaultIds = cursor.getString(cursor.getColumnIndexOrThrow(Site.DEFAULT_IDS)) ?: ""
                    val managementIds = cursor.getString(cursor.getColumnIndexOrThrow(Site.MANAGEMENT_IDS)) ?: ""
                    val blueprintId = cursor.getLong(cursor.getColumnIndexOrThrow(Site.BLUEPRINT_ID)) ?: 0L
                    val createdDate = cursor.getLong(cursor.getColumnIndexOrThrow(Site.CREATED_DATE)) ?: 0L
                    val bluePrintName = cursor.getString(cursor.getColumnIndexOrThrow(Image.FILENAME))
                    val bluePrintLocation = cursor.getString(cursor.getColumnIndexOrThrow(Image.LOCATION))
                    siteList.add(Site(id, creatorId, participantIds, name, code, defaultIds, managementIds, blueprintId, createdDate, bluePrintName, bluePrintLocation))
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

    override fun onCleared() {
        super.onCleared()
    }
}