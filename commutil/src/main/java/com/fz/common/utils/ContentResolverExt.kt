@file:JvmName("ContentResolverUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.fz.common.file.copyToFile
import com.fz.common.text.isNonEmpty
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * 根据uri获取文件
 * @author dingpeihua
 * @date 2021/1/28 9:35
 * @version 1.0
 */
fun Fragment.getFileFromUri(uri: String?): File? {
    return uri?.let {
        return getFileFromUri(it.toUri())
    }
}

/**
 * 根据uri获取文件
 * @author dingpeihua
 * @date 2021/1/28 9:35
 * @version 1.0
 */
fun Fragment.getFileFromUri(uri: Uri?): File? {
    val context = context ?: return null
    return context.getFileFromUri(uri)
}

/**
 * 根据uri获取文件
 * @author dingpeihua
 * @date 2021/1/28 9:35
 * @version 1.0
 */
fun Context.getFileFromUri(uri: String?): File? {
    return uri?.let {
        return getFileFromUri(it.toUri())
    }
}

/**
 * 根据uri获取文件
 * @author dingpeihua
 * @date 2021/1/28 9:35
 * @version 1.0
 */
fun Context.getFileFromUri(uri: Uri?): File? {
    return if (uri == null) {
        null
    } else when (uri.scheme) {
        "content" -> getFileFromContentUri(uri)
        "file" -> uri.path?.let {
            File(it)
        }

        null -> {
            val file = File(uri.toString())
            if (file.exists()) {
                file
            } else null
        }

        else -> null
    }
}

/**
 * 通过内容解析中查询uri中的文件路径
 */
fun Context.getFileFromContentUri(contentUri: Uri?): File? {
    val contentResolver = contentResolver ?: return null
    return contentResolver.getFileFromContentUri(contentUri)
}

/**
 * 通过内容解析中查询uri中的文件路径
 */
fun ContentResolver.getFileFromContentUri(contentUri: Uri?): File? {
    return contentUri?.let {
        val filePathColumn = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? = query(
            it, filePathColumn, null,
            null, null
        )
        return cursor?.use {
            cursor.moveToFirst()
            val filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
            if (filePath.isNonEmpty()) {
                return File(filePath)
            }
            null
        }
    }
}

/**
 * 保存图片[source]到系统相册
 * @param source 图片对象
 * @param title 文件显示名称
 * @param description 文件描述
 */
fun ContentResolver.saveBitmapToGallery(source: Bitmap, title: String, description: String): String? {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, title)
    values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
    values.put(MediaStore.Images.Media.DESCRIPTION, description)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    // Add the date meta data to ensure the image is added at the front of the gallery
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    try {
        val uri = insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val imageOut = openOutputStream(uri!!)
        try {
            source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut)
        } finally {
            imageOut?.flush()
            imageOut?.close()
        }
        val id = ContentUris.parseId(uri)
        // Wait until MINI_KIND thumbnail is generated.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            loadThumbnail(uri, Size(50, 50), CancellationSignal())
        } else {
            val miniThumb =
                MediaStore.Images.Thumbnails.getThumbnail(this, id, MediaStore.Images.Thumbnails.MINI_KIND, null)
            // This is for backward compatibility.
            storeThumbnail(miniThumb, id, 50f, 50f, MediaStore.Images.Thumbnails.MICRO_KIND)
        }
        // Everything went well above, publish it!
        values.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        update(uri, values, null, null);
        return uri.toString()
    } catch (e: java.lang.Exception) {
        return null
    }
}

private fun ContentResolver.storeThumbnail(
    source: Bitmap,
    id: Long,
    width: Float,
    height: Float,
    kind: Int
): Bitmap? {
    // create the matrix to scale it
    val matrix = Matrix()
    val scaleX = width / source.width
    val scaleY = height / source.height
    matrix.setScale(scaleX, scaleY)
    val thumb = Bitmap.createBitmap(
        source, 0, 0,
        source.width,
        source.height, matrix,
        true
    )
    val values = ContentValues(4)
    values.put(MediaStore.Images.Thumbnails.KIND, kind)
    values.put(MediaStore.Images.Thumbnails.IMAGE_ID, id.toInt())
    values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.height)
    values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.width)
    val url = insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values)
    return try {
        val thumbOut = openOutputStream(url!!)
        thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut)
        thumbOut!!.close()
        thumb
    } catch (ex: FileNotFoundException) {
        null
    } catch (ex: IOException) {
        null
    }
}

fun ContentResolver.saveFileToExternal(source: File, title: String,mimeType: String): Uri? {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, title)
    values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
    values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
    // Add the date meta data to ensure the image is added at the front of the gallery
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    try {
        val uri = insert(MediaStore.Files.getContentUri("external"), values)
        if (uri != null) {
            val imageOut = openOutputStream(uri)
            try {
                source.copyToFile(imageOut)
            } finally {
                imageOut?.flush()
                imageOut?.close()
            }
            // Wait until MINI_KIND thumbnail is generated.
            // Everything went well above, publish it!
            values.clear()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
            update(uri, values, null, null);
            return uri
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return null
}