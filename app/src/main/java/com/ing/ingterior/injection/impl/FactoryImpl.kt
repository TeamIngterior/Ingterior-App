package com.ing.ingterior.injection.impl

import com.ing.ingterior.IngTeriorApplication
import com.ing.ingterior.injection.*

class FactoryImpl : Factory() {

    private var application: IngTeriorApplication? = null
    private var move: Move? = null
    private var session: Session? = null
    private var database: Database? = null
    private var serverApi: ServerApi? = null

    companion object{
        fun register(application: IngTeriorApplication){
            val factory = FactoryImpl()
            instance = factory
            factory.application = application
            factory.move = MoveImpl()
            factory.session = SessionImpl(application)
            factory.database = DatabaseImpl()
            factory.serverApi = ServerApiImpl()
        }
    }

    override fun getApplication(): IngTeriorApplication {
        return application!!
    }

    override fun getMove(): Move {
        return move!!
    }


    override fun getSession(): Session {
        return session!!
    }

    override fun getDatabase(): Database {
        return database!!
    }

    override fun getServerApi(): ServerApi {
        return serverApi!!
    }
}