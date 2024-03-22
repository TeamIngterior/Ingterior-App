package com.ing.ingterior.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.ing.ingterior.R


inline fun <reified T : Parcelable> Intent.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(key) as T?
    }
}
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        @Suppress("UNCHECKED_CAST")
        getParcelable(key) as T?
    }
}

fun Resources.getFloatCompat(resourceId: Int): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        getFloat(R.dimen.dialog_land_width_ratio)
    }
    else{
        val typedValue = TypedValue()
        getValue(resourceId, typedValue, true)
        typedValue.float
    }
}

fun Activity.getDisplayPixelSize(): Size {
    val displayMetrics = DisplayMetrics()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout())
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom
        // Window size including system bars and display cutout
        val bounds = windowMetrics.bounds
        val width = bounds.width() - insetsWidth
        val height = bounds.height() - insetsHeight
        displayMetrics.widthPixels = width
        displayMetrics.heightPixels = height
    } else {
        @Suppress("DEPRECATION")
        val display = windowManager.defaultDisplay
        @Suppress("DEPRECATION")
        display.getMetrics(displayMetrics)
    }
    return Size(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun EditText.hideKeyboard(context: Context) {
    val inputMethodManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    postDelayed({
        clearFocus()
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }, 0)
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}
