package com.ing.ingterior.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.ing.ingterior.db.Image
import com.ing.ingterior.util.ImageUtils.GraphicUtils.loadBitmapFromFile


data class ImageModel(var id: Long = 0L, val uri: Uri? = null, var location: String? = null, var name: String, var rotation: Float = 0f, var horizontalInversion: Boolean = false, var verticalInversion: Boolean = false, var bitmap: Bitmap? = null) {

    companion object{
        fun loadImage(context: Context, id: Long):ImageModel? {
            var imageModel: ImageModel? = null
            val cursor = context.contentResolver.query(Uri.withAppendedPath(Uri.parse(Image.CONTENT_URI), id.toString()), null, null, null)
            if(cursor!=null) {
                if(cursor.moveToFirst()) {
                    imageModel = ImageModel(
                        cursor.getLong(cursor.getColumnIndexOrThrow(Image._ID)),
                        null,
                        cursor.getString(cursor.getColumnIndexOrThrow(Image.LOCATION)) ?: "",
                        cursor.getString(cursor.getColumnIndexOrThrow(Image.FILENAME)),
                    0f,false,false,
                        loadBitmapFromFile(cursor.getString(cursor.getColumnIndexOrThrow(Image.LOCATION)) ?: "")
                    )
                }
                cursor.close()
            }
            return imageModel
        }
    }

    fun increaseRotation(): Float {
        rotation = (rotation + 90f) % 360f
        return rotation
    }

    fun updateHorizontalInversion(): Boolean{
        horizontalInversion = !horizontalInversion
        return horizontalInversion
    }


    fun updateVerticalInversion(): Boolean{
        verticalInversion = !verticalInversion
        return verticalInversion
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageModel

        if (id != other.id) return false
        if (uri != other.uri) return false
        if (name != other.name) return false
        if (rotation != other.rotation) return false
        if (horizontalInversion != other.horizontalInversion) return false
        if (verticalInversion != other.verticalInversion) return false

        return true
    }


    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + horizontalInversion.hashCode()
        result = 31 * result + verticalInversion.hashCode()
        return result
    }


}