package com.ing.ingterior

import android.Manifest

// permission
var storagePermissions = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

var cameraPermission = Manifest.permission.CAMERA

const val EXTRA_MOVE_INDEX = "extra_move_index"
const val EXTRA_SITE = "extra_site"
const val EXTRA_EVENT = "extra_event"

val DIALOG_LAND_WIDTH_RATIO = 0.85F
val DIALOG_PORT_WIDTH_RATIO = 0.95F

val IMAGE_LAND_WIDTH_RATIO = 0.6F
val IMAGE_PORT_WIDTH_RATIO = 0.75F