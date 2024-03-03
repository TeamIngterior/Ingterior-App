package com.ing.ingterior.db

data class Sign(val id: Long, val userId: Long, val email: String, val token: String, val type: String, val latestDate: Long) {
    companion object{
        const val TABLE_NAME = "sign"
        const val _ID = "_id"
        const val USER_ID = "user_id"
        const val EMAIL = "email"
        const val TOKEN = "token"
        const val TYPE = "type"
        const val LATEST_DATE = "latest_date"
        const val CONTENT_URI = "content://ingterior/$TABLE_NAME"

        val PROJECTION = arrayOf(_ID, USER_ID, EMAIL, TOKEN, TYPE, LATEST_DATE)
    }
}