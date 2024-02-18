package com.ing.ingterior.util

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
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

    object GraphicUtils{

        // 파일의 Orientation 과 이미지를 동기화
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

        // Uri에서 이미지 추출
        fun loadBitmapFromUri(uri: Uri, context: Context): Bitmap? {
            return try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                null
            }
        }

        // 이미지 회전
        fun rotateImage(source: Bitmap, angle: Float): Bitmap {
            val matrix = Matrix().apply { postRotate(angle) }
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }


        // 이미지 좌우 반전
        fun flipImageHorizontally(originalBitmap: Bitmap, horizontalInversion: Boolean): Bitmap {
            var sx = 1f
            var sy = 1f
            if(horizontalInversion) {
                sx = -1f
                sy = 1f
            }
            // x축을 반대로 곱해서 덮어씌움
            // y축은 그대로 곱해서 크기 유지
            // 또한, Y축의 중심을 기준으로 회전해야 하므로, pivotY는 이미지 높이의 절반을 사용
            val matrix = Matrix().apply { postScale(sx, sy, originalBitmap.width / 2f, originalBitmap.height / 2f) }
            return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)
        }


        // 이미지 상하 반전
        fun flipImageVertically(originalBitmap: Bitmap, verticalInversion: Boolean): Bitmap {
            var sx = 1f
            var sy = 1f
            if(verticalInversion) {
                sx = 1f
                sy = -1f
            }
            // x축은 그대로 곱해서 크기 유지
            // y축을 반대로 곱해서 덮어씌움
            // 또한, Y축의 중심을 기준으로 회전해야 하므로, pivotY는 이미지 높이의 절반을 사용
            val matrix = Matrix().apply { postScale(sx, sy, originalBitmap.width / 2f, originalBitmap.height / 2f) }
            return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)
        }
    }
}