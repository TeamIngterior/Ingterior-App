package com.ing.ingterior.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri


class IngTeriorProvider : ContentProvider() {

    companion object{
        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        private const val NO_DELETES_INSERTS_OR_UPDATES = "IngTeriorProvider does not support deletes, inserts, or updates for this URI."

        private const val URI_LOG                               = "log"
        private const val URI_LOG_USER_ID                       = "log/#"

        private const val MATCH_LOG                             = 0
        private const val MATCH_LOG_USER_ID                     = 1
    }

    init {
        URI_MATCHER.addURI(AUTHORITY, URI_LOG, MATCH_LOG)
        URI_MATCHER.addURI(AUTHORITY, URI_LOG_USER_ID, MATCH_LOG_USER_ID)
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
        when(URI_MATCHER.match(uri)) {
            MATCH_LOG -> {
                cursor = db.query(Log.TABLE_NAME, Log.PROJECTION, null, null, null, null, null)
            }
            MATCH_LOG_USER_ID -> {
                val userId = uri.lastPathSegment?.toLong() ?: -1L
                if(userId > 0L) {
                    cursor = db.query(Log.TABLE_NAME, Log.PROJECTION, "${Log.USER_ID} = ?", arrayOf(userId.toString()),
                        null, null, null)
                }
            }
            else -> throw UnsupportedOperationException(NO_DELETES_INSERTS_OR_UPDATES + uri)
        }
        return cursor
    }

    override fun getType(uri: Uri): String {
        return "vnd.android-dir/$AUTHORITY"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        var rsUri: Uri? = null
        val db = dbHelper.writableDatabase
        when(URI_MATCHER.match(uri)) {
            MATCH_LOG -> {
                val rowId = db.insert(Log.TABLE_NAME, null, contentValues)
                rsUri = Uri.parse("$uri/$rowId")
            }
            else -> throw UnsupportedOperationException(NO_DELETES_INSERTS_OR_UPDATES + uri)
        }
        return rsUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }
}