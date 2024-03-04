package com.ing.ingterior.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract.CommonDataKinds.Im

const val AUTHORITY = "ingterior"
private const val DATABASE_VERSION = 4
class IngTeriorDBHelper(context: Context) : SQLiteOpenHelper(context, "${AUTHORITY}.db", null, DATABASE_VERSION) {

    companion object{
        const val SIGN_CREATE_TABLE = "CREATE TABLE ${Sign.TABLE_NAME} (${Sign._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${Sign.USER_ID} INTEGER NOT NULL, " +
                "${Sign.EMAIL} TEXT NOT NULL, ${Sign.TOKEN} TEXT NOT NULL, ${Sign.TYPE} INTEGER NOT NULL, ${Sign.LATEST_DATE} INTEGER NOT NULL);"

        const val SITE_CREATE_TABLE = "CREATE TABLE ${Site.TABLE_NAME} (${Site._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${Site.CREATOR_ID} INTEGER NOT NULL, " +
                "${Site.PARTICIPANT_IDS} INTEGER NOT NULL, ${Site.NAME} TEXT NOT NULL, ${Site.CODE} TEXT NOT NULL, ${Site.DEFAULT_IDS} TEXT, ${Site.MANAGEMENT_IDS} TEXT , " +
                "${Site.BLUEPRINT_ID} INTEGER NOT NULL, " +
                "${Site.CREATED_DATE} INTEGER NOT NULL, " +
                "${Site.FAVORITE} INTEGER NOT NULL, " +
                "FOREIGN KEY (${Site.BLUEPRINT_ID}) REFERENCES ${Image.TABLE_NAME}(${Image._ID}) ON DELETE SET DEFAULT);"

        const val IMAGE_CREATE_TABLE = "CREATE TABLE ${Image.TABLE_NAME}(${Image._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${Image.LOCATION} TEXT NOT NULL, ${Image.FILENAME} TEXT NOT NULL, ${Image.CREATED_DATE} INTEGER NOT NULL);"


        const val FOLD_CREATE_TABLE = "CREATE TABLE ${Fold.TABLE_NAME} (${Fold._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${Fold.SITE_ID} INTEGER NOT NULL, ${Fold.TYPE} INTEGER NOT NULL, ${Fold.NAME} TEXT, ${Fold.IMAGE_IDS} TEXT, " +
                "${Fold.MEMO} TEXT, ${Fold.CREATOR_ID} INTEGER NOT NULL, ${Fold.CREATOR_TYPE} TEXT NOT NULL, ${Fold.POSITION} TEXT, ${Fold.CREATED_DATE} INTEGER NOT NULL);"

        // trigger 생성 안된 상태 디버깅 필요
        const val CREATE_ADD_FOLD_DEFAULT_TRIGGER = "CREATE TRIGGER AddFoldIdToSiteDefault AFTER INSERT ON fold " +
                "FOR EACH ROW " +
                "WHEN NEW.type = 1 " +
                "BEGIN " +
                "UPDATE site SET default_ids = CASE " +
                "WHEN default_ids IS NULL OR default_ids = '' THEN NEW._id " +
                "ELSE default_ids || ',' || NEW._id " +
                "END " +
                "WHERE _id = NEW.site_id; " +
                "END;"

        const val CREATE_ADD_FOLD_MANAGEMENT_TRIGGER = "CREATE TRIGGER AddFoldIdToSiteManagement AFTER INSERT ON fold " +
                "FOR EACH ROW " +
                "WHEN NEW.type = 2 " +
                "BEGIN " +
                "UPDATE site SET management_ids = CASE " +
                "WHEN management_ids IS NULL OR management_ids = '' THEN NEW._id " +
                "ELSE management_ids || ',' || NEW._id " +
                "END " +
                "WHERE _id = NEW.site_id; " +
                "END;"

        const val CREATE_DELETE_IMAGE_UPDATE_SITE_TRIGGER = "CREATE TRIGGER UpdateBlueprintIdBeforeImageDelete BEFORE DELETE ON image " +
                "FOR EACH ROW " +
                "BEGIN " +
                "UPDATE site SET blueprint_id = 0 WHERE blueprint_id = OLD._id; " +
                "END;"

        const val DROP_SIGN_TABLE = "DROP TABLE IF EXISTS  ${Sign.TABLE_NAME}"
        const val DROP_SITE_TABLE = "DROP TABLE IF EXISTS  ${Site.TABLE_NAME}"
        const val DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS  ${Image.TABLE_NAME}"
        const val DROP_FOLD_TABLE = "DROP TABLE IF EXISTS  ${Fold.TABLE_NAME}"

        const val DROP_FOLD_DEFAULT_TRIGGER = "DROP TABLE IF EXISTS AddFoldIdToSiteDefault"
        const val DROP_FOLD_MANAGEMENT_TRIGGER = "DROP TABLE IF EXISTS AddFoldIdToSiteManagement"
        const val DROP_DELETE_IMAGE_UPDATE_SITE_TRIGGER = "DROP TABLE IF EXISTS UpdateBlueprintIdBeforeImageDelete"

    }


    private fun createTable(db: SQLiteDatabase){
        db.execSQL(SIGN_CREATE_TABLE)
        db.execSQL(FOLD_CREATE_TABLE)
        db.execSQL(IMAGE_CREATE_TABLE)
        db.execSQL(SITE_CREATE_TABLE)
    }

    private fun createTrigger(db: SQLiteDatabase){
        db.execSQL(CREATE_ADD_FOLD_DEFAULT_TRIGGER)
        db.execSQL(CREATE_ADD_FOLD_MANAGEMENT_TRIGGER)
        db.execSQL(CREATE_DELETE_IMAGE_UPDATE_SITE_TRIGGER)
    }

    private fun dropAll(db: SQLiteDatabase) {
        db.execSQL(DROP_SIGN_TABLE)
        db.execSQL(DROP_SITE_TABLE)
        db.execSQL(DROP_IMAGE_TABLE)
        db.execSQL(DROP_FOLD_TABLE)
        db.execSQL(DROP_FOLD_DEFAULT_TRIGGER)
        db.execSQL(DROP_FOLD_MANAGEMENT_TRIGGER)
        db.execSQL(DROP_DELETE_IMAGE_UPDATE_SITE_TRIGGER)
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
        createTrigger(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        dropAll(db)
        createTable(db)
        createTrigger(db)
    }

}