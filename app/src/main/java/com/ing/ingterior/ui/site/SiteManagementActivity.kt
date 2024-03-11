package com.ing.ingterior.ui.site

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.ing.ingterior.R
import com.ing.ui.button.VisualDotLineButton
import com.ing.ui.text.label.LabelView
import kotlin.math.min

class SiteManagementActivity : AppCompatActivity() {

    private lateinit var lbTitle: LabelView
    private lateinit var tabLayout: TabLayout
    private lateinit var vdbAddPoint: VisualDotLineButton
    private lateinit var fabAddPoint: FloatingActionButton
    private lateinit var frameImageLayout: FrameLayout
    private lateinit var bluePrintView: PhotoView
    private lateinit var markImageView: ImageView
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    private var tempX = 0f
    private var tempY = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_management)

        frameImageLayout = findViewById(R.id.frame_site_management_image_layout)
        bluePrintView = findViewById(R.id.photo_site_management_blueprint)
        lbTitle = findViewById(R.id.lb_site_management_title)
        tabLayout = findViewById(R.id.tab_site_management_layout)
        fabAddPoint = findViewById(R.id.fab_site_management_add)
        vdbAddPoint = findViewById(R.id.vdb_site_management_add_point)
        vdbAddPoint.setOnClickListener {
            lbTitle.text = "하자 추가"
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_site_management_content, SiteInsertDefectsFragment())
            transaction.commit()
            vdbAddPoint.isVisible = false
            fabAddPoint.isVisible = false
            tabLayout.isVisible = false
        }

        // 동적으로 마크 생성 및 추가
        markImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.mark_size), resources.getDimensionPixelSize(R.dimen.mark_size)) // 마크의 크기 설정
            setImageResource(R.drawable.ic_mark) // 마크 이미지 설정
            x = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 X 위치 설정
            y = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 Y 위치 설정
            lastTouchX = x
            lastTouchY = y
        }
        frameImageLayout.addView(markImageView)

        markImageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    tempX = event.x
                    tempY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    // 드래그 이동 로직
                    val dx: Float = event.x - tempX
                    val dy: Float = event.y - tempY
                    view.x += dx
                    view.y += dy
                    lastTouchX = view.x
                    lastTouchY = view.y
                    Log.d("TestActivity", "onCreate: markImageView x=${markImageView.x}, markImageView y=${markImageView.y}")
                }
            }
            true
        }

//        bluePrintView.setOnPhotoTapListener { view, x, y ->
//            // x와 y는 탭된 위치의 상대적인 비율을 나타냅니다. (0 ~ 1 사이의 값)
//            // 예를 들어, 이미지의 중앙을 탭하면 x와 y는 대략 0.5, 0.5가 됩니다.
//
//            // 여기에서 마커와 관련된 로직을 구현할 수 있습니다.
//            // 예: 마커가 있는 영역을 탭했는지 확인하고, 해당하는 동작을 수행
//            Log.d("TestActivity", "setOnPhotoTapListener: x=${x}, y=${y}")
//            val rect = bluePrintView.attacher.displayRect
//
//            // 상대적인 위치(x, y)를 실제 이미지 상의 절대 위치로 변환
//            val absoluteX = rect.left + x * rect.width()
//            val absoluteY = rect.top + y * rect.height()
//
//            // 마커 뷰의 위치를 업데이트합니다.
//            // 마커의 크기를 고려하여 위치 조정이 필요할 수 있습니다.
//            lastTouchX = absoluteX - (markImageView.width / 2)
//            lastTouchY = absoluteY - (markImageView.height / 2)
//
//            val matrix = Matrix()
//            bluePrintView.attacher.getSuppMatrix(matrix)
//            val tempPoint = floatArrayOf(lastTouchX, lastTouchY)
//
//            markImageView.x = tempPoint[0]
//            markImageView.y = tempPoint[1]
//
//            lastTouchX = markImageView.x
//            lastTouchY = markImageView.y
//        }

        bluePrintView.setOnMatrixChangeListener {
            markImageView.scaleX = bluePrintView.scale
            markImageView.scaleY = bluePrintView.scale

            // PhotoView의 변환 행렬을 가져옴
            val matrix = Matrix()
            bluePrintView.attacher.getSuppMatrix(matrix)

            // 초기 위치를 변환 행렬에 적용하여 실제 위치를 계산
            val tempPoint = floatArrayOf(lastTouchX, lastTouchY)
            Log.d("TestActivity", "onCreate: transformedPoint=${tempPoint.contentToString()}")
            matrix.mapPoints(tempPoint)

            // 이미지뷰의 위치 조정
            markImageView.x = tempPoint[0]
            markImageView.y = tempPoint[1]
            Log.d("TestActivity", "onCreate: markImageView x=${markImageView.x}, markImageView y=${markImageView.y}")
        }
    }

    override fun onBackPressed() {
        if(!vdbAddPoint.isVisible) {
           vdbAddPoint.isVisible = true
            fabAddPoint.isVisible = true
            tabLayout.isVisible = true
            lbTitle.text = ""
        }
        else super.onBackPressed()
    }
}