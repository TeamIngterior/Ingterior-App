package com.ing.ingterior

import android.app.Application
import com.ing.ingterior.injection.impl.FactoryImpl

class IngTeriorApplication : Application() {
    companion object{
        private var instance: IngTeriorApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FactoryImpl.register(this)
    }
}