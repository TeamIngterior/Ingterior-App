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

class VisualButtonCheckBox : VisualCheckBox {

    private var isChecked = false

    private var unCheckIcon = R.drawable.ic_uncheck_box
    private val unCheckBackground = R.drawable.bg_ing_uncheck_box
    private val unCheckTextColor = R.color.text_color_04

    private var checkIcon = R.drawable.ic_check_box
    private val checkBackground = R.drawable.bg_ing_check_box
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

    private val imageView by lazy {
        ImageView(context).apply {
            layoutParams = LayoutParams(context.resources.getDimensionPixelSize(R.dimen.check_box_size), context.resources.getDimensionPixelSize(R.dimen.check_box_size)).also {
                    val marginEnd = context.resources.getDimensionPixelSize(R.dimen.check_box_spacing)
                    it.setMargins(0,0,marginEnd,0)
                    it.gravity = Gravity.CENTER_VERTICAL
                }
            // 초기 아이콘 설정
            setImageResource(unCheckIcon)
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
        addView(imageView)
        addView(textView)

        updateView()

    }

    fun toggleCheck() {
        isChecked = !isChecked
        updateView()
    }

    private fun updateView() {
        if (isChecked) {
            imageView.setImageResource(checkIcon)
            background = ContextCompat.getDrawable(context, checkBackground)
            textView.setTextColor(ContextCompat.getColor(context, checkTextColor))
        } else {
            imageView.setImageResource(unCheckIcon)
            background = ContextCompat.getDrawable(context, unCheckBackground)
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
}