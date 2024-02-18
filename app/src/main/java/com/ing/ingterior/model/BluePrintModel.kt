package com.ing.ingterior.model

import android.graphics.Bitmap
import android.net.Uri


data class BluePrintModel(var id: Long = 0L, val uri: Uri, var name: String, var rotation: Float = 0f, var horizontalInversion: Boolean = false, var verticalInversion: Boolean = false, var bitmap: Bitmap? = null) {
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

        other as BluePrintModel

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