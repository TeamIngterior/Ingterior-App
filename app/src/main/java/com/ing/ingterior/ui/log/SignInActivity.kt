package com.ing.ingterior.ui.log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.TYPE_GOOGLE
import com.ing.ingterior.model.TYPE_INSTAGRAM
import com.ing.ingterior.model.TYPE_KAKAO_TALK
import com.ing.ingterior.model.TYPE_NAVER
import com.ing.ui.button.LoginButton

class SignInActivity : AppCompatActivity() {

    private val lbKakaoTalk: LoginButton by lazy { findViewById(R.id.lb_login_kakaotalk) }
    private val lbNaver: LoginButton by lazy { findViewById(R.id.lb_login_naver) }
    private val lbGoogle: LoginButton by lazy { findViewById(R.id.lb_login_google) }
    private val lbInstagram: LoginButton by lazy { findViewById(R.id.lb_login_instagram) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        lbKakaoTalk.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_KAKAO_TALK)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                Factory.get().getMove().moveMainActivity(this)
                finish()
            }
        }

        lbNaver.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_NAVER)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                Factory.get().getMove().moveMainActivity(this)
                finish()
            }
        }

        lbGoogle.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_GOOGLE)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                Factory.get().getMove().moveMainActivity(this)
                finish()
            }
        }

        lbInstagram.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_INSTAGRAM)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                Factory.get().getMove().moveMainActivity(this)
                finish()
            }
        }
    }
}