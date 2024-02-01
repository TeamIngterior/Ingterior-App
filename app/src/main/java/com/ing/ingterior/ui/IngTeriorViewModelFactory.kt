package com.ing.ingterior.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.ui.start.StartViewModel

class IngTeriorViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StartViewModel::class.java)){
            return StartViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}