package com.ing.ingterior.ui.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.TYPE_KAKAO_TALK

class SignInActivity : AppCompatActivity() {

    private val ivLoginBack : ImageView by lazy { findViewById(R.id.iv_login_back) }
    private val lineLoginKakaoTalkLayout : LinearLayout by lazy { findViewById(R.id.line_login_kakaotalk) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ivLoginBack.setOnClickListener {
            onBackPressed()
        }

        lineLoginKakaoTalkLayout.setOnClickListener {
            val userModel = Factory.get().getSession().logIn(this, TYPE_KAKAO_TALK)
            if(userModel != null) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                Factory.get().getMove().moveMainActivity(this)
                finish()
            }
        }
    }
}