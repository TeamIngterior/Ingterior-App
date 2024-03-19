package com.ing.ui.check

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ing.ui.Config
import com.ing.ui.R

class VisualCalendarTextView : VisualCheckBox {

    companion object{
        const val SUNDAY = 0
        const val SATURDAY = 6

    }

    private val backgroundContain = R.drawable.bg_calendar_cotain
    private val backgroundDefault = R.drawable.bg_calendar_default
    private val backgroundEnd = R.drawable.bg_calendar_end
    private val backgroundStart = R.drawable.bg_calendar_start

    private val startEndTextColor = R.color.text_color_01
    private val defaultTextColor = R.color.text_color_06

    private val textView by lazy {
        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            gravity = Gravity.CENTER_VERTICAL
            // 초기 텍스트 색상 설정
            textSize = Config.LABEL_FONT_SIZE
            val font: Typeface = resources.getFont(Config.MEDIUM)
            typeface = font
            setTextColor(ContextCompat.getColor(context, defaultTextColor))
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
            textView.text = text
            typedArray.recycle()
        }

        background = ContextCompat.getDrawable(context, backgroundDefault)

        addView(textView)
    }

    fun setStart(){
        background = ContextCompat.getDrawable(context, backgroundStart)
        textView.setTextColor(ContextCompat.getColor(context, startEndTextColor))
    }

    fun setEnd() {
        background = ContextCompat.getDrawable(context, backgroundEnd)
        textView.setTextColor(ContextCompat.getColor(context, startEndTextColor))

    }

    fun setDefault(week: Int) {
        background = ContextCompat.getDrawable(context, backgroundDefault)
        textView.setTextColor(ContextCompat.getColor(context, defaultTextColor))
        when (week) {
            SUNDAY -> {
                textView.setTextColor(ContextCompat.getColor(context, R.color.error_color))
            }
            SATURDAY -> {
                textView.setTextColor(ContextCompat.getColor(context, R.color.blue_color04))
            }
            else -> {
                textView.setTextColor(ContextCompat.getColor(context, R.color.text_color_06))
            }
        }
    }
    fun setContains() {
        background = ContextCompat.getDrawable(context, backgroundContain)
        textView.setTextColor(ContextCompat.getColor(context, defaultTextColor))
    }

    fun setText(text: String){
        textView.text = text
    }

    fun setTextColor(color: Int) {
        textView.setTextColor(color)
    }
}