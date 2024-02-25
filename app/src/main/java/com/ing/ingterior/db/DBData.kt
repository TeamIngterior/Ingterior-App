package com.ing.ingterior.db

object Sign {
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



object Image {
    const val TABLE_NAME = "image"
    const val _ID = "_id"
    const val LOCATION = "location"
    const val FILENAME = "filename"
    const val CREATED_DATE = "created_date"

}

object Fold {
    // option
    const val FOLD_DEFAULT = 1
    const val FOLD_MANAGEMENT = 2
    const val FOLD_ALL = 3


    const val TABLE_NAME = "fold"
    const val _ID = "_id"
    const val SITE_ID = "site_id"
    const val TYPE = "type"
    const val NAME = "name"
    const val MEMO = "memo"
    const val IMAGE_IDS = "image_ids"
    const val CREATOR_ID = "creator_id"
    const val CREATOR_TYPE = "creator_type"
    const val POSITION = "position"
    const val CREATED_DATE = "created_date"
}