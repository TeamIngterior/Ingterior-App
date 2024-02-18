package com.ing.ui.button

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat

class VisualButton2 : LinearLayoutCompat {

    val imageView:ImageView by lazy { ImageView(context) }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        isClickable = true
        isFocusable = true

        orientation = HORIZONTAL
        gravity = Gravity.CENTER
    }
}