package com.ing.ingterior.util

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object ImageUtils {
    private const val TAG = "ImageUtils"


    private const val IMAGE_SCALE_FACTOR = 0.75f
    private const val IMAGE_COMPRESSION_QUALITY = 95
    private const val MINIMUM_IMAGE_COMPRESSION_QUALITY = 50
    private const val NUMBER_OF_RESIZE_ATTEMPTS = 4

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
                context.contentResolver.openInputStream(uri)?.use { inputStream -> BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                null
            }
        }

        fun loadBitmapFromFile(path: String): Bitmap? {
            return try {
                val file = File(path)
                BitmapFactory.decodeStream(FileInputStream(file))
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

    /**
     * Resize and recompress the image such that it fits the given limits. The resulting byte
     * array contains an image in JPEG format, regardless of the original image's content type.
     * @param widthLimit The width limit, in pixels
     * @param heightLimit The height limit, in pixels
     * @param byteLimit The binary size limit, in bytes
     * @return A resized/recompressed version of this image, in JPEG format
     */
    @Synchronized
    fun getResizedImageData(
        width: Int, height: Int,
        widthLimit: Int, heightLimit: Int, byteLimit: Int, mimeType: String, uri: Uri, context: Context
    ): ByteArray? {
        // 사진 크기 자체가 제대로 되어 있지 않는 경우가 있다. 예를 들면 세로든 가로든 사진의 너비*높이가 4160*3120 으로 되어 있는데, 카메라 앱에서 저장할 때 크기를 제대로 저장하지 않는 것 같다.
        // 일단 Glide 에서 다시 크기에 맞춰 그려주기 때문에 문제가 발생하진 않지만 추후 문제가 발생하면 이쪽 다시 확인해 봐야 한다.
        var scaleFactor = 1f
        while (width * scaleFactor > widthLimit || height * scaleFactor > heightLimit) {
            scaleFactor *= IMAGE_SCALE_FACTOR
        }
        val orientation = getOrientation(context, uri)
        var input: InputStream? = null
        var os: ByteArrayOutputStream? = null
        val types = mimeType.split("/")
        val type = if(types.size > 1) types[1] else "jpeg"
        val compressFormat =
            if(type == "png") Bitmap.CompressFormat.PNG
            else Bitmap.CompressFormat.JPEG
        return try {
            var attempts = 1
            var sampleSize = 1
            val options = BitmapFactory.Options()
            var quality: Int = IMAGE_COMPRESSION_QUALITY
            var b: Bitmap? = null

            // In this loop, attempt to decode the stream with the best possible subsampling (we
            // start with 1, which means no subsampling - get the original content) without running
            // out of memory.
            do {
                input = context.contentResolver.openInputStream(uri)
                options.inSampleSize = sampleSize
                try {
                    b = BitmapFactory.decodeStream(input, null, options)
                    if (b == null) {
                        return null // Couldn't decode and it wasn't because of an exception,
                        // bail.
                    }
                }
                catch (e: OutOfMemoryError) {
                    Log.w(TAG, "getResizedImageData: img too large to decode (OutOfMemoryError), may try with larger sampleSize. Curr sampleSize=$sampleSize")
                    sampleSize *= 2 // works best as a power of two
                    attempts++
                    continue
                }
                finally {
                    if (input != null) {
                        try {
                            input.close()
                        } catch (e: IOException) {
                            Log.e(TAG, e.message, e)
                        }
                    }
                }
            } while (b == null && attempts < NUMBER_OF_RESIZE_ATTEMPTS)
            if (b == null) {
                Log.d(TAG, "getResizedImageData: gave up after too many attempts to resize")
                return null
            }
            var resultTooBig = true
            attempts = 1 // reset count for second loop
            // In this loop, we attempt to compress/resize the content to fit the given dimension
            // and file-size limits.
            do {
                try {
                    if (options.outWidth > widthLimit || options.outHeight > heightLimit ||
                        os != null && os.size() > byteLimit
                    ) {
                        // The decoder does not support the inSampleSize option.
                        // Scale the bitmap using Bitmap library.
                        val scaledWidth = (width * scaleFactor).toInt()
                        val scaledHeight = (height * scaleFactor).toInt()

                        b = Bitmap.createScaledBitmap(b!!, scaledWidth, scaledHeight, false)
                        if (b == null) {
                            Log.d(TAG, "getResizedImageData: Bitmap.createScaledBitmap returned NULL!")
                            return null
                        }
                    }

                    // Compress the image into a JPG. Start with MessageUtils.IMAGE_COMPRESSION_QUALITY.
                    // In case that the image byte size is still too large reduce the quality in
                    // proportion to the desired byte size.
                    if (os != null) {
                        try {
                            os.close()
                        } catch (e: IOException) {
                            Log.e(TAG, e.message, e)
                        }
                    }
                    os = ByteArrayOutputStream()
                    b!!.compress(compressFormat, quality, os)
                    val jpgFileSize = os.size()
                    if (jpgFileSize > byteLimit) {
                        quality = quality * byteLimit / jpgFileSize // watch for int division!
                        if (quality < MINIMUM_IMAGE_COMPRESSION_QUALITY) {
                            quality = MINIMUM_IMAGE_COMPRESSION_QUALITY
                        }
                        try {
                            os.close()
                        } catch (e: IOException) {
                            Log.e(TAG, "${e.message}", e)
                        }
                        os = ByteArrayOutputStream()
                        b.compress(compressFormat, quality, os)
                    }
                } catch (e: OutOfMemoryError) {
                    Log.w(
                        TAG, "getResizedImageData - image too big (OutOfMemoryError), will try "
                                + " with smaller scale factor, cur scale factor: " + scaleFactor
                    )
                    // fall through and keep trying with a smaller scale factor.
                }
                scaleFactor *= IMAGE_SCALE_FACTOR
                attempts++
                resultTooBig = os == null || os.size() > byteLimit
            } while (resultTooBig && attempts < NUMBER_OF_RESIZE_ATTEMPTS)
            if (!resultTooBig && orientation != 0) {
                // Rotate the final bitmap if we need to.
                try {
                    b = rotateBitmap(b, orientation)
                    os = ByteArrayOutputStream()
                    b!!.compress(compressFormat, quality, os)
                    resultTooBig = os.size() > byteLimit
                } catch (e: OutOfMemoryError) {
                    Log.w(TAG, "getResizedImageData - image too big (OutOfMemoryError)")
                    return null
                }
            }
            b?.recycle()
            if (resultTooBig) {
                Log.d(TAG, "getResizedImageData returning NULL because the result is too big: " + " requested max: " + byteLimit + " actual: " + os!!.size())
            }

//            logD(TAG, "getResizedImageData: width=$width, height=$height, os size=${os?.size()}, byteLimit=$byteLimit")
            if (resultTooBig) null else os?.toByteArray()
        } catch (e: FileNotFoundException) {
            Log.e(TAG, e.message, e)
            null
        } catch (e: OutOfMemoryError) {
            Log.e(TAG, e.message, e)
            null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    Log.e(TAG, e.message, e)
                }
            }
            if (os != null) {
                try {
                    os.close()
                } catch (e: IOException) {
                    Log.e(TAG, e.message, e)
                }
            }
        }
    }

    private fun getOrientation(context: Context, uri: Uri): Int {
        var dur = System.currentTimeMillis()
        var rotate = 0
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            try {
                val exif = ExifInterface(inputStream!!)
                val orientation: Int = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }
                inputStream.close()
            } catch (e: Exception) {
                Log.e(TAG, "getOrientation: ", e)
                e.printStackTrace()
            }
            return rotate
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "Can't open uri: $uri", e)
        } finally {
            dur = System.currentTimeMillis() - dur
            Log.d(TAG, "UriImage.getOrientation (exif path) took: $dur ms")
        }
        return 0
    }

    private fun rotateBitmap(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var newBitmap = bitmap
        if (degrees != 0 && newBitmap != null) {
            val m = Matrix()
            val w = newBitmap.width
            val h = newBitmap.height
            m.setRotate(degrees.toFloat(), w.toFloat() / 2, h.toFloat() / 2)
            try {
                val rotatedBitmap = Bitmap.createBitmap(newBitmap, 0, 0, w, h, m, true)
                if (newBitmap != rotatedBitmap && rotatedBitmap != null) {
                    newBitmap.recycle()
                    newBitmap = rotatedBitmap
                }
            } catch (ex: OutOfMemoryError) {
                Log.e(TAG, "OOM in rotateBitmap", ex)
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return newBitmap
    }

    fun getMediaFileSize(context: Context, uri: Uri): Long{
        var fileSize = 0L
        val cursor: Cursor? = context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.SIZE), null, null, null)
        if(cursor!=null && cursor.moveToFirst()){
            fileSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
            cursor.close()
        }
        return fileSize
    }
}