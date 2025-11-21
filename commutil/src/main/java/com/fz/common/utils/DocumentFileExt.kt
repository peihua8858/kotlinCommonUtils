package com.fz.common.utils

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import java.io.OutputStream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * 判断字符串是否为空
 * @author dingpeihua
 * @date 2020/8/7 8:58
 * @version 1.0
 */
@OptIn(ExperimentalContracts::class)
fun String?.canReadPathByDocumentFile(context: Context): Boolean {
    contract {
        returns(true) implies (this@canReadPathByDocumentFile != null)
    }
    return this?.getDocumentFile(context).canReadPathByDocumentFile()
}

/**
 * 判断字符串是否为空
 * @author dingpeihua
 * @date 2020/8/7 8:58
 * @version 1.0
 */
@OptIn(ExperimentalContracts::class)
fun DocumentFile?.canReadPathByDocumentFile(): Boolean {
    contract {
        returns(true) implies (this@canReadPathByDocumentFile != null)
    }
    return this?.canRead() == true
}

@Volatile
private var dataDocumentFile: DocumentFile? = null

fun String.getDocumentFile(context: Context): DocumentFile? {
    if (dataDocumentFile == null) {
        synchronized(this) {
            if (dataDocumentFile == null) {
                dataDocumentFile = DocumentFile.fromTreeUri(context, this.toUri())
            }
        }
    }
    return dataDocumentFile
}

/**
 * 通过segment片段定位到parent的指定文件夹，如果没有则尝试创建
 */
@Throws(java.lang.Exception::class)
fun DocumentFile.getDocumentFileBySegments(segment: String?): DocumentFile? {
    return getDocumentFileBySegments(segment, true)
}

/**
 * 通过segment片段定位到parent的指定文件夹
 */
fun DocumentFile.getDocumentFileBySegments(
    segment: String?,
    create: Boolean,
): DocumentFile? {
    if (segment == null) return null
    val segments = segment.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var documentFile = this
    try {
        for (i in segments.indices) {
            var lookup = documentFile.findDocumentFile(segments[i])
            if (lookup == null) {
                if (create) lookup = documentFile.createDirectory(segments[i])
                else throw Exception("can not find path $segment")
            }
            if (lookup == null) {
                throw Exception("Can not create folder " + segments[i])
            }
            documentFile = lookup
        }
        return documentFile
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun DocumentFile?.findDocumentFile(fileName: String): DocumentFile? {
    try {
        if (this == null) return null
        val result = this.findFile(fileName)
        if (result != null) return result
        val list = this.listFiles()
        for (d in list) {
            if (fileName.equals(d.name, ignoreCase = true)) {
                return d
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}


/**
 * 获取指定文件（包含文件夹及其子文件）大小
 *
 * @param f
 * @return
 * @throws Exception
 */
fun DocumentFile?.getFileSize(): Long {
    if (this == null || !this.exists()) {
        return 0
    }
    if (isFile) {
        return length()
    }
    var size: Long = 0
    val files = listFiles()
    if (files != null) {
        for (file in files) {
            size += file.getFileSize()
        }
    }
    return size
}


@Throws(java.lang.Exception::class)
fun String.getDocumentFileOf(context: Context, packageName: String): DocumentFile? {
    return DocumentFile.fromTreeUri(context, ("$this%2F$packageName").toUri())
}

/**
 * 获取导出根目录的documentFile
 */
@Throws(java.lang.Exception::class)
fun getExportPathDocumentFile(
    context: Context,
    segments: String?,
    externalStorageUri: Uri,
): DocumentFile? {
    val documentFile =
        DocumentFile.fromTreeUri(context, externalStorageUri)
    if (documentFile == null || !documentFile.canWrite()) {
        throw RuntimeException("Exporting path invalid or can not write to it, please check")
    }
    return documentFile.getDocumentFileBySegments(segments)
}


/**
 * 创建一个按照命名规则命名的写入documentFile的输出流
 *
 * @param [this] 要写入的documentFile
 * @return 已按照命名规则的写入的documentFile输出流
 */
@Throws(java.lang.Exception::class)
fun DocumentFile.getOutputStreamForDocumentFile(context: Context): OutputStream? {
    return context.contentResolver.openOutputStream(uri)
}