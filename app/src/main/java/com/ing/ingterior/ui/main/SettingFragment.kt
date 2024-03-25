package com.ing.ingterior.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.ui.viewmodel.MainViewModel

class SettingFragment : Fragment() {
    private val viewModel : MainViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[MainViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.currentPageIndex = 3
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

}