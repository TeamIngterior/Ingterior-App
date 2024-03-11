package com.ing.ingterior.ui

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView

class CustomImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    private var gestureDetector: GestureDetector
    private var scaleGestureDetector: ScaleGestureDetector
    private var currentScale = 1f // 이미지의 현재 스케일
    private val maxScale = 3f // 최대 스케일 값
    private val minScale = 1f // 최소 스케일 값
    init {
        scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                var scaleFactor = detector.scaleFactor

                // 임시 스케일 값 계산 (현재 스케일에 변화량 적용)
                val tempScale = currentScale * scaleFactor

                // 최소 및 최대 스케일 값에 따른 조정
                scaleFactor = when {
                    tempScale > maxScale -> maxScale / currentScale
                    tempScale < minScale -> minScale / currentScale
                    else -> scaleFactor
                }

                // 스케일 적용
                currentScale *= scaleFactor
                scaleX *= scaleFactor
                scaleY *= scaleFactor

                return true
            }
        })

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                // 현재 스케일(줌 레벨) 검사
                if (currentScale <= 1f) {
                    // 스케일이 1 이하인 경우, 즉 충분히 줌인되지 않았으면 스크롤하지 않습니다.
                    return false
                }

                // 스크롤할 때의 이동 범위를 계산하고, 이미지가 뷰의 경계를 넘지 않도록 조정합니다.
                // 이를 위해, 이미지의 스케일링된 크기와 ImageView의 크기를 고려합니다.

                // 이미지의 스케일링된 크기 계산
                val scaledWidth = drawable.intrinsicWidth * currentScale
                val scaledHeight = drawable.intrinsicHeight * currentScale

                // 계산된 새 위치에 대한 제한을 적용
                val newTranslationX = (translationX - distanceX).coerceIn(-(scaledWidth - width) / 2, (scaledWidth - width) / 2)
                val newTranslationY = (translationY - distanceY).coerceIn(-(scaledHeight - height) / 2, (scaledHeight - height) / 2)

                translationX = newTranslationX
                translationY = newTranslationY

                return true
            }
        })
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

}
