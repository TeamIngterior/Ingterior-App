package com.ing.ingterior.ui.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ing.ingterior.R
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.chat.ConversationListFragment
import com.ing.ingterior.ui.main.HomeFragment
import com.ing.ingterior.ui.main.SettingFragment
import com.ing.ingterior.ui.main.SiteListFragment

class MainViewModel : ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    var currentPageIndex = 0

    fun getPage(index: Int): Fragment{
        return when(index) {
            0 -> HomeFragment()
            1 -> SiteListFragment()
            2 -> ConversationListFragment()
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

    val user = MutableLiveData<User>()

    fun isLogin(): Boolean {
        return user.value != null
    }


    var fabOpen = MutableLiveData(false) // 초기 상태는 닫힌 상태로 설정

    override fun onCleared() {
        super.onCleared()
    }
}