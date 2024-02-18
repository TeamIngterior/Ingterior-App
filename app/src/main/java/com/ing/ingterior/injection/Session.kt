package com.ing.ingterior.injection

import android.content.Context
import com.ing.ingterior.model.User

abstract class Session {
    abstract fun logIn(context: Context, type:String): User?
    abstract fun logOut(): Boolean
    abstract fun getUser(): User?
    abstract fun isLogin(): Boolean
}