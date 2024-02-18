package com.ing.ingterior.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import com.ing.ingterior.injection.Factory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object FileWrapper {

    private const val TAG = "FileWrapper"

    private fun getOrCreateDirectory(context: Context): File{
        val pictureDirectory = File(context.dataDir, "Image")
        if(!pictureDirectory.exists()){
            pictureDirectory.mkdirs()
        }
        return pictureDirectory
    }

    fun createImageFile(context: Context, bitmap: Bitmap, baseFileName: String): File {
        Log.d(TAG, "createImageFile: baseFileName=$baseFileName")
        val parentDir: File = getOrCreateDirectory(context)

        var file = File(parentDir, baseFileName)
        var fileName: String

        // 파일 이름이 이미 존재하는지 확인하고, 존재한다면 새로운 이름을 생성합니다.
        var fileNumber = 1
        while (file.exists()) {
            val dotIndex = baseFileName.lastIndexOf('.')
            fileName = if (dotIndex != -1) {
                val name = baseFileName.substring(0, dotIndex)
                val extension = baseFileName.substring(dotIndex)
                "$name($fileNumber)$extension"
            } else {
                "$baseFileName($fileNumber)"
            }
            file = File(parentDir, fileName)
            fileNumber++
        }

        // 고유한 파일 이름으로 파일을 생성하고 비트맵을 저장합니다.
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream) // PNG 형식으로 압축하고 저장합니다.
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.flush()
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        Log.d(TAG, "createImageFile: file=$file")

        return file
    }

    fun createFileName(context: Context, uri: Uri): String? {
        val type = getMimeType(context, uri) ?: return null
        val name = getImageNameFromUri(context, uri)
        return "${name}.$type"
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        val mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            // ContentResolver를 통해 MIME 타입을 얻습니다.
            val contentResolver = context.contentResolver
            contentResolver.getType(uri)
        } else {
            // 파일 확장자를 기반으로 MIME 타입을 추정합니다.
            val fileExtension: String = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase(Locale.getDefault()))
        }

        return when(mimeType) {
            "image/jpeg" -> {
                "jpg"
            }
            "image/jpg" -> {
                "jpg"
            }
            "image/png" -> {
                "png"
            }
            else -> {
                null
            }
        }
    }

    fun getImageNameFromUri(context: Context, uri: Uri): String {
        var fileName = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(columnIndex)
            }
        }
        return fileName
    }
}