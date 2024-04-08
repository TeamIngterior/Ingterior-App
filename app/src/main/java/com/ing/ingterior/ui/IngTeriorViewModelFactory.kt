package com.ing.ingterior.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.ui.chat.ChatViewModel
import com.ing.ingterior.ui.simple.SimpleEstimationViewModel
import com.ing.ingterior.ui.viewmodel.MainViewModel
import com.ing.ingterior.ui.viewmodel.SiteViewModel

class IngTeriorViewModelFactory : ViewModelProvider.Factory {

    companion object{
        private val mainViewModel = MainViewModel()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return mainViewModel as T
        }
        else if (modelClass.isAssignableFrom(SimpleEstimationViewModel::class.java)) {
            return SimpleEstimationViewModel() as T
        }
        else if (modelClass.isAssignableFrom(SiteViewModel::class.java)) {
            return SiteViewModel() as T
        }
        else if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}