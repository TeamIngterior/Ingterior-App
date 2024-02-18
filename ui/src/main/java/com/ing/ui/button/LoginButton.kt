package com.ing.ui.button

import android.content.Context
import android.text.InputFilter
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.ing.ui.R
import com.ing.ui.text.label.LabelView

class LoginButton : LinearLayoutCompat {

    private var iconRec: Int = R.drawable.ic_logo_kakao_24
    private var backgroundRec: Int = R.drawable.ic_logo_naver_24
    private var textColor: Int = R.color.text_color_06
    private var actionString: String = ""

    private val imageView: ImageView by lazy {
        ImageView(context).apply {
            val size = context.resources.getDimensionPixelSize(R.dimen.login_icon_size)
            layoutParams = LayoutParams(size, size).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
            setImageDrawable(ContextCompat.getDrawable(context, iconRec))
        }
    }

    private val labelView: LabelView by lazy {
        LabelView(context).apply {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                val startMargin = context.resources.getDimensionPixelSize(R.dimen.login_label_start_margin)
                setMargins(startMargin,0,0,0)
                gravity = Gravity.CENTER_VERTICAL
            }
            text = actionString
            setTextColor(ContextCompat.getColor(context, textColor))
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

        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        addView(imageView)
        addView(labelView)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoginButton, defStyleAttr, 0)
            when(typedArray.getString(R.styleable.InputTextLayout_inputHint) ?: "") {
                "kakao" -> {
                    iconRec = R.drawable.ic_logo_kakao_24
                    backgroundRec = R.drawable.bg_kakaotalk_button
                    actionString = context.getString(R.string.action_login, context.getString(R.string.kakaotalk))
                    textColor = R.color.text_color_06
                }
                "naver" -> {
                    iconRec = R.drawable.ic_logo_naver_24
                    backgroundRec = R.drawable.bg_naver_button
                    actionString = context.getString(R.string.action_login, context.getString(R.string.naver))
                    textColor = R.color.text_color_01
                }
                "google" -> {
                    iconRec = R.drawable.ic_logo_google_24
                    backgroundRec = R.drawable.bg_default_button
                    actionString = context.getString(R.string.action_login, context.getString(R.string.google))
                    textColor = R.color.text_color_06
                }
                "instagram" -> {
                    iconRec = R.drawable.ic_logo_instagram_24
                    backgroundRec = R.drawable.bg_default_button
                    actionString = context.getString(R.string.action_login, context.getString(R.string.instagram))
                    textColor = R.color.text_color_06
                }
                else -> {
                    iconRec = R.drawable.ic_logo_instagram_24
                    backgroundRec = R.drawable.bg_default_button
                    actionString = context.getString(R.string.action_login, context.getString(R.string.instagram))
                    textColor = R.color.text_color_06
                }
            }
            typedArray.recycle()
        }

        background = ContextCompat.getDrawable(context, backgroundRec)
        imageView.setImageDrawable(ContextCompat.getDrawable(context, iconRec))
        labelView.setTextColor(ContextCompat.getColor(context, textColor))
        labelView.text = actionString
    }
}