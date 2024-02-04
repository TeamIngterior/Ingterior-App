package com.ing.ingterior.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.ui.simple.SimpleEstimationViewModel
import com.ing.ingterior.ui.home.HomeViewModel

class IngTeriorViewModelFactory : ViewModelProvider.Factory {

    companion object{
        private val interiorViewModel = SimpleEstimationViewModel()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel() as T
        }
        else if (modelClass.isAssignableFrom(SimpleEstimationViewModel::class.java)) {
            return interiorViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}