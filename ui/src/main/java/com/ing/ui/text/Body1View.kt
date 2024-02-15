package com.ing.ui.text

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ing.ui.Config.BODY1_FONT_SIZE
import com.ing.ui.Config.H1_FONT_SIZE
import com.ing.ui.Config.REGULAR
import com.ing.ui.Config.SEMI_BOLD
import com.ing.ui.R

class Body1View : BaseTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        textSize = BODY1_FONT_SIZE
        val font: Typeface = resources.getFont(REGULAR)
        typeface = font
        setTextColor(ContextCompat.getColor(context, R.color.text_color_06))
    }
}