package com.ing.ingterior.db

object Sign {
    const val TABLE_NAME = "sign"
    const val _ID = "_id"
    const val USER_ID = "user_id"
    const val EMAIL = "email"
    const val TOKEN = "token"
    const val TYPE = "type"
    const val LATEST_DATE = "date"

    const val CONTENT_URI = "content://ingterior/$TABLE_NAME"

    const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_ID INTEGER NOT NULL, " +
            "$EMAIL TEXT NOT NULL, $TOKEN TEXT NOT NULL, $TYPE INTEGER NOT NULL, $LATEST_DATE INTEGER NOT NULL);"

    val PROJECTION = arrayOf(_ID, USER_ID, EMAIL, TOKEN, TYPE, LATEST_DATE)
}

object Site {
    const val TABLE_NAME = "site"
    const val _ID = "_id"
    const val USER_ID = "user_id"
    const val NAME = "name"
    const val CODE = "code"
    const val BLUEPRINT_ID = "blueprint_id"
    const val CREATE_DATE = "create_date"

    const val CONTENT_URI = "content://ingterior/${TABLE_NAME}"

    const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME($_ID INTEGER PRIMARY KEY AUTOINCREMENT, $USER_ID INTEGER NOT NULL, $NAME TEXT NOT NULL, $CODE TEXT NOT NULL, " +
            "$BLUEPRINT_ID INTEGER NOT NULL, $CREATE_DATE INTEGER NOT NULL);"
    val QUERY_ALL = "SELECT site._id, site.user_id, site.name, site.code, site.blueprint_id, site.create_date, " +
            "image.location, image.filename FROM site JOIN image ON site.blueprint_id = image._id WHERE site._id = ?"
}

object Management {
    const val TABLE_NAME = "management"
    const val _ID = "_id"
    const val SITE_ID = "site_id"
    const val FOLDS = "folds"

    const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$SITE_ID INTEGER NOT NULL, $FOLDS TEXT NOT NULL);"
}

object Image {
    const val TABLE_NAME = "image"
    const val _ID = "_id"
    const val LOCATION = "location"
    const val FILENAME = "filename"
    const val CREATE_DATE = "create_date"

    const val CREATE_TABLE = "CREATE TABLE ${TABLE_NAME}(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$LOCATION TEXT NOT NULL, $FILENAME TEXT NOT NULL, $CREATE_DATE INTEGER NOT NULL);"
}

object Fold {
    const val TABLE_NAME = "fold"
    const val _ID = "_id"
    const val FOLD_IMAGES = "fold_images"
    const val FOLD_NAME = "fold_name"
    const val MEMO = "memo"
    const val CREATER = "creater"
    const val POSITION = "position"
    const val CREATE_DATE = "create_date"

    const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT, $FOLD_IMAGES TEXT NOT NULL, $FOLD_NAME TEXT NOT NULL, " +
            "$MEMO TEXT NOT NULL, $CREATER INTEGER NOT NULL, $POSITION TEXT NOT NULL, $CREATE_DATE INTEGER NOT NULL);"
}