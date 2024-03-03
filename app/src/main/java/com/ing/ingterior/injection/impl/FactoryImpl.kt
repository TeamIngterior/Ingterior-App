package com.ing.ingterior.injection.impl

import com.ing.ingterior.IngTeriorApplication
import com.ing.ingterior.injection.*

class FactoryImpl : Factory() {

    private var application: IngTeriorApplication? = null
    private var move: Move? = null
    private var session: Session? = null
    private var database: Database? = null

    companion object{
        fun register(application: IngTeriorApplication){
            val factory = FactoryImpl()
            instance = factory
            factory.application = application
            factory.move = MoveImpl()
            factory.session = SessionImpl()
            factory.database = DatabaseImpl()
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
}