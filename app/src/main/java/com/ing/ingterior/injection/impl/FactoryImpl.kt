package com.ing.ingterior.injection.impl

import com.ing.ingterior.IngTeriorApplication
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.injection.Move
import com.ing.ingterior.injection.ServerApi

class FactoryImpl : Factory() {

    private var application: IngTeriorApplication? = null
    private var move: Move? = null
    private var serverApi: ServerApi? = null

    companion object{
        fun register(application: IngTeriorApplication){
            val factory = FactoryImpl()
            instance = factory
            factory.application = application
            factory.move = MoveImpl()
            factory.serverApi = ServerApiIImpl()
        }
    }

    override fun getApplication(): IngTeriorApplication {
        return application!!
    }

    override fun getMove(): Move {
        return move!!
    }

    override fun getServiceApi(): ServerApi {
        return serverApi!!
    }
}