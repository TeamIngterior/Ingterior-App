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

class TestActivity : AppCompatActivity() {

    private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            Log.d("JYPARK", "imageFile is null? ${ImageUtils.imageFile==null}")
            if(ImageUtils.imageFile!=null) {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("JYPARK", "absolutePath=${ImageUtils.imageFile!!.absolutePath}")
                    val imageBitmap:Bitmap = withContext(Dispatchers.IO){
                        BitmapFactory.decodeFile(ImageUtils.imageFile!!.absolutePath)
                    }

                    val rotatedBitmap:Bitmap = withContext(Dispatchers.IO){
                        rotateImageIfRequired(imageBitmap, ImageUtils.imageFile!!)
                    }

                    ivImage.setImageBitmap(rotatedBitmap)
                }
            }
            else{
                Toast.makeText(this, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getPictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val photoUri = result.data?.data
            if(photoUri != null) {
                ivImage.setImageURI(photoUri)
            }
            else{
                Toast.makeText(this, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    private lateinit var ivImage: PhotoView
    private lateinit var markImageView: ImageView
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f


    private val btnGallery : Button by lazy { findViewById(R.id.btn_blue_print_from_gallery) }
    private val btnCamera : Button by lazy { findViewById(R.id.btn_blue_print_from_camera) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        ivImage = findViewById(R.id.iv_management_image)
        btnGallery.setOnClickListener {
            getPictureResult.launch(ImageUtils.getPictureIntent())
        }

        btnCamera.setOnClickListener {
            if(PermissionUtils.hasCameraPermission(this)) {
                takePictureResult.launch(ImageUtils.getTakePictureIntent(this))
            }
            else {
                requestPermissions(arrayOf(cameraPermission) , PermissionUtils.REQUEST_CAMERA_PERMISSION)
            }
        }

        val frameLayout: FrameLayout = findViewById(R.id.frame_site_management_image_layout)

        // 동적으로 마크 생성 및 추가
        markImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(resources.getDimensionPixelSize(R.dimen.mark_size), resources.getDimensionPixelSize(R.dimen.mark_size)) // 마크의 크기 설정
            setImageResource(R.drawable.ic_mark) // 마크 이미지 설정
            x = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 X 위치 설정
            y = resources.getDimensionPixelSize(R.dimen.popup_item_width).toFloat() // 초기 Y 위치 설정
        }
        frameLayout.addView(markImageView)

        markImageView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    // 드래그 이동 로직
                    val dx: Float = event.x - lastTouchX
                    val dy: Float = event.y - lastTouchY
                    view.x += dx
                    view.y += dy
                }
            }
            true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionUtils.REQUEST_CAMERA_PERMISSION) {
            val granted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if(granted) takePictureResult.launch(ImageUtils.getTakePictureIntent(this))
            else Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}