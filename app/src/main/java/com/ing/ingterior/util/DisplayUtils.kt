package com.ing.ingterior.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.WindowMetrics
import com.ing.ingterior.*

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

    fun getPropertyImageSize(activity: Activity): Int {
        val windowMetrics: WindowMetrics = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.windowManager.currentWindowMetrics
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION")
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return calculateWidth(displayMetrics, activity)
        }

        val widthPixels = windowMetrics.bounds.width()
        val isPortrait = activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val factor = if (isPortrait) IMAGE_PORT_WIDTH_RATIO else IMAGE_LAND_WIDTH_RATIO
        val calculatedWidth = widthPixels * factor
        val limit = activity.resources.getDimensionPixelOffset(R.dimen.image_size_limit)
        return if (calculatedWidth > limit) limit else calculatedWidth.toInt()
    }

    @Suppress("DEPRECATION")
    fun calculateWidth(displayMetrics: DisplayMetrics, activity: Activity): Int {
        val widthPixels = displayMetrics.widthPixels
        val isPortrait = activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val factor = if (isPortrait) IMAGE_PORT_WIDTH_RATIO else IMAGE_LAND_WIDTH_RATIO
        val calculatedWidth = widthPixels * factor

        val limit = activity.resources.getDimensionPixelOffset(R.dimen.image_size_limit)
        return if (calculatedWidth > limit) limit else calculatedWidth.toInt()
    }
}