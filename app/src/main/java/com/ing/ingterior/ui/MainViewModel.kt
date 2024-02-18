package com.ing.ingterior.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.main.HomeFragment
import com.ing.ingterior.ui.main.MessageFragment
import com.ing.ingterior.ui.main.SettingFragment
import com.ing.ingterior.ui.main.SiteListFragment

class MainViewModel : ViewModel() {

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

    override fun onCleared() {
        super.onCleared()
    }
}