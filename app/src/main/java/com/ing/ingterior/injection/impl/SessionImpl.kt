package com.ing.ingterior.injection.impl

import android.content.Context
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.Session
import com.ing.ingterior.model.TYPE_GOOGLE
import com.ing.ingterior.model.UserModel

class SessionImpl : Session() {
    companion object{
        private var user: UserModel? = null
    }

    override fun logIn(context: Context, type:String): UserModel? {
        // TODO 다시 짜야 함
        if(isLogin()) {
            // TODO 이미 로그인된 상태
        }
        else{
            user = UserModel(0, "jypjun12@gmail.com", "T", TYPE_GOOGLE)
            Factory.get().getDatabase().insertLog(context, user!!)
        }

        return user
    }

    override fun logOut(): Boolean {
        user = null
        return true
    }

    override fun getUser(): UserModel? {
        return user
    }

    override fun isLogin(): Boolean {
        return user != null
    }
}