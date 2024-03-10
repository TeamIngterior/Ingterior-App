package com.ing.ingterior.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.ing.ingterior.db.Site.Companion.FAVORITE
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
        private const val URI_SITE_FAVORITE_ID                  = "site/favorite/#"
        private const val URI_IMAGE_ID                          = "image/#"

        private const val MATCH_SIGN                            = 0
        private const val MATCH_SIGN_USER_ID                    = 1
        private const val MATCH_SITE                            = 2
        private const val MATCH_SITE_ID                         = 3
        private const val MATCH_SITE_FAVORITE_ID                = 4
        private const val MATCH_IMAGE_ID                        = 5
    }

    init {
        URI_MATCHER.addURI(AUTHORITY, URI_SIGN, MATCH_SIGN)
        URI_MATCHER.addURI(AUTHORITY, URI_SIGN_USER_ID, MATCH_SIGN_USER_ID)
        URI_MATCHER.addURI(AUTHORITY, URI_SITE, MATCH_SITE)
        URI_MATCHER.addURI(AUTHORITY, URI_SITE_ID, MATCH_SITE_ID)
        URI_MATCHER.addURI(AUTHORITY, URI_SITE_FAVORITE_ID, MATCH_SITE_FAVORITE_ID)
        URI_MATCHER.addURI(AUTHORITY, URI_IMAGE_ID, MATCH_IMAGE_ID)
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
        try {
            db.beginTransaction()
            val matcher = URI_MATCHER.match(uri)
            Log.d(TAG, "query: uri=$uri, matcher=$matcher, selection=$selection, selectionArgs=${Arrays.toString(selectionArgs)}")
            when (matcher) {
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
                MATCH_IMAGE_ID -> {
                    val imageId = uri.lastPathSegment?.toLong() ?: return null
                    cursor = db.rawQuery(Image.QUERY_ID, arrayOf(imageId.toString()), null)
                }
                else -> throw UnsupportedOperationException(NO_DELETES_INSERTS_OR_UPDATES + uri)
            }
        } catch (e: Exception) {
            Log.e(TAG, "update error: ", e)
        } finally {
            db.endTransaction()
        }
        return cursor
    }

    override fun getType(uri: Uri): String {
        return "vnd.android-dir/$AUTHORITY"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? = runBlocking {
        var rsUri: Uri? = null
        val db = dbHelper.writableDatabase
        try {
            db.beginTransaction()
            val matcher = URI_MATCHER.match(uri)
            Log.d(TAG, "insert: uri=$uri, matcher=$matcher")
            when(matcher) {
                MATCH_SIGN -> {
                    val rowId = db.insert(Sign.TABLE_NAME, null, contentValues)
                    db.setTransactionSuccessful()
                    rsUri = Uri.parse("$uri/$rowId")
                    context?.contentResolver?.notifyChange(rsUri, null)
                }
                MATCH_SITE -> {
                    if(contentValues == null) return@runBlocking null
                    val operator = uri.getQueryParameter(Site.EXTRA_SITE_OPERATOR)?: return@runBlocking null

                    val bluePrintValues = ContentValues(3)
                    bluePrintValues.put(Image.LOCATION, contentValues.getAsString(Image.LOCATION))
                    bluePrintValues.put(Image.FILENAME, contentValues.getAsString(Image.FILENAME))
                    bluePrintValues.put(Image.CREATED_DATE, Date().time)
                    Log.d(TAG, "insert: contentValues=$contentValues")

                    val bluePrintId:Long = if(contentValues.getAsString(Image.LOCATION).isEmpty()) 0
                    else{ db.insert(Image.TABLE_NAME, null, bluePrintValues) }
                    Log.d(TAG, "insert: bluePrintId=$bluePrintId")

                    val siteValues = ContentValues().apply {
                        put(Site.CREATOR_ID, contentValues.getAsLong(Sign.USER_ID))
                        put(Site.PARTICIPANT_IDS, contentValues.getAsLong(Sign.USER_ID).toString())
                        put(Site.NAME, contentValues.getAsString(Site.NAME))
                        put(Site.CODE, contentValues.getAsString(Site.CODE))
                        put(Site.BLUEPRINT_ID, bluePrintId)
                        put(Site.CREATED_DATE, Date().time)
                        put(Site.FAVORITE, 0)
                    }

                    val rowId = db.insert(Site.TABLE_NAME, null, siteValues)
                    Log.d(TAG, "insert: rowId=$rowId")

                    when(operator.toInt()) {
                        Fold.FOLD_DEFECTS -> {
                            insertEmptyFold(db, Fold.FOLD_DEFECTS, contentValues.getAsLong(Sign.USER_ID), rowId)
                        }
                        Fold.FOLD_MANAGEMENT -> {
                            insertEmptyFold(db, Fold.FOLD_MANAGEMENT, contentValues.getAsLong(Sign.USER_ID), rowId)
                        }
                        Fold.FOLD_ALL -> {
                            insertEmptyFold(db, Fold.FOLD_DEFECTS, contentValues.getAsLong(Sign.USER_ID), rowId)
                            insertEmptyFold(db, Fold.FOLD_MANAGEMENT, contentValues.getAsLong(Sign.USER_ID), rowId)
                        }
                        else -> {
                            insertEmptyFold(db, Fold.FOLD_DEFECTS, contentValues.getAsLong(Sign.USER_ID), rowId)
                        }
                    }
                    rsUri = Uri.parse("${Site.CONTENT_URI}/$rowId")
                    context?.contentResolver?.notifyChange(rsUri, null)
                    db.setTransactionSuccessful()
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        } catch (e: Exception) {
            Log.e(TAG, "update error: ", e)
        } finally {
            db.endTransaction()
        }
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

    private fun deleteFoldFromSite(db: SQLiteDatabase, foldType: Int, siteId: Long) {
        db.delete(Fold.TABLE_NAME, "${Fold.SITE_ID} = ? AND ${Fold.TYPE} = ?", arrayOf(siteId.toString(), foldType.toString()))
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        try {
            db.beginTransaction()
            val matcher = URI_MATCHER.match(uri)
            Log.d(TAG, "delete: uri=$uri, matcher=$matcher, selection=$selection, selectionArgs=${selectionArgs.contentToString()}")
            when (matcher) {
                MATCH_IMAGE_ID -> {
                    val imageId = uri.lastPathSegment?.toLong() ?: return 0.also { db.endTransaction() }
                    db.delete(Image.TABLE_NAME, "${Image._ID} = ?" , arrayOf(imageId.toString()))
                    db.setTransactionSuccessful()
                }
                MATCH_SITE_ID -> {
                    val siteId = uri.lastPathSegment?.toLong() ?: return 0.also { db.endTransaction() }
                    db.delete(Site.TABLE_NAME, "${Site._ID} = ?" , arrayOf(siteId.toString()))
                    db.setTransactionSuccessful()
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        } catch (e: Exception) {
            Log.e(TAG, "delete error: ", e)
            return 0
        } finally {
            db.endTransaction()
        }
        return 1
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        try {
            db.beginTransaction()
            val matcher = URI_MATCHER.match(uri)
            Log.d(TAG, "update: uri=$uri, matcher=$matcher, contentValues=$contentValues, selection=$selection")
            when (matcher) {
                MATCH_SITE_FAVORITE_ID -> {
                    val siteId = uri.lastPathSegment?.toLong() ?: return 0.also { db.endTransaction() }
                    val favoriteValue = contentValues?.getAsInteger(FAVORITE) ?: return 0.also { db.endTransaction() }
                    Log.d(TAG, "update: MATCH_SITE_FAVORITE_ID, siteId=$siteId, favoriteValue=$favoriteValue")
                    val updateQuery = "UPDATE site SET favorite = $favoriteValue WHERE _id = $siteId"
                    Log.d(TAG, "update: MATCH_SITE_FAVORITE_ID, updateQuery=$updateQuery")
                    db.execSQL(updateQuery)
                    db.setTransactionSuccessful()
                }
                MATCH_SITE_ID -> {
                    if(contentValues == null) return 0.also { db.endTransaction() }
                    val operator = uri.getQueryParameter(Site.EXTRA_SITE_OPERATOR) ?: return 0.also { db.endTransaction() }
                    val siteId = uri.lastPathSegment?.toLong() ?: return 0.also { db.endTransaction() }
                    val siteQueryCursor = db.rawQuery(Site.QUERY_ALL, arrayOf(siteId.toString()), null) ?: return 0.also { db.endTransaction() }
                    var bluePrintId = if(siteQueryCursor.moveToFirst()) {
                        siteQueryCursor.getLong(siteQueryCursor.getColumnIndexOrThrow(Site.BLUEPRINT_ID))
                    }
                    else 0L
                    siteQueryCursor.close()

                    if(contentValues.containsKey(Image.LOCATION)){
                        val imageValues = ContentValues(3)
                        imageValues.put(Image.LOCATION, contentValues.getAsString(Image.LOCATION))
                        imageValues.put(Image.FILENAME, contentValues.getAsString(Image.FILENAME))
                        imageValues.put(Image.CREATED_DATE, Date().time)

                        if(bluePrintId > 0) {
                            db.update(Image.TABLE_NAME, contentValues, "${Image._ID} = ?", arrayOf(bluePrintId.toString()))
                        }
                        else {
                            bluePrintId = db.insert(Image.TABLE_NAME, null, imageValues)
                        }
                    }
                    else{
                        db.delete(Image.TABLE_NAME, "${Image._ID} = ?", arrayOf(bluePrintId.toString()))
                    }

                    val siteValues = ContentValues().apply {
                        put(Site.CREATOR_ID, contentValues.getAsLong(Sign.USER_ID))
                        put(Site.PARTICIPANT_IDS, contentValues.getAsLong(Sign.USER_ID).toString())
                        put(Site.NAME, contentValues.getAsString(Site.NAME))
                        put(Site.CODE, contentValues.getAsString(Site.CODE))
                        put(Site.BLUEPRINT_ID, bluePrintId)
                        put(Site.CREATED_DATE, Date().time)
                        put(Site.FAVORITE, 0)
                    }

                    val rs = db.update(Site.TABLE_NAME, siteValues, null, null)
                    Log.d(TAG, "update: rs=$rs")

                    when(operator.toInt()) {
                        Fold.FOLD_DEFECTS -> {
                            insertEmptyFold(db, Fold.FOLD_DEFECTS, contentValues.getAsLong(Sign.USER_ID), siteId)
                            deleteFoldFromSite(db, Fold.FOLD_MANAGEMENT, siteId)
                        }
                        Fold.FOLD_MANAGEMENT -> {
                            insertEmptyFold(db, Fold.FOLD_MANAGEMENT, contentValues.getAsLong(Sign.USER_ID), siteId)
                            deleteFoldFromSite(db, Fold.FOLD_DEFECTS, siteId)
                        }
                        Fold.FOLD_ALL -> {
                            insertEmptyFold(db, Fold.FOLD_DEFECTS, contentValues.getAsLong(Sign.USER_ID), siteId)
                            insertEmptyFold(db, Fold.FOLD_MANAGEMENT, contentValues.getAsLong(Sign.USER_ID), siteId)
                        }
                    }
                    db.setTransactionSuccessful()
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
            }
        } catch (e: Exception) {
            Log.e(TAG, "update error: ", e)
            return 0
        } finally {
            db.endTransaction()
        }
        return 1
    }

}