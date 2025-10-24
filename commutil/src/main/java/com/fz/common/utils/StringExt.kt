package com.fz.common.utils

import android.content.Context
import android.graphics.Bitmap
import com.fz.common.file.adjustBitmapOrientation
import com.fz.common.file.cacheFile
import com.fz.common.file.createFileName
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.text.SimpleDateFormat
import kotlin.coroutines.resume

private val sf = SimpleDateFormat("yyyy-MM-dd")

/**
 * 根据时间戳创建文件名
 *
 * @return
 */
fun String.createFolderFileName(): String {
    val millis = System.currentTimeMillis()
    return this + sf.format(millis)
}

fun String.createFolderFile(context: Context,): File {
    val fileCache = createFolderFileName()
    val parentPath = context.cacheFile("files")
    return File(parentPath, fileCache)
}

fun String.createFile(context: Context,extension: String): File {
    val fileCache = createFileName(extension)
    val parentPath = context.cacheFile("files")
    return File(parentPath, fileCache)
}



suspend fun String.adjustBitmapOrientationAsync(): Bitmap? {
    return try {
        suspendCancellableCoroutine<Bitmap?> { continuation ->
            continuation.resume(this.adjustBitmapOrientation())
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

fun String.adjustBitmapOrientation(): Bitmap? {
    return File(this).adjustBitmapOrientation()

}