package com.ing.ui.button

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.ing.ui.R
import com.ing.ui.text.body.Body1View
import com.ing.ui.text.label.LabelView

class VisualDotLineButton : LinearLayoutCompat {

    private val bodyView: Body1View by lazy {
        Body1View(context).apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER_VERTICAL }
            setTextColor(ContextCompat.getColor(context, R.color.text_color_05))
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        isClickable = true
        isFocusable = true
        background = ContextCompat.getDrawable(context, R.drawable.bg_dot_line_button)

        orientation = LinearLayoutCompat.HORIZONTAL
        gravity = Gravity.CENTER
        addView(bodyView)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VisualDotLineButton, defStyleAttr, 0)
            bodyView.text = typedArray.getString(R.styleable.VisualDotLineButton_dotText)
            typedArray.recycle()
        }
    }
}