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
        val allPadding = context.resources.getDimensionPixelSize(R.dimen.image_button_padding)
        setPadding(allPadding, allPadding, allPadding, allPadding)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VisualImageButton, defStyleAttr, 0)
            setBackgroundResource(typedArray.getResourceId(R.styleable.VisualImageButton_visualBackground, R.drawable.bg_circle_effect))
            typedArray.recycle()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val allSize = context.resources.getDimensionPixelSize(R.dimen.image_button_size)
        val exactMeasureSpec = MeasureSpec.makeMeasureSpec(allSize, MeasureSpec.EXACTLY)
        super.onMeasure(exactMeasureSpec, exactMeasureSpec)
    }
}