package com.ing.ui.button

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.ing.ui.Config
import com.ing.ui.R

class VisualButton : androidx.appcompat.widget.AppCompatButton {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        isClickable = true
        isFocusable = true

        textSize = Config.LABEL_FONT_SIZE
        val font: Typeface = resources.getFont(Config.MEDIUM)
        typeface = font

        setTextColor(ContextCompat.getColor(context, R.color.text_color_01))
        background = ContextCompat.getDrawable(context, R.drawable.bg_ing_button)
        gravity = Gravity.CENTER
    }
}