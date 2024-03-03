package com.ing.ingterior

import android.Manifest

// permission
var storagePermissions = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

var cameraPermission = Manifest.permission.CAMERA

const val EXTRA_MOVE_INDEX = "move_index"
