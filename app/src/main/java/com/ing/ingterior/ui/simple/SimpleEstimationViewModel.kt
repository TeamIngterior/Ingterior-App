package com.ing.ingterior.ui.simple

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.ing.ingterior.R

class SimpleEstimationViewModel : ViewModel() {

    var meter = 0L
    var isWindow = true
    var balconyCount = 0
    var bathroomCount = 1

    fun setSelectedTextView(view: TextView) {
        val context = view.context
        view.setTextColor(ContextCompat.getColor(context, R.color.text_color_01))
        view.backgroundTintList = ContextCompat.getColorStateList(context, R.color.primary_color05)
    }

    fun setDefaultTextView(view: TextView) {
        val context = view.context
        view.setTextColor(ContextCompat.getColor(context, R.color.text_color_04))
        view.backgroundTintList = ContextCompat.getColorStateList(context, R.color.gray_02)
    }
}