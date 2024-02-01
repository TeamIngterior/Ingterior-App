package com.ing.ingterior.injection

import android.content.Context
import com.ing.ingterior.IngTeriorApplication

abstract class Factory {
    companion object{
        const val TAG = "Factory"

        @Volatile
        var instance: Factory? = null
        fun get(): Factory = instance!!
    }

    abstract fun getApplication(): IngTeriorApplication
    abstract fun getMove(): Move
    abstract fun getServiceApi(): ServerApi

}