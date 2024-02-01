package com.ing.ingterior.ui.start

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.ing.ingterior.R
import com.ing.ingterior.cameraPermission
import com.ing.ingterior.ui.IngTeriorViewModelFactory
import com.ing.ingterior.util.AnimationUtils
import com.ing.ingterior.util.ImageUtils
import com.ing.ingterior.util.PermissionUtils
import com.ing.ingterior.util.PermissionUtils.REQUEST_CAMERA_PERMISSION


class StartActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "StartActivity"
    }

    private val viewModel : StartViewModel by lazy { ViewModelProvider(this, IngTeriorViewModelFactory())[StartViewModel::class.java] }
    private val ivLogo: ImageView by lazy { findViewById(R.id.iv_ss_logo) }
    private val lineSplashScreenLayout: LinearLayout by lazy { findViewById(R.id.line_ss_layout) }
    private val btnGotoEstimation: Button by lazy { findViewById(R.id.btn_start_go_to_estimation) }
    private val btnGotoManaging: Button by lazy { findViewById(R.id.btn_start_go_to_managing) }

    private val ivTest: ImageView by lazy { findViewById(R.id.iv_test_picture) }

    private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val resultIntent = result.data
            if(resultIntent != null && resultIntent.hasExtra("data")) {
                val imageBitmap = resultIntent.extras?.get("data") as Bitmap
                ivTest.setImageBitmap(imageBitmap)
            }
            else{
                Toast.makeText(this@StartActivity, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getPictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val photoUri = result.data?.data
            if(photoUri != null) {
                ivTest.setImageURI(photoUri)
            }
            else{
                Toast.makeText(this@StartActivity, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        if(viewModel.isFirst) {
            AnimationUtils.fadeInAndOut(ivLogo, 500, object : AnimationUtils.AnimationListener {
                override fun start() {
                    lineSplashScreenLayout.isVisible = true
                }
                override fun end() {
                    lineSplashScreenLayout.isVisible = false
                }
            })
        }

        btnGotoEstimation.setOnClickListener {
            getPictureResult.launch(ImageUtils.getPictureIntent())
//            Factory.get().getMove().moveSimpleEstimationActivity(this@StartActivity)
        }
        btnGotoManaging.setOnClickListener {
            if(PermissionUtils.hasCameraPermission(this)){
                takePictureResult.launch(ImageUtils.getTakePictureIntent())
            }
            else {
                requestPermissions(arrayOf(cameraPermission), REQUEST_CAMERA_PERMISSION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            val granted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if(granted) takePictureResult.launch(ImageUtils.getTakePictureIntent())
            else Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}