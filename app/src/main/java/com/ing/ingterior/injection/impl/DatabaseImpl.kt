package com.ing.ingterior.injection.impl

import android.content.Context
import android.net.Uri
import com.ing.ingterior.db.IngTeriorDBHelper
import com.ing.ingterior.db.Log
import com.ing.ingterior.injection.Database
import com.ing.ingterior.injection.Factory
import com.ing.ingterior.model.UserModel

class DatabaseImpl : Database() {

    companion object {
        private var dbHelper: IngTeriorDBHelper? = null
    }

    override fun generateDatabase(context: Context): IngTeriorDBHelper {
        if(dbHelper == null) dbHelper = IngTeriorDBHelper(context)
        return dbHelper!!
    }

    override fun insertLog(context: Context, userModel: UserModel) {
        context.contentResolver.insert(Uri.parse(Log.URI_LOG), userModel.toLog())
    }

}