package com.ing.ingterior.injection

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.ing.ingterior.model.User

abstract class Session {
    abstract fun googleLogin(context: Context, launcher: ActivityResultLauncher<Intent>): User?
    abstract fun logOut(): Boolean
    abstract fun getUser(): User?

    abstract fun setUser(user: User)
    abstract fun isLogin(): Boolean
}