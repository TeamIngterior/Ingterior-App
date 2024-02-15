package com.ing.ui.text

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.ing.ui.R

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

        setTextColor(ContextCompat.getColor(context, R.color.text_color_06))
        setHintTextColor(ContextCompat.getColor(context, R.color.text_color_04))

    }
}