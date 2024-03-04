package com.ing.ingterior.db

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class Site(val _id: Long, val creatorId: Long, val participantIds: String, val siteName: String, val siteCode: String,
                var defaultIds: String = "", var managementIds:String = "", var imageId: Long=0, var createdDate:Long,
                var imageName: String = "", var imageLocation: String = "", var favorite: Boolean = false) : Parcelable {

    companion object{
        const val TAG = "Site"

        const val SITE_MANAGER = "manager"
        const val SITE_GUEST = "guest"

        const val EXTRA_SITE_OPERATOR = "site_operator"
        const val TABLE_NAME = "site"

        const val _ID = "_id"
        const val CREATOR_ID = "creator_id"
        const val PARTICIPANT_IDS = "participant_ids"
        const val NAME = "name"
        const val CODE = "code"
        const val DEFAULT_IDS = "default_ids"
        const val MANAGEMENT_IDS = "management_ids"
        const val BLUEPRINT_ID = "blueprint_id"
        const val CREATED_DATE = "created_date"
        const val FAVORITE = "favorite"

        const val CONTENT_URI = "content://ingterior/${TABLE_NAME}"
        const val CONTENT_FAVORITE_URI = "content://ingterior/${TABLE_NAME}/$FAVORITE"

        const val QUERY_ALL = "SELECT site.$_ID, site.$CREATOR_ID, site.$PARTICIPANT_IDS, site.$NAME, site.$CODE, site.$DEFAULT_IDS, " +
                "site.$MANAGEMENT_IDS, site.$BLUEPRINT_ID, site.$CREATED_DATE, site.$FAVORITE, " +
                "image.${Image.FILENAME}, image.${Image.LOCATION} FROM $TABLE_NAME AS site LEFT JOIN ${Image.TABLE_NAME} AS image ON site.$BLUEPRINT_ID = image.${Image._ID} " +
                "WHERE ',' || site.$PARTICIPANT_IDS || ',' LIKE '%,' || ? || ',%'"

        const val UPDATE_FAVORITE = "UPDATE $TABLE_NAME SET $FAVORITE = ? WHERE $_ID = ?"

        val ALL_PROJECTION = arrayOf(
            _ID,                    // 0
            CREATOR_ID,             // 1
            PARTICIPANT_IDS,        // 2
            NAME,                   // 3
            CODE,                   // 4
            DEFAULT_IDS,            // 5
            MANAGEMENT_IDS,         // 6
            MANAGEMENT_IDS,         // 7
            BLUEPRINT_ID,           // 8
            CREATED_DATE,           // 9
            FAVORITE,               // 10
            Image.FILENAME,         // 11
            Image.LOCATION,         // 12
        )

        fun updateFavoriteSite(context: Context, siteId:Long, favorite: Boolean) {
            val values = ContentValues()
            values.put(FAVORITE, if(favorite) 1 else 0)
            val rsResult = context.contentResolver.update(Uri.withAppendedPath(Uri.parse(CONTENT_FAVORITE_URI), siteId.toString()), values, null, null)
            Log.d(TAG, "updateFavoriteSite: rsResult=$rsResult")
        }
    }

}