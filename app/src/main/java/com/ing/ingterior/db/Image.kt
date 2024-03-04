package com.ing.ingterior.db

data class Image(val id: Long, val location: String, val fileName: String, val createdDate: Long){
    companion object{
        const val TABLE_NAME = "image"
        const val _ID = "_id"
        const val LOCATION = "location"
        const val FILENAME = "filename"
        const val CREATED_DATE = "created_date"

        const val CONTENT_URI = "content://ingterior/${TABLE_NAME}"
        const val QUERY_ID = "SELECT * FROM $TABLE_NAME WHERE $_ID = ?"
    }
}
