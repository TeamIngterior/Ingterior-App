package com.ing.ingterior.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val AUTHORITY = "ingterior"
private const val DATABASE_VERSION = 1
class IngTeriorDBHelper(context: Context) : SQLiteOpenHelper(context, "${AUTHORITY}.db", null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Log.CREATE_LOG_TABLE)
        db.execSQL(Site.CREATE_SITE_TABLE)
        db.execSQL(Site.Management.CREATE_MANAGEMENT_TABLE)
        db.execSQL(Site.Fold.CREATE_FOLD_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}