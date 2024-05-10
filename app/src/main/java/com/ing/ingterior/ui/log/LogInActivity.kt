package com.ing.ingterior.ui.log

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.ServerApi
import com.ing.ingterior.model.TYPE_GOOGLE
import com.ing.ingterior.model.TYPE_INSTAGRAM
import com.ing.ingterior.model.TYPE_KAKAO_TALK
import com.ing.ingterior.model.TYPE_NAVER
import com.ing.ingterior.model.User
import com.ing.ui.button.LoginButton
import com.ing.ui.button.VisualImageButton

class LogInActivity : AppCompatActivity() {

    private val lbKakaoTalk: LoginButton by lazy { findViewById(R.id.lb_login_kakaotalk) }
    private val lbNaver: LoginButton by lazy { findViewById(R.id.lb_login_naver) }
    private val lbGoogle: LoginButton by lazy { findViewById(R.id.lb_login_google) }
    private val lbInstagram: LoginButton by lazy { findViewById(R.id.lb_login_instagram) }
    private val lbTest: LoginButton by lazy { findViewById(R.id.lb_login_test) }
    private lateinit var vibBack: VisualImageButton

    private val googleAuthLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            // 로그인 성공 처리
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//                Factory.get().getMove().moveMainActivity(this)
            val userEmail = account.email ?: ""
            val userName = account.givenName
            val userDisplayName = account.displayName ?: ""
            val serverAuth = account.serverAuthCode ?: ""
            val avatarURL = account.photoUrl ?: ""
            val user = User(id = 1, email = userEmail, nickName = userDisplayName, token = serverAuth, type = TYPE_GOOGLE, avatarURL = avatarURL.toString())
            logD(LogInActivity::class.java.simpleName, "user=${user}")
            Factory.get().getSession().setUser(user)
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
            Log.d(LogInActivity::class.java.simpleName, "id=${account.id}, photoUrl=${account.photoUrl}")
            Log.d(LogInActivity::class.java.simpleName, "idToken=${account.idToken}, photoUrl=${account.photoUrl}")
            Log.d(LogInActivity::class.java.simpleName, "userEmail=$userEmail, userName=$userName, userDisplayName=$userDisplayName, serverAuth=$serverAuth")
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
//            val userModel = Factory.get().getSession().googleLogin(this, TYPE_KAKAO_TALK)
//            if(userModel != null) {
//                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
////                Factory.get().getMove().moveMainActivity(this)
//                setResult(RESULT_OK)
//                finish()
//            }
        }

        lbNaver.setOnClickListener {
//            val userModel = Factory.get().getSession().googleLogin(this, TYPE_NAVER)
//            if(userModel != null) {
//                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
////                Factory.get().getMove().moveMainActivity(this)
//                setResult(RESULT_OK)
//                finish()
//            }
        }

        lbGoogle.setOnClickListener {
//            val user = User(1, "jum8kit", "테스트 계정", "", TYPE_GOOGLE)
//            Factory.get().getSession().setUser(user)
//            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
//            setResult(RESULT_OK)
//            finish()
            Factory.get().getSession().googleLogin(this, googleAuthLauncher)
        }

        lbInstagram.setOnClickListener {
//            val userModel = Factory.get().getSession().googleLogin(this, TYPE_INSTAGRAM)
//            if(userModel != null) {
//                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
////                Factory.get().getMove().moveMainActivity(this)
//                setResult(RESULT_OK)
//                finish()
//            }
        }

        lbTest.setOnClickListener {
            val user = User(1, "jum8kit", "테스트 계정", "", TYPE_GOOGLE)
            Factory.get().getSession().setUser(user)
            Toast.makeText(this, "테스트 계정으로 로그인 하셨습니다.", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
//            Factory.get().getSession().googleLogin(this, googleAuthLauncher)
        }
    }

}