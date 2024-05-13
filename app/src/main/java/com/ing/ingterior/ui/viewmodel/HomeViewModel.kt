package com.ing.ingterior.ui.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ing.ingterior.Logging.logE
import com.ing.ingterior.Logging.logW
import com.ing.ingterior.R
import com.ing.ingterior.db.constructor.Construction
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.User
import com.ing.ingterior.ui.chat.ConversationListFragment
import com.ing.ingterior.ui.main.HomeFragment
import com.ing.ingterior.ui.main.SettingFragment
import com.ing.ingterior.ui.main.HomeConstructionFragment
import com.ing.ingterior.util.Resource
import com.ing.ingterior.util.ioNewThread
import io.reactivex.observers.DisposableObserver

class HomeViewModel : BaseViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }

    var currentPageIndex = 0
    val user = MutableLiveData<User>()
    var fabOpen = MutableLiveData(false) // 초기 상태는 닫힌 상태로 설정

    fun getPage(index: Int): Fragment{
        return when(index) {
            0 -> HomeFragment()
            1 -> HomeConstructionFragment()
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

    fun isLogin(): Boolean {
        return user.value != null
    }

    override fun onCleared() {
        super.onCleared()
    }




}