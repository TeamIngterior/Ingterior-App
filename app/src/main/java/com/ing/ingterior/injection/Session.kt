package com.ing.ingterior.injection

import android.content.Context
import com.ing.ingterior.model.UserModel

abstract class Session {
    abstract fun logIn(context: Context, type:String): UserModel?
    abstract fun logOut(): Boolean
    abstract fun getUser(): UserModel?
    abstract fun isLogin(): Boolean
}