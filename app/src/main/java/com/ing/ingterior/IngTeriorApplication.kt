package com.ing.ingterior

import android.app.Application
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.impl.FactoryImpl

class IngTeriorApplication : Application() {
    companion object{
        private var instance: IngTeriorApplication? = null
    }

    var isFirst: Boolean = true

    override fun onCreate() {
        super.onCreate()
        instance = this
        FactoryImpl.register(this)
        Factory.get().getDatabase().generateDatabase(this)
    }
}