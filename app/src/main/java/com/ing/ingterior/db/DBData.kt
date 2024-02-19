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

object Site {
    const val SITE_MANAGER = "manager"
    const val SITE_GUEST = "guest"

    const val EXTRA_SITE_OPERATOR = "site_operator"
    const val FOLD_DEFAULT = 1
    const val FOLD_MANAGEMENT = 2
    const val FOLD_ALL = 3

    const val TABLE_NAME = "site"
    const val _ID = "_id"
    const val USER_ID = "user_id"
    const val NAME = "name"
    const val CODE = "code"
    const val DEFAULT_IDS = "default_ids"
    const val MANAGEMENT_IDS = "management_ids"
    const val BLUEPRINT_ID = "blueprint_id"
    const val CREATED_DATE = "created_date"

    const val CONTENT_URI = "content://ingterior/${TABLE_NAME}"
    val QUERY_ALL = "SELECT site._id, site.user_id, site.name, site.code, site.default_ids, site.management_ids, site.blueprint_id, site.created_date,  " +
            "image.location, image.filename FROM site JOIN image ON site.blueprint_id = image._id WHERE site._id = ?"
}

object Image {
    const val TABLE_NAME = "image"
    const val _ID = "_id"
    const val LOCATION = "location"
    const val FILENAME = "filename"
    const val CREATED_DATE = "created_date"

}

object Fold {
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