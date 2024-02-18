package com.ing.ingterior.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.ing.ingterior.db.Image.FILENAME
import com.ing.ingterior.db.Image.LOCATION
import com.ing.ingterior.db.Sign.USER_ID
import com.ing.ingterior.db.Site.CREATE_DATE
import com.ing.ingterior.db.Site.BLUEPRINT_ID
import com.ing.ingterior.db.Site.CODE
import com.ing.ingterior.db.Site.NAME
import com.ing.ingterior.db.Site.QUERY_ALL
import kotlinx.coroutines.*
import java.util.Date


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
        when(URI_MATCHER.match(uri)) {
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
            MATCH_SITE_ID -> {
                val siteId = uri.lastPathSegment?.toLong() ?: return null
                cursor = db.rawQuery(QUERY_ALL, arrayOf(siteId.toString()), null)
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
                val bluePrintId:Long = withContext(Dispatchers.IO){
                    if(contentValues.getAsString(LOCATION).isEmpty()) -1L
                    else{
                        Log.d(TAG, "insert: contentValues=$contentValues")
                        val bluePrintValues = ContentValues(3)
                        bluePrintValues.put(LOCATION, contentValues.getAsString(LOCATION))
                        bluePrintValues.put(FILENAME, contentValues.getAsString(FILENAME))
                        bluePrintValues.put(CREATE_DATE, Date().time)
                        db.insert(Image.TABLE_NAME, null, bluePrintValues)
                    }
                }

                if (bluePrintId != -1L) {
                    val siteValues = ContentValues().apply {
                        put(USER_ID, contentValues.getAsLong(USER_ID))
                        put(NAME, contentValues.getAsString(NAME))
                        put(CODE, contentValues.getAsString(CODE))
                        put(BLUEPRINT_ID, bluePrintId)
                        put(CREATE_DATE, Date().time)
                    }

                    val rowId = withContext(Dispatchers.IO) {
                        db.insert(Site.TABLE_NAME, null, siteValues)
                    }
                    rsUri = Uri.parse("$uri/$rowId")
                }
            }
            else -> throw UnsupportedOperationException(NO_DELETES_INSERTS_OR_UPDATES + uri)
        }
        Log.d(TAG, "insert: rsUri=$rsUri")
        return@runBlocking rsUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }
}