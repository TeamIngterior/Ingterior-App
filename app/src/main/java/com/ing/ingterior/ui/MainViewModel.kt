package com.ing.ingterior.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.impl.SessionImpl
import com.ing.ingterior.model.UserModel

class MainViewModel : ViewModel() {

    fun getUser() : UserModel?{
        return Factory.get().getSession().getUser()
    }

    fun isLogin(): Boolean {
        return getUser() != null
    }

    override fun onCleared() {
        super.onCleared()
    }
}