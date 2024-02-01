package com.ing.ingterior.ui.start

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.util.AnimationUtils


class StartActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "StartActivity"
    }

    private val viewModel : StartViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[StartViewModel::class.java] }
    private val ivLogo: ImageView by lazy { findViewById(R.id.iv_ss_logo) }
    private val lineSplashScreenLayout: LinearLayout by lazy { findViewById(R.id.line_ss_layout) }
    private val btnGotoEstimation: Button by lazy { findViewById(R.id.btn_start_go_to_estimation) }
    private val btnGotoManaging: Button by lazy { findViewById(R.id.btn_start_go_to_managing) }
    private val btnGotoSignIn: Button by lazy { findViewById(R.id.btn_start_go_to_sign_in) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

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

        btnGotoEstimation.setOnClickListener {
            Factory.get().getMove().moveSimpleEstimationActivity(this@StartActivity)
        }
        btnGotoManaging.setOnClickListener {

        }
        btnGotoSignIn.setOnClickListener {
            Factory.get().getMove().moveSignInActivity(this@StartActivity)
        }
    }
}