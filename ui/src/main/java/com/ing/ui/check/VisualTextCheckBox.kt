package com.ing.ui.check

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.ing.ui.Config
import com.ing.ui.R

class VisualTextCheckBox : VisualCheckBox {

    private var isChecked = false

    private val unCheckTextColor = R.color.text_color_06
    private var checkBackground = R.drawable.bg_calendar_start
    private val checkTextColor = R.color.text_color_01

    private val textView by lazy {
        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            gravity = Gravity.CENTER_VERTICAL
            // 초기 텍스트 색상 설정
            textSize = Config.LABEL_FONT_SIZE
            val font: Typeface = resources.getFont(Config.MEDIUM)
            typeface = font
            setTextColor(ContextCompat.getColor(context, unCheckTextColor))
        }
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VisualCheckBox, defStyleAttr, 0)
            val text = typedArray.getString(R.styleable.VisualCheckBox_checkBoxText)

            isChecked = typedArray.getBoolean(R.styleable.VisualCheckBox_isChecked, false)
            textView.text = text
            typedArray.recycle()
        }

        init()
    }

    private fun init() {
        addView(textView)

        updateView()

    }

    fun toggleCheck() {
        isChecked = !isChecked
        updateView()
    }

    private fun updateView() {
        if (isChecked) {
            background = ContextCompat.getDrawable(context, checkBackground)
            textView.setTextColor(ContextCompat.getColor(context, checkTextColor))
        } else {
            setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            textView.setTextColor(ContextCompat.getColor(context, unCheckTextColor))
        }
    }

    // Getter and Setter for isChecked to be accessible from outside
    fun isChecked(): Boolean = isChecked

    fun setChecked(checked: Boolean) {
        isChecked = checked
        updateView()
    }

    fun setText(text: String){
        textView.text = text
    }

    fun setTextColor(color: Int) {
        textView.setTextColor(color)
    }
}