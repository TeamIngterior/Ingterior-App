package com.ing.ingterior.injection

import android.content.Context
import com.ing.ingterior.db.IngTeriorDBHelper
import com.ing.ingterior.model.User

abstract class Database {
    abstract fun generateDatabase(context: Context): IngTeriorDBHelper
    abstract fun insertLog(context: Context, user: User)
}