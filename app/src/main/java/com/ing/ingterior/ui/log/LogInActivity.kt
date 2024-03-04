package com.ing.ingterior.ui.log

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.TYPE_INSTAGRAM
import com.ing.ingterior.model.TYPE_KAKAO_TALK
import com.ing.ingterior.model.TYPE_NAVER
import com.ing.ui.button.LoginButton
import com.ing.ui.button.VisualImageButton


class LogInActivity : AppCompatActivity() {

    private val lbKakaoTalk: LoginButton by lazy { findViewById(R.id.lb_login_kakaotalk) }
    private val lbNaver: LoginButton by lazy { findViewById(R.id.lb_login_naver) }
    private val lbGoogle: LoginButton by lazy { findViewById(R.id.lb_login_google) }
    private val lbInstagram: LoginButton by lazy { findViewById(R.id.lb_login_instagram) }
    private lateinit var vibBack: VisualImageButton

    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            // 로그인 성공 처리
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                Factory.get().getMove().moveMainActivity(this)
            val userName = account.givenName
            val serverAuth = account.serverAuthCode
            Log.d(LogInActivity::class.java.simpleName, "userName=$userName, serverAuth=$serverAuth")
        } catch (e: ApiException) {
            // 로그인 실패 처리
            Log.e(LogInActivity::class.java.simpleName, e.stackTraceToString())
            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        vibBack = findViewById(R.id.vib_log_in_back)
        vibBack.setOnClickListener {
            finish()
        }

        lbKakaoTalk.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_KAKAO_TALK)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                Factory.get().getMove().moveMainActivity(this)
                setResult(RESULT_OK)
                finish()
            }
        }

        lbNaver.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_NAVER)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                Factory.get().getMove().moveMainActivity(this)
                setResult(RESULT_OK)
                finish()
            }
        }

        lbGoogle.setOnClickListener {
            signIn()
//            val userModel = Factory.get().getSession().logIn(this, TYPE_GOOGLE)
//            if(userModel != null) {
//                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
////                Factory.get().getMove().moveMainActivity(this)
//                setResult(RESULT_OK)
//                finish()
//            }
        }

        lbInstagram.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_INSTAGRAM)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                Factory.get().getMove().moveMainActivity(this)
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

}