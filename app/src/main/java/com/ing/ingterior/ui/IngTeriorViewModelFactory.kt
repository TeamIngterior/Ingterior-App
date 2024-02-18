package com.ing.ingterior.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.ui.simple.SimpleEstimationViewModel
import com.ing.ingterior.ui.site.SiteViewModel

class IngTeriorViewModelFactory : ViewModelProvider.Factory {

    companion object{
        private val mainViewModel = MainViewModel()
        var simpleEstimationViewModel: SimpleEstimationViewModel? = SimpleEstimationViewModel()
        var siteViewModel: SiteViewModel? = SiteViewModel()

    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return mainViewModel as T
        }
        else if (modelClass.isAssignableFrom(SimpleEstimationViewModel::class.java)) {
            if(simpleEstimationViewModel == null) simpleEstimationViewModel = SimpleEstimationViewModel()
            return simpleEstimationViewModel as T
        }
        else if (modelClass.isAssignableFrom(SiteViewModel::class.java)) {
            if(siteViewModel == null) siteViewModel = SiteViewModel()
            return siteViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}