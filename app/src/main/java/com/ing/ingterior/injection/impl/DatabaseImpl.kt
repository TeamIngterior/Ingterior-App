package com.ing.ingterior.injection.impl

import android.content.Context
import android.net.Uri
import com.ing.ingterior.db.IngTeriorDBHelper
import com.ing.ingterior.db.Sign
import com.ing.ingterior.injection.Database
import com.ing.ingterior.model.User

class DatabaseImpl : Database() {

    companion object {
        private var dbHelper: IngTeriorDBHelper? = null
    }

    override fun generateDatabase(context: Context): IngTeriorDBHelper {
        if(dbHelper == null) dbHelper = IngTeriorDBHelper(context)
        return dbHelper!!
    }

    override fun insertLog(context: Context, user: User) {
        context.contentResolver.insert(Uri.parse(Sign.CONTENT_URI), user.toLog())
    }

}