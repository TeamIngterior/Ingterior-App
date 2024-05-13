package com.ing.ingterior.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.ui.chat.ChatViewModel
import com.ing.ingterior.ui.simple.SimpleEstimationViewModel
import com.ing.ingterior.ui.viewmodel.HomeViewModel
import com.ing.ingterior.ui.viewmodel.ConstructionViewModel

class IngTeriorViewModelFactory : ViewModelProvider.Factory {

    companion object{
        private val homeViewModel = HomeViewModel()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return homeViewModel as T
        }
        else if (modelClass.isAssignableFrom(SimpleEstimationViewModel::class.java)) {
            return SimpleEstimationViewModel() as T
        }
        else if (modelClass.isAssignableFrom(ConstructionViewModel::class.java)) {
            return ConstructionViewModel() as T
        }
        else if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}