package com.ing.ui.button

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.ing.ui.Config
import com.ing.ui.R
import com.ing.ui.text.label.LabelView

class VisualButton : BaseButtonView {

    private val labelView: LabelView by lazy {
        LabelView(context).apply {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
        gravity = Gravity.CENTER

        labelView.setTextColor(ContextCompat.getColor(context, R.color.text_color_01))
        background = ContextCompat.getDrawable(context, R.drawable.bg_visual_button)


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseButtonView, defStyleAttr, 0)
            val text = (typedArray.getText(R.styleable.BaseButtonView_visualText)) ?: ""
            labelView.text = text
            val textSize = (typedArray.getFloat(R.styleable.BaseButtonView_visualTextSize, Config.LABEL_FONT_SIZE))
            labelView.textSize = textSize

            val lineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.BaseButtonView_visualLineSpacingExtra, 0)
            labelView.setLineSpacing(lineSpacingExtra.toFloat(), 1.0f)

            isEnabled = typedArray.getBoolean(R.styleable.BaseButtonView_visualEnable, true)

            typedArray.recycle()
        }

        addView(labelView)
    }

    fun setText(text: String?) {
        labelView.text = text
    }
}