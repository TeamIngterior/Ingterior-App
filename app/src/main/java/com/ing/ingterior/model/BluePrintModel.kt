package com.ing.ingterior.model

import android.net.Uri


data class BluePrintModel(var id: Long = 0L, val uri: Uri, var name: String, var rotation: Float, var horizontalInversion: Boolean = false, var verticalInversion: Boolean = false) {
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
        if(other is BluePrintModel) {
            return uri == other.uri
        }
        return false
    }

    override fun hashCode(): Int {
        var result = uri.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + horizontalInversion.hashCode()
        result = 31 * result + verticalInversion.hashCode()
        return result
    }


}