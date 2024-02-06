package com.ing.ingterior.db

object User {
    const val TABLE_NAME = "sign"
    const val _ID = "_id"
    const val EMAIL = "email"
    const val SIGN_TYPE = "sign_type"
    const val LATEST_DATE = "date"

    const val CREATE_SIGN_TABLE = "CREATE TABLE sign($_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$EMAIL TEXT NOT NULL, $SIGN_TYPE INTEGER NOT NULL, $LATEST_DATE INTEGER NOT NULL);"
}