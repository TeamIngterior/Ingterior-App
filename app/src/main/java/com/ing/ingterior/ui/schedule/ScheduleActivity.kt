package com.ing.ingterior.ui.schedule

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ing.ingterior.R
import com.ing.ingterior.util.ImageUtils
import com.ing.ingterior.util.PermissionUtils

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
    }

    private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val resultIntent = result.data
            if(resultIntent != null && resultIntent.hasExtra("data")) {
                val imageBitmap = resultIntent.extras?.get("data") as Bitmap
//                ivTest.setImageBitmap(imageBitmap)
            }
            else{
                Toast.makeText(this@ScheduleActivity, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getPictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val photoUri = result.data?.data
            if(photoUri != null) {
//                ivTest.setImageURI(photoUri)
            }
            else{
                Toast.makeText(this@ScheduleActivity, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionUtils.REQUEST_CAMERA_PERMISSION) {
            val granted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if(granted) takePictureResult.launch(ImageUtils.getTakePictureIntent())
            else Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}