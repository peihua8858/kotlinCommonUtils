@file:JvmName("ContentResolverUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.fz.common.text.isNonEmpty
import java.io.File

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