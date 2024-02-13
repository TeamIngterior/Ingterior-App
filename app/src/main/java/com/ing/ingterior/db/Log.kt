package com.ing.ingterior.db

object Log {
    const val TABLE_NAME = "log"
    const val _ID = "_id"
    const val USER_ID = "user_id"
    const val EMAIL = "email"
    const val TOKEN = "token"
    const val TYPE = "type"
    const val LATEST_DATE = "date"

    const val URI_LOG = "content://ingterior/log"

    const val CREATE_LOG_TABLE = "CREATE TABLE $TABLE_NAME ($_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_ID INTEGER NOT NULL, " +
            "$EMAIL TEXT NOT NULL, $TOKEN TEXT NOT NULL, $TYPE INTEGER NOT NULL, $LATEST_DATE INTEGER NOT NULL);"

    val PROJECTION = arrayOf(_ID, USER_ID, EMAIL, TOKEN, TYPE, LATEST_DATE)
}