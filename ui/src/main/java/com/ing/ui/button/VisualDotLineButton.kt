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

class VisualDotLineButton : BaseButtonView {

    private val bodyView: Body1View by lazy {
        Body1View(context).apply {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            gravity = Gravity.CENTER
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
        gravity = Gravity.CENTER
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseButtonView, defStyleAttr, 0)
            bodyView.text = typedArray.getString(R.styleable.BaseButtonView_visualText)

            val lineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.BaseButtonView_visualLineSpacingExtra, 0)
            bodyView.setLineSpacing(lineSpacingExtra.toFloat(), 1.0f)

            typedArray.recycle()
        }

        addView(bodyView)
    }

    fun setText(text: String){
        bodyView.text = text
    }

    fun setText(textId: Int){
        bodyView.setText(textId)
    }
}