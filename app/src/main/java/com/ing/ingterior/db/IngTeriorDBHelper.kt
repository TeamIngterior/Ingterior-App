package com.ing.ingterior.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val AUTHORITY = "ingterior"
private const val DATABASE_VERSION = 1
class IngTeriorDBHelper(context: Context) : SQLiteOpenHelper(context, "${AUTHORITY}.db", null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Sign.CREATE_TABLE)
        db.execSQL(Site.CREATE_TABLE)
        db.execSQL(Image.CREATE_TABLE)
        db.execSQL(Management.CREATE_TABLE)
        db.execSQL(Fold.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}