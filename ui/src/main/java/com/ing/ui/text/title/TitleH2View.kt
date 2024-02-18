package com.ing.ui.text.title

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.ing.ui.Config.H2_FONT_SIZE
import com.ing.ui.Config.MEDIUM
import com.ing.ui.R
import com.ing.ui.text.BaseTextView

class TitleH2View : BaseTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        textSize = H2_FONT_SIZE
        val font: Typeface = resources.getFont(MEDIUM)
        typeface = font

        // attrs가 null이 아닌 경우에만 속성 처리
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseTextView, defStyleAttr, 0)
            // 여기서 R.styleable.LabelView_textColor 같은 식으로 사용자 정의 속성을 읽어올 수 있습니다.
            // XML에서 textColor를 설정하지 않은 경우 기본 색상 사용
            val textColor = typedArray.getColor(R.styleable.BaseTextView_customTextColor, ContextCompat.getColor(context, R.color.text_color_06))
            setTextColor(textColor)
            typedArray.recycle()
        }
    }
}