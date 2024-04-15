package com.ing.ui.check

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ing.ui.Config
import com.ing.ui.R

class VisualCalendarTextView2 : VisualCheckBox {

    companion object{
        const val SUNDAY = 0
        const val SATURDAY = 6

    }

    private val backgroundDefault = R.drawable.bg_calendar_default
    private val backgroundSelect = R.drawable.bg_calendar_select

    private val startEndTextColor = R.color.text_color_01
    private val defaultTextColor = R.color.text_color_06

    private val textView by lazy {
        TextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            gravity = Gravity.CENTER_VERTICAL
            // 초기 텍스트 색상 설정
            textSize = Config.CAPTION1_FONT_SIZE
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

        setPadding(resources.getDimensionPixelSize(R.dimen.calendar_item_horizontal_margin), resources.getDimensionPixelSize(R.dimen.calendar_item_vertical_margin),
            resources.getDimensionPixelSize(R.dimen.calendar_item_horizontal_margin),resources.getDimensionPixelSize(R.dimen.calendar_item_vertical_margin))

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

    fun setSelect(){
        background = ContextCompat.getDrawable(context, backgroundSelect)
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

    fun setDisable() {
        background = ContextCompat.getDrawable(context, backgroundDefault)
        textView.setTextColor(ContextCompat.getColor(context, R.color.text_color_03))

    }

    fun setText(text: String){
        textView.text = text
    }

    fun setTextColor(color: Int) {
        textView.setTextColor(color)
    }
}