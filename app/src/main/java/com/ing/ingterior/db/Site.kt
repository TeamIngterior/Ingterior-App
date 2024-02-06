package com.ing.ingterior.db

object Site {
    const val TABLE_NAME = "site"
    const val _ID = "_id"
    const val SITE_NAME = "site_name"
    const val SITE_CODE = "site_code"
    const val SITE_ADDRESS = "site_address"
    const val SITE_IMAGE_ID = "site_image_id"
    const val CREATE_DATE = "create_date"

    const val CREATE_SITE_TABLE = "CREATE TABLE $TABLE_NAME($_ID INTEGER PRIMARY KEY AUTOINCREMENT, $SITE_NAME TEXT NOT NULL, $SITE_CODE TEXT NOT NULL, " +
            "$SITE_ADDRESS TEXT NOT NULL, $SITE_IMAGE_ID INTEGER NOT NULL, $CREATE_DATE INTEGER NOT NULL);"

    object Management {
        const val TABLE_NAME = "management"
        const val _ID = "_id"
        const val SITE_ID = "site_id"
        const val FOLDS = "folds"

        const val CREATE_MANAGEMENT_TABLE = "CREATE TABLE $TABLE_NAME(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$SITE_ID INTEGER NOT NULL, $FOLDS TEXT NOT NULL);"
    }

    object Fold {
        const val TABLE_NAME = "Fold"
        const val _ID = "_id"
        const val FOLD_IMAGES = "fold_images"
        const val FOLD_NAME = "fold_name"
        const val MEMO = "memo"
        const val CREATER = "creater"
        const val POSITION = "position"
        const val CREATE_DATE = "create_date"

        const val CREATE_FOLD_TABLE = "CREATE TABLE $TABLE_NAME(${_ID} INTEGER PRIMARY KEY AUTOINCREMENT, $FOLD_IMAGES TEXT NOT NULL, $FOLD_NAME TEXT NOT NULL, " +
                "$MEMO TEXT NOT NULL, $CREATER INTEGER NOT NULL, $POSITION TEXT NOT NULL, $CREATE_DATE INTEGER NOT NULL);"
    }
}