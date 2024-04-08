package com.ing.ingterior.model

import android.content.ContentValues
import com.ing.ingterior.db.Sign
import java.util.*

const val TYPE_KAKAO_TALK = "kakao"
const val TYPE_NAVER = "naver"
const val TYPE_GOOGLE = "google"
const val TYPE_INSTAGRAM = "instagram"
data class User(val id: Long, val email: String, val nickName: String, val token: String, val type: String, val avatarURL: String = "") {
    fun toLog() : ContentValues {
        val values = ContentValues()
        values.put(Sign.USER_ID, id)
        values.put(Sign.EMAIL, email)
        values.put(Sign.TOKEN, token)
        values.put(Sign.TYPE, type)
        values.put(Sign.LATEST_DATE, Date().time)
        return values
    }
}
