package com.ing.ingterior.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.*
import java.util.*


class IngTeriorProvider : ContentProvider() {

    companion object{
        private const val TAG = "IngTeriorProvider"
        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        private const val NO_DELETES_INSERTS_OR_UPDATES = "IngTeriorProvider does not support deletes, inserts, or updates for this URI."

        private const val URI_SIGN                              = "sign"
        private const val URI_SIGN_USER_ID                      = "sign/#"
        private const val URI_SITE                              = "site"
        private const val URI_SITE_ID                           = "site/#"

        private const val MATCH_SIGN                            = 0
        private const val MATCH_SIGN_USER_ID                    = 1
        private const val MATCH_SITE                            = 2
        private const val MATCH_SITE_ID                         = 3
    }

    init {
        URI_MATCHER.addURI(AUTHORITY, URI_SIGN, MATCH_SIGN)
        URI_MATCHER.addURI(AUTHORITY, URI_SIGN_USER_ID, MATCH_SIGN_USER_ID)
        URI_MATCHER.addURI(AUTHORITY, URI_SITE, MATCH_SITE)
        URI_MATCHER.addURI(AUTHORITY, URI_SITE_ID, MATCH_SITE_ID)
    }

    private val dbHelper: IngTeriorDBHelper by lazy { IngTeriorDBHelper(context!!) }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var cursor: Cursor? = null
        val db = dbHelper.readableDatabase
        val matcher = URI_MATCHER.match(uri)
        Log.d(TAG, "query: uri=$uri, matcher=$matcher, selection=$selection, selectionArgs=${Arrays.toString(selectionArgs)}")
        when(matcher) {
            MATCH_SIGN -> {
                cursor = db.query(Sign.TABLE_NAME, Sign.PROJECTION, null, null, null, null, null)
            }
            MATCH_SIGN_USER_ID -> {
                val userId = uri.lastPathSegment?.toLong() ?: return null
                if(userId > 0L) {
                    cursor = db.query(Sign.TABLE_NAME, Sign.PROJECTION, "${Sign.USER_ID} = ?", arrayOf(userId.toString()),
                        null, null, null)
                }
            }
            MATCH_SITE  -> {
                cursor = db.rawQuery(Site.QUERY_ALL, selectionArgs, null)
            }
            MATCH_SITE_ID -> {
                val siteId = uri.lastPathSegment?.toLong() ?: return null
                cursor = db.rawQuery(Site.QUERY_ALL, arrayOf(siteId.toString()), null)
            }
            else -> throw UnsupportedOperationException(NO_DELETES_INSERTS_OR_UPDATES + uri)
        }
        return cursor
    }

    override fun getType(uri: Uri): String {
        return "vnd.android-dir/$AUTHORITY"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? = runBlocking {
        var rsUri: Uri? = null
        val db = dbHelper.writableDatabase
        val matcher = URI_MATCHER.match(uri)
        Log.d(TAG, "insert: uri=$uri, matcher=$matcher")
        when(matcher) {
            MATCH_SIGN -> {
                val rowId = db.insert(Sign.TABLE_NAME, null, contentValues)
                rsUri = Uri.parse("$uri/$rowId")
            }
            MATCH_SITE -> {
                if(contentValues == null) return@runBlocking null
                val operator = uri.getQueryParameter(Site.EXTRA_SITE_OPERATOR)?: return@runBlocking null

                val bluePrintId:Long = withContext(Dispatchers.IO){
                    if(contentValues.getAsString(Image.LOCATION).isEmpty()) -1L
                    else{
                        Log.d(TAG, "insert: contentValues=$contentValues")
                        val bluePrintValues = ContentValues(3)
                        bluePrintValues.put(Image.LOCATION, contentValues.getAsString(Image.LOCATION))
                        bluePrintValues.put(Image.FILENAME, contentValues.getAsString(Image.FILENAME))
                        bluePrintValues.put(Image.CREATED_DATE, Date().time)
                        db.insert(Image.TABLE_NAME, null, bluePrintValues)
                    }
                }
                Log.d(TAG, "insert: bluePrintId=$bluePrintId")

                val siteValues = ContentValues().apply {
                    put(Site.CREATOR_ID, contentValues.getAsLong(Sign.USER_ID))
                    put(Site.PARTICIPANT_IDS, contentValues.getAsLong(Sign.USER_ID).toString())
                    put(Site.NAME, contentValues.getAsString(Site.NAME))
                    put(Site.CODE, contentValues.getAsString(Site.CODE))
                    put(Site.BLUEPRINT_ID, bluePrintId)
                    put(Site.CREATED_DATE, Date().time)
                }

                val rowId = withContext(Dispatchers.IO) {
                    db.insert(Site.TABLE_NAME, null, siteValues)
                }
                Log.d(TAG, "insert: rowId=$rowId")

                when(operator.toInt()) {
                    Fold.FOLD_DEFAULT -> {
                        insertEmptyFold(db, Fold.FOLD_DEFAULT, contentValues.getAsLong(Sign.USER_ID), rowId)
                    }
                    Fold.FOLD_MANAGEMENT -> {
                        insertEmptyFold(db, Fold.FOLD_MANAGEMENT, contentValues.getAsLong(Sign.USER_ID), rowId)
                    }
                    Fold.FOLD_ALL -> {
                        insertEmptyFold(db, Fold.FOLD_DEFAULT, contentValues.getAsLong(Sign.USER_ID), rowId)
                        insertEmptyFold(db, Fold.FOLD_MANAGEMENT, contentValues.getAsLong(Sign.USER_ID), rowId)
                    }
                    else -> {
                        insertEmptyFold(db, Fold.FOLD_DEFAULT, contentValues.getAsLong(Sign.USER_ID), rowId)
                    }
                }
                rsUri = Uri.parse("${Site.CONTENT_URI}/$rowId")
            }
            else -> throw UnsupportedOperationException(NO_DELETES_INSERTS_OR_UPDATES + uri)
        }
        Log.d(TAG, "insert: rsUri=$rsUri")
        return@runBlocking rsUri
    }

    private fun insertEmptyFold(db: SQLiteDatabase, foldType: Int, userId: Long, siteId: Long) {
        val foldValues = ContentValues()
        foldValues.put(Fold.TYPE, foldType)
        foldValues.put(Fold.SITE_ID, siteId)
        foldValues.put(Fold.CREATOR_ID, userId)
        foldValues.put(Fold.CREATOR_TYPE, Site.SITE_MANAGER)
        foldValues.put(Fold.CREATED_DATE, Date().time)
        db.insert(Fold.TABLE_NAME, null, foldValues)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }
}