package com.ing.ui.button

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.ing.ui.R

class ImageBackButton : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){

        isClickable = true
        background = ContextCompat.getDrawable(context, R.drawable.bg_circle_effect)
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_24))
        foregroundTintList = ContextCompat.getColorStateList(context, R.color.black)
        val allPadding = context.resources.getDimensionPixelSize(R.dimen.back_button_padding)
        setPadding(allPadding, allPadding, allPadding, allPadding)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val allSize = context.resources.getDimensionPixelSize(R.dimen.back_button_size)
        val exactMeasureSpec = MeasureSpec.makeMeasureSpec(allSize, MeasureSpec.EXACTLY)
        super.onMeasure(exactMeasureSpec, exactMeasureSpec)
    }
}