package com.ing.ingterior.ui

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ing.ingterior.R
import com.ing.ingterior.cameraPermission
import com.ing.ingterior.util.ImageUtils
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
                        ImageUtils.rotateImageIfRequired(imageBitmap, ImageUtils.imageFile!!)
                    }

                    ivTest.setImageBitmap(rotatedBitmap)
                }
            }
            else{
                Toast.makeText(this, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val scaleGestureDetector: ScaleGestureDetector by lazy { ScaleGestureDetector(this, ScaleListener()) }
    private val matrix = Matrix()
    private var scale = 1f
    private val getPictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val photoUri = result.data?.data
            if(photoUri != null) {
                ivTest.setImageURI(photoUri)
            }
            else{
                Toast.makeText(this, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val ivTest: ImageView by lazy { findViewById(R.id.iv_blue_print) }
    private val btnGallery : Button by lazy { findViewById(R.id.btn_blue_print_from_gallery) }
    private val btnCamera : Button by lazy { findViewById(R.id.btn_blue_print_from_camera) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


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

        ivTest.setOnTouchListener(OnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)

            // 이미지뷰 내에서의 터치 좌표 추출
            val point = floatArrayOf(event.x, event.y)
            matrix.invert(Matrix())
            matrix.mapPoints(point)

            // 로그에 좌표 출력
            Log.d("JYPARK", "실제 좌표: x=" + point[0] + ", y=" + point[1])

            // 여기서 원하는 추가 작업 수행 가능
            true
        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionUtils.REQUEST_CAMERA_PERMISSION) {
            val granted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if(granted) takePictureResult.launch(ImageUtils.getTakePictureIntent(this))
            else Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // 확대/축소 비율 갱신
            scale *= detector.scaleFactor
            // 최소 및 최대 확대 비율 제한
            scale = Math.max(0.1f, Math.min(scale, 5.0f))

            // 이미지뷰에 변환 적용
            matrix.setScale(scale, scale)
            ivTest.imageMatrix = matrix
            return true
        }
    }
}