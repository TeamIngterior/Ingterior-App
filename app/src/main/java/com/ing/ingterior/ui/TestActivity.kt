package com.ing.ingterior.ui

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import com.ing.ingterior.R
import com.ing.ingterior.cameraPermission
import com.ing.ingterior.util.ImageUtils
import com.ing.ingterior.util.ImageUtils.GraphicUtils.rotateImageIfRequired
import com.ing.ingterior.util.PermissionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

class TestActivity : AppCompatActivity() {

    private lateinit var ivImage: PhotoView
    private lateinit var markImageView: ImageView
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    private var tempX = 0f
    private var tempY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        ivImage = findViewById(R.id.iv_management_image)

        val frameLayout: FrameLayout = findViewById(R.id.frame_site_management_image_layout)

        // 동적으로 마크 생성 및 추가
        markImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.mark_size), resources.getDimensionPixelSize(R.dimen.mark_size)) // 마크의 크기 설정
            setImageResource(R.drawable.ic_mark) // 마크 이미지 설정
            x = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 X 위치 설정
            y = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 Y 위치 설정
            lastTouchX = x
            lastTouchY = y
        }
        frameLayout.addView(markImageView)

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

        ivImage.setOnPhotoTapListener { view, x, y ->
            // x와 y는 탭된 위치의 상대적인 비율을 나타냅니다. (0 ~ 1 사이의 값)
            // 예를 들어, 이미지의 중앙을 탭하면 x와 y는 대략 0.5, 0.5가 됩니다.

            // 여기에서 마커와 관련된 로직을 구현할 수 있습니다.
            // 예: 마커가 있는 영역을 탭했는지 확인하고, 해당하는 동작을 수행
            Log.d("TestActivity", "setOnPhotoTapListener: x=${x}, y=${y}")
            val rect = ivImage.attacher.displayRect

            // 상대적인 위치(x, y)를 실제 이미지 상의 절대 위치로 변환
            val absoluteX = rect.left + x * rect.width()
            val absoluteY = rect.top + y * rect.height()

            // 마커 뷰의 위치를 업데이트합니다.
            // 마커의 크기를 고려하여 위치 조정이 필요할 수 있습니다.
            lastTouchX = absoluteX - (markImageView.width / 2)
            lastTouchY = absoluteY - (markImageView.height / 2)

            val matrix = Matrix()
            ivImage.attacher.getSuppMatrix(matrix)
            val tempPoint = floatArrayOf(lastTouchX, lastTouchY)


            markImageView.x = tempPoint[0]
            markImageView.y = tempPoint[1]

            lastTouchX = absoluteX
            lastTouchY = absoluteY
        }

        ivImage.setOnMatrixChangeListener {
            // PhotoView의 변환 행렬을 가져옴

            val matrix = Matrix()
            ivImage.attacher.getSuppMatrix(matrix)

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

    /*
    300x300 크기의 이미지를 중앙에서 2배로 줌인 했을 때, 보여지는 사각형의 시작 위치와 종료 위치의 x, y 값의 행렬을 계산하겠습니다.

줌인 전에 전체 이미지(300x300)가 보여졌다면, 2배로 줌인한 후에는 이미지의 크기가 같게 보이는 화면 영역이 1/2로 축소됩니다. 이는 이미지의 중앙 부분만 확대해서 보이게 됨을 의미합니다.

    원본 이미지 크기: 300x300
    줌인 배율: 2배
    줌인 후 보여지는 이미지의 크기: 원본의 1/2

따라서, 확대된 이미지에서 중앙에 보여질 부분의 크기는 원본의 절반인 150x150 크기가 됩니다. 이미지가 정중앙에서 확대되므로, 보여지는 사각형의 시작 위치와 종료 위치는 다음과 같습니다:

    시작 위치: 이미지의 가로와 세로에서 각각 1/4 지점에 해당하는 (75, 75)
    종료 위치: 이미지의 가로와 세로에서 각각 3/4 지점에 해당하는 (225, 225)

즉, 2배로 줌인한 상태에서 화면에 보여지는 이미지의 영역은 좌상단 모서리가 (75, 75)이고, 우하단 모서리가 (225, 225)인 사각형 영역입니다. 이 영역은 이미지의 정중앙 부분을 확대해서 보여주는 결과를 나타냅니다.

     */

}