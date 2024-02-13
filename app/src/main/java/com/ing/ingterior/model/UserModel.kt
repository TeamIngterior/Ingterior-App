package com.ing.ingterior.model

import android.content.ContentValues
import android.provider.ContactsContract.Data
import com.ing.ingterior.db.Log
import java.util.*

const val TYPE_KAKAO_TALK = "kakao"
const val TYPE_NAVER = "naver"
const val TYPE_GOOGLE = "google"
const val TYPE_INSTAGRAM = "instagram"
data class UserModel(val id: Long, val email: String, val token: String, val type: String) {
    fun toLog() : ContentValues {
        val values = ContentValues()
        values.put(Log.USER_ID, id)
        values.put(Log.EMAIL, email)
        values.put(Log.TOKEN, token)
        values.put(Log.TYPE, type)
        values.put(Log.LATEST_DATE, Date().time)
        return values
    }
}
