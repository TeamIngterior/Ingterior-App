package com.ing.ui.text.edit

import android.content.Context
import android.graphics.Typeface
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.ing.ui.Config
import com.ing.ui.Config.CAPTION2_FONT_SIZE
import com.ing.ui.R

class InputTextLayout : ConstraintLayout {

    private val inputTextView by lazy { InputTextView(context) }
    private val supportTextView by lazy { TextView(context) }
    private var spacing = 0
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        // inputTextView 설정
        inputTextView.id = generateViewId()
        val inputTextViewParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        inputTextView.layoutParams = inputTextViewParams

        addView(inputTextView)

        // supportTextView 설정
        supportTextView.id = generateViewId()
        val supportTextViewParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        supportTextView.textSize = CAPTION2_FONT_SIZE
        val font: Typeface = resources.getFont(Config.MEDIUM)
        supportTextView.typeface = font
        supportTextView.layoutParams = supportTextViewParams
        supportTextView.isVisible = false
        addView(supportTextView)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputTextLayout, defStyleAttr, 0)
            val hint = typedArray.getString(R.styleable.InputTextLayout_inputHint)
            inputTextView.hint = hint

            val text = typedArray.getString(R.styleable.InputTextLayout_inputText)
            inputTextView.setText(text)

            val textColor = typedArray.getColor(R.styleable.InputTextLayout_inputTextColor, ContextCompat.getColor(context, R.color.text_color_06))
            inputTextView.setTextColor(textColor)

            val inputMax = typedArray.getInteger(R.styleable.InputTextLayout_inputMax, 100)
            val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(inputMax))
            inputTextView.filters = filters

            val horizontalPadding = typedArray.getDimensionPixelSize(R.styleable.InputTextLayout_inputPaddingVertical, context.resources.getDimensionPixelSize(R.dimen.horizontal_input_text_padding))
            val verticalPadding = typedArray.getDimensionPixelSize(R.styleable.InputTextLayout_inputPaddingHorizontal, context.resources.getDimensionPixelSize(R.dimen.vertical_input_text_padding))
            inputTextView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

            val singleLine = typedArray.getBoolean(R.styleable.InputTextLayout_inputSingleLine, false)
            inputTextView.isSingleLine = singleLine

            val isEnable = typedArray.getBoolean(R.styleable.InputTextLayout_inputEnable, true)
            inputTextView.isEnabled = isEnable

            if(!singleLine){
                spacing = typedArray.getDimensionPixelSize(R.styleable.InputTextLayout_inputSupportSpacing, 0)
                inputTextView.setLineSpacing(spacing.toFloat(), 1f)

                val inputMaxLine = typedArray.getInteger(R.styleable.InputTextLayout_inputMaxLine, 10)
                inputTextView.maxLines = inputMaxLine
            }

            val supportText = typedArray.getString(R.styleable.InputTextLayout_inputSupportText) ?: ""
            supportTextView.text = supportText
            supportTextView.isVisible = supportText.isNotEmpty()

            val supportTextColor = typedArray.getColor(R.styleable.InputTextLayout_inputSupportColor, ContextCompat.getColor(context, R.color.text_color_04))
            supportTextView.setTextColor(supportTextColor)

            typedArray.recycle()
        }

        // 제약 조건 설정
        applyConstraints()
    }

    private fun applyConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        // inputTextView 제약 조건
        constraintSet.connect(inputTextView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(inputTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(inputTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        // supportTextView 제약 조건
        val supportStartMargin = context.resources.getDimensionPixelSize(R.dimen.input_text_support_start_margin)
        constraintSet.connect(supportTextView.id, ConstraintSet.TOP, inputTextView.id, ConstraintSet.BOTTOM, spacing)
        constraintSet.connect(supportTextView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, supportStartMargin)
        constraintSet.connect(supportTextView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        constraintSet.applyTo(this)
    }

    fun setHint(hint: String){
        supportTextView.text = hint
    }

    fun addTextChangedListener(watcher: TextWatcher?) {
        inputTextView.addTextChangedListener(watcher)
    }

    fun length(): Int {
        return inputTextView.length()
    }

    fun getText(): String {
        return inputTextView.text.toString()
    }

    fun setText(text: String) {
        inputTextView.setText(text)
    }

    fun getTextView() = inputTextView
}