package com.ing.ingterior.ui.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ing.ingterior.R

class SignInActivity : AppCompatActivity() {

    private val btnSignIn : Button by lazy { findViewById(R.id.btn_sign_in) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnSignIn.setOnClickListener {

        }
    }
}