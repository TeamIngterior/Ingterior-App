package com.ing.ingterior.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object FileWrapper {

    private const val TAG = "FileWrapper"
    const val DOCUMENTS_EXTERNAL_AUTHORITY = "com.android.externalstorage.documents"
    const val CONTACTS_FILES_AUTHORITY = "com.android.contacts.files"
    const val FILE_PROVIDER_AUTHORITY = "com.ing.ingterior.fileProvider";
    const val DOCUMENTS_AUTHORITY = "com.android.providers.downloads.documents"
    const val DOCUMENTS_MEDIA_AUTHORITY = "com.android.providers.media.documents"

    const val KB = 1024.0
    const val MB = 1024.0 * 1024.0

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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream) // JPEG 형식으로 압축하고 저장합니다.
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
        Log.d(TAG, "createImageFile: file=$file, file size=${file.length()}")

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

    fun getFileSizeFromUri(context: Context, uri: Uri): Long{
        val contentResolver: ContentResolver = context.contentResolver
        val fileSize: Long? = try {
            contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->
                parcelFileDescriptor.statSize // 파일 크기를 바이트 단위로 반환
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return -1
        }
        return fileSize ?: -1
    }

    fun getFileSize(context: Context, uri: Uri?): Long {
        var size = 0L
        if (uri != null && isFileUri(uri)) {
            try {
                val cSize: Cursor? = context.contentResolver.query(uri, null, null, null, null)
                if (cSize != null && cSize.moveToFirst()) {
                    size = cSize.getLong(cSize.getColumnIndexOrThrow(OpenableColumns.SIZE))
                    cSize.close()
                }
            } catch (e: IllegalStateException) {
                Log.e(TAG, "getFileSize", e)
            }
        }
        return size
    }

    fun isFileUri(uri: Uri?): Boolean {
        return uri != null && (TextUtils.equals(uri.scheme, ContentResolver.SCHEME_FILE)
                || isExternalStorageDocument(uri) || isProviderFile(uri) || isDownloadsDocument(uri))
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return DOCUMENTS_EXTERNAL_AUTHORITY == uri.authority
    }

    private fun isProviderFile(uri: Uri): Boolean{
        return uri.authority == this.FILE_PROVIDER_AUTHORITY
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return DOCUMENTS_AUTHORITY == uri.authority
    }

}