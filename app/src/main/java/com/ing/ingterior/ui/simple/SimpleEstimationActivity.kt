package com.ing.ingterior.ui.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ui.button.VisualImageButton

class SimpleEstimationActivity : AppCompatActivity() {
    private val viewModel : SimpleEstimationViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[SimpleEstimationViewModel::class.java] }

    private lateinit var titleTextView: TextView
    private lateinit var vibBack: VisualImageButton
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_estimation)

        titleTextView = findViewById(R.id.tv_se_title)
        vibBack = findViewById(R.id.vib_se_back)
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
        vibBack.setOnClickListener {
            backPressedCallback.handleOnBackPressed()
        }
    }

    fun setToolbarTitle(title: String) {
        titleTextView.text = title
    }

    override fun onDestroy() {
        super.onDestroy()
        // Callback을 제거합니다
        backPressedCallback.remove()
    }
}