package com.ing.ui.text.edit

import android.content.Context
import android.graphics.Typeface
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ing.ui.Config
import com.ing.ui.Config.LABEL_FONT_SIZE
import com.ing.ui.R
import java.lang.reflect.Field


class InputTextView: androidx.appcompat.widget.AppCompatEditText {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        maxLines = 1
        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true
        background = ContextCompat.getDrawable(context, R.drawable.bg_input_text_view)

        val horizontalPadding = context.resources.getDimensionPixelSize(R.dimen.horizontal_input_text_padding)
        val verticalPadding = context.resources.getDimensionPixelSize(R.dimen.vertical_input_text_padding)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)
        textSize = LABEL_FONT_SIZE

        val font: Typeface = resources.getFont(Config.MEDIUM)
        typeface = font

        setTextColor(ContextCompat.getColor(context, R.color.text_color_06))
        setHintTextColor(ContextCompat.getColor(context, R.color.text_color_04))
    }

}