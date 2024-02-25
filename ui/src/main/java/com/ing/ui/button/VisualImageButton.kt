package com.ing.ui.button

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.ing.ui.R

class VisualImageButton : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){

        isClickable = true
        foregroundTintList = ContextCompat.getColorStateList(context, R.color.image_button_tint_color)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VisualImageButton, defStyleAttr, 0)
            val styleAttr = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.padding))
            setBackgroundResource(typedArray.getResourceId(R.styleable.VisualImageButton_visualBackground, R.drawable.bg_circle_effect))
            var padding = styleAttr.getDimensionPixelSize(0, 0)
            if(padding == 0) padding = context.resources.getDimensionPixelSize(R.dimen.image_button_padding)
            setPadding(padding, padding, padding, padding)
            styleAttr.recycle()
            typedArray.recycle()
        }

    }
}