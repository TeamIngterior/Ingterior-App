package com.ing.ingterior.util

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object ImageUtils {

    var imageFile: File? = null

    private fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = File(context.filesDir, "Picture")
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) throw IOException("Not Found")
        }
        imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        return imageFile!!
    }

    fun rotateImageIfRequired(img: Bitmap, imageFile: File): Bitmap {
        imageFile.let { file ->
            val ei = ExifInterface(file.absolutePath)
            return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270f)
                else -> img
            }
        }
    }

    private fun rotateImage(img: Bitmap, degree: Float): Bitmap {
        Log.d("ImageUtils", "rotateImage: degree=$degree")
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun getPictureIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        return Intent.createChooser(intent, "Get Album")
    }

    fun getTakePictureIntent(context: Context): Intent? {
        val photoFile: File = try { createImageFile(context) } catch (ex: IOException) { null } ?: return null
        val photoURI: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        intent.clipData = ClipData.newRawUri("", photoURI)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return intent
    }
}