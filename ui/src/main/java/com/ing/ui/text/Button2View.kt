package com.ing.ui.text

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.ing.ui.Config.BUTTON1_FONT_SIZE
import com.ing.ui.Config.BUTTON2_FONT_SIZE
import com.ing.ui.Config.MEDIUM
import com.ing.ui.R

class Button2View : BaseTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        textSize = BUTTON2_FONT_SIZE
        val font: Typeface = resources.getFont(MEDIUM)
        typeface = font
        setTextColor(ContextCompat.getColor(context, R.color.text_color_06))
    }
}