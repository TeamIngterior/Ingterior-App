package com.ing.ingterior.ui.site

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.ing.ingterior.R

class NewSiteActivity : AppCompatActivity() {

    private val ivBack: ImageView by lazy { findViewById(R.id.iv_site_back) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_site)

        ivBack.setOnClickListener {
            super.onBackPressed()
        }


    }
}