package com.ing.ingterior.injection.impl

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.Session
import com.ing.ingterior.model.TYPE_GOOGLE
import com.ing.ingterior.model.User

class SessionImpl(private val context: Context) : Session() {
    companion object{
        private var user: User? = null
    }

    private val googleSignInClient: GoogleSignInClient
    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }


    override fun googleLogin(context: Context, launcher: ActivityResultLauncher<Intent>): User? {
        // TODO 다시 짜야 함
        if(isLogin()) {
            // TODO 이미 로그인된 상태
        }
        else{
            launcher.launch(googleSignInClient.signInIntent)
        }

        return user
    }

    override fun logOut(): Boolean {
        user = null
        return true
    }

    override fun getUser(): User? {
        return user
    }

    override fun setUser(user: User) {

    }

    override fun isLogin(): Boolean {
        return user != null
    }
}