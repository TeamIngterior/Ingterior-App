package com.ing.ingterior.ui.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.ui.IngTeriorViewModelFactory

class SimpleEstimationActivity : AppCompatActivity() {
    private val viewModel : SimpleEstimationViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[SimpleEstimationViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_estimation)


    }

    override fun onDestroy() {
        super.onDestroy()
        IngTeriorViewModelFactory.simpleEstimationViewModel = null
    }
}