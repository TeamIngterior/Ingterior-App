package com.ing.ingterior.ui.home

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.util.AnimationUtils


class HometActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "StartActivity"
    }

    private val viewModel : HomeViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[HomeViewModel::class.java] }
    private val ivLogo: ImageView by lazy { findViewById(R.id.iv_home_splash_logo) }
    private val lineSplashScreenLayout: LinearLayout by lazy { findViewById(R.id.line_home_splash_layout) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if(viewModel.isFirst) {
            AnimationUtils.fadeInAndOut(ivLogo, 500, object : AnimationUtils.AnimationListener {
                override fun start() {
                    lineSplashScreenLayout.isVisible = true
                }
                override fun end() {
                    lineSplashScreenLayout.isVisible = false
                }
            })
        }

//        btnGotoEstimation.setOnClickListener {
//            Factory.get().getMove().moveSimpleEstimationActivity(this@HometActivity)
//        }
//        btnGotoManaging.setOnClickListener {
//
//        }
//        btnGotoSignIn.setOnClickListener {
//            Factory.get().getMove().moveSignInActivity(this@HometActivity)
//        }
    }
}