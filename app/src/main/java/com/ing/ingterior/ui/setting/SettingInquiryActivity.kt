package com.ing.ingterior.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ing.ingterior.R
import com.ing.ui.button.VisualImageButton

class SettingInquiryActivity : AppCompatActivity() {
    private lateinit var vibBack: VisualImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_inquiry)

        vibBack = findViewById(R.id.vib_si_back)
        vibBack.setOnClickListener {
            onBackPressed()
        }
    }
}