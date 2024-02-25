package com.ing.ui.check

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.ing.ui.R

class VisualImageCheckBox: VisualCheckBox {
    private var isChecked = false

    private var unCheckIcon = R.drawable.ic_uncheck_box
    private var checkIcon = R.drawable.ic_check_box

    private val imageView by lazy {
        ImageView(context).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
                it.gravity = Gravity.CENTER_VERTICAL
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
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

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VisualCheckBox, defStyleAttr, 0)
            isChecked = typedArray.getBoolean(R.styleable.VisualCheckBox_isChecked, false)
            val checkboxSrc = (typedArray.getResourceId(R.styleable.VisualCheckBox_checkBoxSrc, 0))
            if(checkboxSrc > 0) checkIcon = checkboxSrc

            val uncheckboxSrc = (typedArray.getResourceId(R.styleable.VisualCheckBox_uncheckBoxSrc, 0))
            if(uncheckboxSrc > 0) unCheckIcon = uncheckboxSrc

            typedArray.recycle()
        }

        init()
    }

    private fun init() {
        addView(imageView)

        updateView()

    }

    fun toggleCheck() {
        isChecked = !isChecked
        updateView()
    }

    private fun updateView() {
        if (isChecked) {
            imageView.setImageResource(checkIcon)
        } else {
            imageView.setImageResource(unCheckIcon)
        }
    }

    // Getter and Setter for isChecked to be accessible from outside
    fun isChecked(): Boolean = isChecked

    fun setChecked(checked: Boolean) {
        isChecked = checked
        updateView()
    }
}