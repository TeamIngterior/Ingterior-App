package com.ing.ingterior.ui.site

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ing.ingterior.R
import com.ing.ingterior.cameraPermission
import com.ing.ingterior.util.ImageUtils
import com.ing.ingterior.util.ImageUtils.getPictureIntent
import com.ing.ingterior.util.ImageUtils.getTakePictureIntent
import com.ing.ingterior.util.PermissionUtils

class BluePrintManagementFragment : Fragment() {


    private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            val resultIntent = result.data
            if(resultIntent != null && resultIntent.hasExtra("data")) {
                val imageBitmap = resultIntent.extras?.get("data") as Bitmap
//                ivTest.setImageBitmap(imageBitmap)
            }
            else{
                Toast.makeText(requireContext(), "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getPictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == AppCompatActivity.RESULT_OK) {
            val photoUri = result.data?.data
            if(photoUri != null) {
//                ivTest.setImageURI(photoUri)
            }
            else{
                Toast.makeText(requireContext(), "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val btnGallery : Button by lazy { requireView().findViewById(R.id.btn_blue_print_from_gallery) }
    private val btnCamera : Button by lazy { requireView().findViewById(R.id.btn_blue_print_from_camera) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blue_print_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnGallery.setOnClickListener {
            getPictureResult.launch(getPictureIntent())
        }

        btnCamera.setOnClickListener {
            if(PermissionUtils.hasCameraPermission(requireContext())) {
                takePictureResult.launch(getTakePictureIntent())
            }
            else {
                requireActivity().requestPermissions(arrayOf(cameraPermission) , PermissionUtils.REQUEST_CAMERA_PERMISSION)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionUtils.REQUEST_CAMERA_PERMISSION) {
            val granted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if(granted) takePictureResult.launch(ImageUtils.getTakePictureIntent())
            else Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}