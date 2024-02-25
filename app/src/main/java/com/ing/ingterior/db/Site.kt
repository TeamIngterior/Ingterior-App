package com.ing.ingterior.db

data class Site(val _id: Long, val creatorId: Long, val participantIds: String, val name: String, val code: String,
                var defaultIds: String = "", var managementIds:String = "", var blueprintId: Long=0, var createdDate:Long,
                var bluePrintName: String = "", var bluePrintLocation: String = "") {

    companion object{
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

        const val CONTENT_URI = "content://ingterior/${TABLE_NAME}"

        const val QUERY_ALL = "SELECT site.${Site._ID}, site.${Site.CREATOR_ID}, site.${Site.PARTICIPANT_IDS}, site.${Site.NAME}, site.${Site.CODE}, site.${Site.DEFAULT_IDS}, site.${Site.MANAGEMENT_IDS}, site.${Site.BLUEPRINT_ID}, site.${Site.CREATED_DATE}, " +
                "image.${Image.FILENAME}, image.${Image.LOCATION} FROM ${Site.TABLE_NAME} AS site JOIN ${Image.TABLE_NAME} AS image ON site.${Site.BLUEPRINT_ID} = image.${Image._ID} " +
                "WHERE ',' || site.${Site.PARTICIPANT_IDS} || ',' LIKE '%,' || ? || ',%'"

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
            Image.FILENAME,         // 10
            Image.LOCATION,         // 11
        )
    }

}