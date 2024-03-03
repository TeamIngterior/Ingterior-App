package com.ing.ingterior.db

data class Fold(val id: Long, val siteId: Long, val type: Int, val name: String, val memo: String, val imageIds: String,
                val creatorId:Long, val creatorType: Long, val position: String, val createdDate: Long){
    companion object{
        const val FOLD_DEFAULT = 1
        const val FOLD_MANAGEMENT = 2
        const val FOLD_ALL = 3


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
}
