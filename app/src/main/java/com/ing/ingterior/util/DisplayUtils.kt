package com.ing.ingterior.util

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.WindowMetrics

object DisplayUtils {
    fun dialogWidthChange(dialog: Dialog, ratio: Float) {
        val context = dialog.context
        val params = dialog.window!!.attributes

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics: WindowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
            val deviceWidth = metrics.bounds.width()
            params.width = (deviceWidth * ratio).toInt()
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
            val deviceWidth = displayMetrics.widthPixels
            params.width = (deviceWidth * ratio).toInt()
        }

        dialog.window!!.attributes = params
    }
}