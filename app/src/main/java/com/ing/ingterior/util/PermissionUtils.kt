package com.ing.ingterior.util

import android.content.Context
import android.content.pm.PackageManager
import com.ing.ingterior.cameraPermission
import com.ing.ingterior.storagePermissions

object PermissionUtils {
    const val REQUEST_CAMERA_PERMISSION = 1777
    fun hasAllPermissions(context: Context): Boolean {
        for(permission in storagePermissions){
            if(!hasPermission(context, permission)) return false
        }
        if(!hasPermission(context, cameraPermission)) return false
        return true
    }

    fun hasStoragePermissions(context: Context): Boolean {
        for(permission in storagePermissions){
            if(!hasPermission(context, permission)) return false
        }
        return true
    }

    fun hasCameraPermission(context: Context): Boolean {
        return hasPermission(context, cameraPermission)
    }

    private fun hasPermission(context: Context, strPermission: String): Boolean {
        val state: Int = context.checkSelfPermission(strPermission)
        return state == PackageManager.PERMISSION_GRANTED
    }
}