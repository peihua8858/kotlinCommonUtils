@file:JvmName("FileUtil")
@file:JvmMultifileClass

package com.fz.common.file

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.fz.common.R
import com.fz.common.array.isNonEmpty
import com.fz.common.text.isNonEmpty
import com.fz.common.utils.*
import com.socks.library.KLog
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.*
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.math.max
import kotlin.coroutines.resume
val UTF8: Charset = Charset.forName("UTF-8")
private val sf = SimpleDateFormat("yyyyMMdd_HHmmssSS")

/**
 * 根据时间戳创建文件名
 *
 * @return
 */
fun String.createFileName(extension: String): String {
    val millis = System.currentTimeMillis()
    return this + sf.format(millis) + "." + extension
}

/**
 * 提取文件路径[this]中的文件名带后缀，如果提取失败则生成一个文件名
 * 如提取：https://img.iplaysoft.com/wp-content/uploads/2019/free-images/free_ff e445^stock_photo.jpg!0x0.webp?astr
 * 则返回 free_ff e445^stock_photo.jpg!0x0.webp
 */
fun String.fetchPhotoFileName(): String {
    val result = fetchFileName()
    if (result.isNonEmpty()) {
        return result
    }
    return "IMG_".createFileName("jpg")
}

fun String.fetchMediaFileName(extension: String): String {
    val result = fetchFileName()
    if (result.isNonEmpty()) {
        return result
    }
    return "Media_".createFileName(extension)
}

fun String.fetchFileName(): String? {
    if (isNonEmpty()) {
        val pat: Pattern = Pattern.compile("(/)(?!.*\\1)[^\\\\/*?:<>|]+[.][a-zA-Z0-9]+") //正则判断
        val mc: Matcher = pat.matcher(this) //条件匹配
        if (mc.find()) {
            val result = mc.group()//截取文件名后缀名
            if (result.isNonEmpty()) {
                if (result.startsWith("/")) {
                    return result.substring(1)
                }
                return result
            }
        }
        val index = lastIndexOf("/")
        if (index != -1) {
            return substring(index + 1)
        }
    }
    return null
}

/**
 * 判断指定文件是否存在
 *
 * @param file 文件对象
 * @return 存在返回true，否则返回false
 */
fun File?.exists(): Boolean {
    return this != null && exists()
}

fun File?.mkdirs(): Boolean {
    return this != null && mkdirs()
}

/**
 * 判断指定文件是否存在
 *
 * @param fileName 文件对象
 * @return 存在返回true，否则返回false
 */
fun String?.exists(): Boolean {
    return !this.isNullOrEmpty() && File(this).exists()
}

fun File?.isFile(): Boolean {
    return this != null && isFile
}

fun String?.isFile(): Boolean {
    return !isNullOrEmpty() && File(this).isFile
}

fun File?.isDirectory(): Boolean {
    return this != null && this.isDirectory
}

fun File?.isDirectoryEmpty(): Boolean {
    return listFiles() == null
}

fun File?.listFiles(): Array<File>? {
    if (isDirectory()) {
        val files = this?.listFiles()
        return if (files != null && files.isNotEmpty()) files else null
    }
    return null
}

/**
 * 删除指定的文件
 *
 * @param file 文件对象
 * @return
 */
fun File?.deleteFile(): Boolean {
    return exists() && (this?.delete() ?: false)
}

/**
 * 删除文件或文件夹
 * @author dingpeihua
 * @date 2020/11/25 20:17
 * @version 1.0
 */
fun File?.deleteFileOrDir(): Boolean {
    return this?.let {
        if (!exists()) {
            return true
        }
        if (isFile) {
            return delete()
        }
        val files = listFiles()
        if (files != null) {
            for (file in files) {
                file.deleteFileOrDir()
            }
        }
        return delete()
    } ?: false
}

/**
 * 删除指定目录下所有的文件
 *
 * @param file 目录文件对象
 * @param isDeleteCurDir 是否删除当前目录
 * @return
 */
@JvmOverloads
fun File?.deleteDirectory(isDeleteCurDir: Boolean = true): Boolean {
    return this?.let {
        if (exists()) {
            if (isDirectory) {
                val files = listFiles()
                if (files.isNonEmpty()) {
                    // 先删除该目录下所有的文件
                    for (f: File? in files) {
                        f.deleteDirectory(true)
                    }
                    return true
                }
            } else {
                // 最后删除该目录
                if (isDeleteCurDir) {
                    return delete()
                }
            }
        }
        return false
    } ?: true
}

/**
 * 删除指定目录下所有的文件
 *
 * @param directoryName 目录文件对象
 * @return
 */
fun CharSequence?.deleteDirectory(): Boolean {
    return this.isNonEmpty() && File(this.toString()).deleteDirectory()
}

/**
 * 删除指定的文件或目录
 *
 * @param file 文件或目录对象
 */
fun File?.delete(): Boolean {
    if (!isNull()) {
        if (isFile) {
            return deleteFile()
        } else if (isDirectory) {
            return deleteDirectory()
        }
    }
    return false
}

/**
 * 删除指定的文件或目录
 *
 * @param fileOrDirName 文件名（全路径）或目录名
 */
fun CharSequence?.delete(): Boolean {
    return this.isNonEmpty() && File(this.toString()).delete()
}


@JvmOverloads
fun File?.write(content: String?, append: Boolean = false): Boolean {
    if (this == null) {
        return false
    }
    // OutputStreamWriter osw = null;
    if (content.isNullOrEmpty()) {
        return false
    }
    if (exists()) {
        delete()
    }
    if (parentFile?.exists() == false) {
        this.parentFile?.mkdirs()
    }
    try {
        FileOutputStream(this, append).use { fos ->
            KLog.d("write --> content==$content")
            fos.write(content.toByteArray(charset("utf-8")), 0, content.length)
            fos.flush()
            return true
        }
    } catch (e1: FileNotFoundException) {
        e1.printStackTrace()
    } catch (e1: UnsupportedEncodingException) {
        e1.printStackTrace()
    } catch (e1: IOException) {
        e1.printStackTrace()
    }
    return false
}

fun CharSequence?.write(fileName: String?): Boolean {
    if (fileName == null || this == null) {
        return false
    }
    return File(fileName).write(this.toString(), false)
}

fun CharSequence?.write(fileName: String?, append: Boolean): Boolean {
    if (fileName == null || this == null) {
        return false
    }
    return File(fileName).write(this.toString(), append)
}

fun File?.read(): String {
    if (this == null) {
        return ""
    }
    if (!exists()) {
        return ""
    }
    try {
        FileInputStream(this).use { fis ->
            val b = ByteArray(1024)
            val sb = StringBuilder()
            while (fis.read(b, 0, b.size) != -1) {
                sb.append(String(b, 0, b.size, UTF8))
            }
            return sb.toString()
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

fun String?.read(): String? {
    if (this == null) {
        return null
    }
    return File(this).read()
}

fun File?.createFile(): Boolean {
    try {
        if (isNotNull()) {
            return this.createNewFile()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}

fun String?.createFile(): Boolean {
    if (this == null) {
        return false
    }
    return !isNullOrEmpty() && File(this).createFile()
}

/**
 * 文件拷贝操作
 *
 * @param source 源文件
 * @param dest   目标文件
 * @return boolean true拷贝成功，反之失败
 * @author dingpeihua
 * @date 2018/5/12 20:28
 * @version 1.0
 */
fun File?.copyToFile(dest: File?): Boolean {
    return copyToFile(FileOutputStream(dest))
}

fun File?.copyToFile(dest: OutputStream?): Boolean {
    try {
        this?.let { input ->
            FileInputStream(input).use { fis ->
                dest?.let { out ->
                    out.use { fos ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (fis.read(buffer).also { length = it } > 0) {
                            fos.write(buffer, 0, length)
                        }
                        return true
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun File?.copyToFile(dest: InputStream?): Boolean {
    try {
        this?.let { input ->
            if (!input.exists()) {
                input.parentFile?.mkdirs()
            }
            FileOutputStream(input).use { fos ->
                dest?.let { input ->
                    input.use { fis ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (fis.read(buffer).also { length = it } > 0) {
                            fos.write(buffer, 0, length)
                        }
                        return true
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

fun File?.writeBitmapToFile(
    bitmap: Bitmap?,
    fileName: String?,
    deleteParentAllFile: Boolean,
): File? {
    if (fileName == null) {
        return null
    }
    if (deleteParentAllFile) {
        val isDelete = delete()
        KLog.d("LockWriteFile>>>isDelete:$isDelete")
    }
    if (!exists()) {
        mkdirs()
    }
    val image = File(this, fileName)
    return image.writeBitmapToFile(bitmap)
}

fun File?.writeBitmapToFile(bitmap: Bitmap?): File? {
    if (this == null || bitmap == null) {
        return null
    }
    try {
        FileOutputStream(this).use { outStream ->
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                outStream
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        bitmap.recycle()
    }
    return this
}

fun Bitmap?.writeBitmapToFile(outFile: File): File? {
    return if (outFile.isFile) {
        outFile.writeBitmapToFile(this)
    } else outFile.writeBitmapToFile(this, false)
}

fun File?.writeBitmapToFile(bitmap: Bitmap?, deleteFile: Boolean): File? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val rand = Random()
    val randomNum = rand.nextInt(1000 + 1)
    val fileName = "IMG_$timeStamp$randomNum.jpg"
    return writeBitmapToFile(bitmap, fileName, deleteFile)
}

fun File?.notifyScanFile(context: Context) {
    if (this != null && exists()) {
        try {
            //保存图片后发送广播通知更新数据库
            val uri = Uri.fromFile(this)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun File.writeImageToFile(data: ByteArray?): File? {
    return if (this.isFile) {
        writeToFile(data)
    } else writeImageToFile(data, this, false)
}

fun writeImageToFile(data: ByteArray?, outFile: File, deleteFile: Boolean): File? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val rand = Random()
    val randomNum = rand.nextInt(1000 + 1)
    val fileName = "IMG_$timeStamp$randomNum.jpg"
    return outFile.writeToFile(data, fileName, deleteFile)
}

fun File?.writeToFile(
    data: ByteArray?,
    fileName: String?,
    deleteParentAllFile: Boolean,
): File? {
    if (fileName == null || data == null) {
        return null
    }
    if (deleteParentAllFile) {
        val isDelete = delete()
        KLog.d("LockWriteFile>>>isDelete:$isDelete")
    }
    if (!exists()) {
        mkdirs()
    }
    val image = File(this, fileName)
    return image.writeToFile(data)
}

fun File?.writeToFile(data: InputStream?, deleteFile: Boolean = false): File? {
    if (this == null || data == null) {
        return null
    }
    if (deleteFile) {
        val isDelete = delete()
        KLog.d("LockWriteFile>>>isDelete:$isDelete")
    }
    try {
        FileOutputStream(this).use { outStream ->
            data.use { fis ->
                val buffer = ByteArray(1024)
                var length: Int
                while (fis.read(buffer).also { length = it } > 0) {
                    outStream.write(buffer, 0, length)
                }
                outStream.flush()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return this
}



fun File?.writeToFile(data: ByteArray?): File? {
    if (this == null) {
        return null
    }
    try {
        FileOutputStream(this).use { outStream ->
            outStream.write(data)
            outStream.flush()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return this
}

fun read(inputStream: InputStream?): String? {
    if (inputStream == null) {
        return null
    }
    try {
        val b = ByteArray(1024)
        val sb = StringBuilder()
        while (inputStream.read(b, 0, b.size) != -1) {
            sb.append(String(b, 0, b.size, UTF8))
        }
        inputStream.close()
        return sb.toString()
    } catch (e1: FileNotFoundException) {
        e1.printStackTrace()
    } catch (e1: UnsupportedEncodingException) {
        e1.printStackTrace()
    } catch (e1: IOException) {
        e1.printStackTrace()
    } finally {
        try {
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}

const val UNIT_KB: Long = 1024
const val UNIT_MB = 1024 * 1024.toLong()
const val UNIT_GB = 1024 * 1024 * 1024.toLong()

/**
 * 计算文件大小
 *
 * @param file
 * @author dingpeihua
 * @date 2020/9/10 1:17
 * @version 1.0
 */
fun File?.calculate(): String {
    return calculate(getFolderSize())
}

/**
 * 获取文件夹大小
 *
 * @param parentFile File实例
 * @return long
 */
private fun File?.getFolderSize(): Long {
    if (this == null) {
        return 0
    }
    var size: Long = 0
    try {
        val files = this.listFiles()
        if (files == null || files.isEmpty()) {
            return 0
        }
        for (file in files) {
            size = if (file.isDirectory) {
                size + file.getFolderSize()
            } else {
                size + file.length()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return size
}

/**
 * 计算文件大小
 *
 * @param length
 * @author dingpeihua
 * @date 2020/9/10 1:17
 * @version 1.0
 */
fun calculate(length: Long): String {
    return when {
        length >= UNIT_GB -> {
            String.format(Locale.US, "%.2f GB", length / 1024f / 1024f / 1024f)
        }

        length >= UNIT_MB -> {
            String.format(Locale.US, "%.2f MB", length / 1024f / 1024f)
        }

        length > UNIT_KB -> {
            String.format(Locale.US, "%.2f KB", length / 1024f)
        }

        else -> {
            String.format(Locale.US, "%.2f B", length.toFloat())
        }
    }
}

/**
 * 文件转base64字符串
 *
 * @return
 */
fun File?.toBase64(): String {
    if (!this.exists()) {
        return ""
    }
    try {
        var base64: String
        FileInputStream(this).use {
            val bytes = ByteArray(it.available())
            val length = it.read(bytes)
            base64 = Base64.encodeToString(bytes, 0, length, Base64.NO_WRAP)
        }
        return base64
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
    }
    return ""
}

fun String?.splitFileName(): Array<String>? {
    if (this == null) {
        return null
    }
    var name = this
    var extension: String? = null
    val i = this.lastIndexOf(".")
    if (i != -1) {
        name = this.substring(0, i)
        extension = this.substring(i)
    }
    if (!name.isNullOrEmpty() && !extension.isNullOrEmpty()) {
        return arrayOf(name, extension)
    }
    return null
}

fun Context.getFileNameByUri(uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        contentResolver.query(uri, null, null, null, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                result = cursor.getString(index)
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf(File.separator) ?: -1
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result
}

fun Context.getRealPathFromURI(contentUri: Uri): String? {
    return contentResolver.query(
        contentUri,
        null, null, null, null
    )
        .use { cursor ->
            if (cursor == null) {
                contentUri.path
            } else {
                cursor.moveToFirst()
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                cursor.getString(index)
            }
        }
}

/**
 * 建立一个MIME类型与文件后缀名的匹配表
 */
val MIME_MAP_TABLE = arrayOf(
    arrayOf(".3gp", "video/3gpp"),
    arrayOf(".apk", "application/vnd.android.package-archive"),
    arrayOf(".asf", "video/x-ms-asf"),
    arrayOf(".avi", "video/x-msvideo"),
    arrayOf(".bin", "application/octet-stream"),
    arrayOf(".bmp", "image/bmp"),
    arrayOf(".c", "text/plain"),
    arrayOf(".class", "application/octet-stream"),
    arrayOf(".conf", "text/plain"),
    arrayOf(".cpp", "text/plain"),
    arrayOf(".doc", "application/msword"),
    arrayOf(".docx", "application/msword"),
    arrayOf(".exe", "application/octet-stream"),
    arrayOf(".gif", "image/gif"),
    arrayOf(".gtar", "application/x-gtar"),
    arrayOf(".gz", "application/x-gzip"),
    arrayOf(".h", "text/plain"),
    arrayOf(".htm", "text/html"),
    arrayOf(".html", "text/html"),
    arrayOf(".jar", "application/java-archive"),
    arrayOf(".java", "text/plain"),
    arrayOf(".jpeg", "image/jpeg"),
    arrayOf(".jpg", "image/jpeg"),
    arrayOf(".js", "application/x-javascript"),
    arrayOf(".log", "text/plain"),
    arrayOf(".m3u", "audio/x-mpegurl"),
    arrayOf(".m4a", "audio/mp4a-latm"),
    arrayOf(".m4b", "audio/mp4a-latm"),
    arrayOf(".m4p", "audio/mp4a-latm"),
    arrayOf(".m4u", "video/vnd.mpegurl"),
    arrayOf(".m4v", "video/x-m4v"),
    arrayOf(".mov", "video/quicktime"),
    arrayOf(".mp2", "audio/x-mpeg"),
    arrayOf(".mp3", "audio/x-mpeg"),
    arrayOf(".mp4", "video/mp4"),
    arrayOf(".mpc", "application/vnd.mpohun.certificate"),
    arrayOf(".mpe", "video/mpeg"),
    arrayOf(".mpeg", "video/mpeg"),
    arrayOf(".mpg", "video/mpeg"),
    arrayOf(".mpg4", "video/mp4"),
    arrayOf(".mpga", "audio/mpeg"),
    arrayOf(".msg", "application/vnd.ms-outlook"),
    arrayOf(".ogg", "audio/ogg"),
    arrayOf(".pdf", "application/pdf"),
    arrayOf(".png", "image/png"),
    arrayOf(".pps", "application/vnd.ms-powerpoint"),
    arrayOf(".ppt", "application/vnd.ms-powerpoint"),
    arrayOf(".prop", "text/plain"),
    arrayOf(".rar", "application/x-rar-compressed"),
    arrayOf(".rc", "text/plain"),
    arrayOf(".rmvb", "audio/x-pn-realaudio"),
    arrayOf(".rtf", "application/rtf"),
    arrayOf(".sh", "text/plain"),
    arrayOf(".tar", "application/x-tar"),
    arrayOf(".tgz", "application/x-compressed"),
    arrayOf(".txt", "text/plain"),
    arrayOf(".wav", "audio/x-wav"),
    arrayOf(".wma", "audio/x-ms-wma"),
    arrayOf(".wmv", "audio/x-ms-wmv"),
    arrayOf(".wps", "application/vnd.ms-works"),
    arrayOf(".xml", "text/plain"),
    arrayOf(".z", "application/x-compress"),
    arrayOf(".zip", "application/zip"),
    arrayOf(".xlsx", "application/vnd.ms-excel"),
    arrayOf(".xls", "application/vnd.ms-excel"),
    arrayOf("", "*/*")
)

/**
 * 打开文件
 *
 * @param file
 */
@Throws(Exception::class)
fun File?.openFile(context: Context) {
    openFile(context, getMIMEType())
}

/**
 * 打开文件
 *
 * @param file
 */
fun File?.openFile(context: Context, type: String?) {
    this?.let {
        //判断是否是AndroidN以及更高的版本
        val contentUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android 7.0
            FileProvider.getUriForFile(
                context, context.packageName + ".fileProvider",
                it
            )
        } else {
            Uri.fromFile(it)
        }
        openFile(context, type, contentUri)
    }
}

/**
 * 打开文件
 *
 * @param contentUri
 */
fun Any.openFile(context: Context, type: String?, contentUri: Uri?) {
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //android 7.0
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
    //设置intent的Action属性
    intent.action = Intent.ACTION_VIEW
    //设置intent的data和Type属性。
    intent.setDataAndType(contentUri, type)
    //跳转
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.text_choose_application)
        )
    )
}

/**
 * 根据文件后缀名获得对应的MIME类型。
 *
 * @param file
 */
fun File?.getMIMEType(): String {
    return this?.let {
        var type = "*/*"
        val fName = name
        //获取后缀名前的分隔符"."在fName中的位置。
        val dotIndex = fName.lastIndexOf(".")
        if (dotIndex < 0) {
            return type
        }
        /* 获取文件的后缀名 */
        val end = fName.substring(dotIndex).toLowerCase()
        if (end === "") return type
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (i in MIME_MAP_TABLE.indices) {
            if (end == MIME_MAP_TABLE[i][0]) {
                type = MIME_MAP_TABLE[i][1]
                break
            }
        }
        return type
    } ?: ""
}

/**
 * 使用DownloadManger下载指定url文件
 *
 * @param context 上下文
 * @param url     需要下载的文件url
 * @return 返回当前下载的文件
 * @author dingpeihua
 * @date 2016/5/5 9:18
 * @version 1.0
 */
fun Any.downLoadFile(context: Context, url: String, mimeType: String?, fileName: String?): Long {
    KLog.d("Update_APP", "mainactivity,you clicked positive button")
    var downloadId: Long = 0
    try {
        // 获取下载服务
        val manager = context.downloadManager
        // 创建下载请求
        val down = DownloadManager.Request(Uri.parse(url))
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        // 发出通知并下载,(false指禁止发出通知，即后台下载）
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        if (mimeType.isNonEmpty()) {
            down.setMimeType(mimeType)
        }
        // 显示下载界面
        down.setVisibleInDownloadsUi(true)
        down.setDescription(context.getString(R.string.precessing))
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        // 将下载请求放入队列
        downloadId = manager?.enqueue(down) ?: 0
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return downloadId
}

@SuppressLint("SimpleDateFormat")
fun Any?.writeLog(logFolder: File, message: String) {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val time: String = dateFormat.format(Date())
    val fileName = "zf-crash-$time.log"
    val logFile = File(logFolder, fileName)
    logFile.write(message)
}

fun Any?.writeLog(logFolder: CharSequence, message: String) {
    writeLog(File(logFolder.toString()), message)
}

fun Any?.writeLog(message: String, logFolderName: CharSequence?) {
    if (logFolderName.isNullOrEmpty()) {
        return
    }
    writeLog(logFolderName, message)
}

fun File.ensureDir(): Boolean {
    try {
        isDirectory.no {
            isFile.yes {
                delete()
            }
            return mkdirs()
        }
    } catch (e: Exception) {
    }
    return false
}

/**
 * 获取指定文件（包含文件夹及其子文件）大小
 *
 * @param f
 * @return
 * @throws Exception
 */
fun String?.getFileSize(): Long {
    if (this.isNullOrEmpty()) {
        return 0
    }
    return File(this).getFileSize()
}

fun String?.formatSize(): String {
    if (this.isNullOrEmpty()) {
        return "0B"
    }
    return File(this).formatSize()
}

/**
 * 获取指定文件（包含文件夹及其子文件）大小
 *
 * @param f
 * @return
 * @throws Exception
 */
fun File?.getFileSize(): Long {
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

fun File?.formatSize(): String {
    return getFileSize().formatFileSize()
}

private val separator = File.separator
private fun Context.cachePath(cacheName: String): String {
    val cachePath = getDiskCacheDir()
    return "${if (cachePath != null) cachePath.absolutePath else cacheDir.absolutePath}${separator}$cacheName$separator"
}

fun Context.cacheFile(cacheName: String): File {
    val cachePath = cachePath(cacheName)
    val file = File(cachePath)
    if (!file.exists()) {
        file.mkdirs()
    }
    return file
}

/**
 * 转换文件大小
 *
 * @return
 */
fun Long.formatFileSize(): String {
    if (this == 0L) {
        return "0B"
    }
    return when {
        this < 1024 -> {
            String.format(Locale.US, "%.2fB", this.toFloat())
        }

        this < 1048576 -> {
            String.format(Locale.US, "%.2fKB", this / 1024f)
        }

        this < 1073741824 -> {
            String.format(Locale.US, "%.2fMB", this / 1048576f)
        }

        else -> {
            String.format(Locale.US, "%.2fGB", this / 1073741824f)
        }
    }
}


suspend fun File?.writeToZip(
    parent: String,
    zos: ZipOutputStream,
    bufferSize: Int = 4096,
    zipLevel: Int = 0,
    callback: (progress: Long, speed: Long) -> Unit = { process, speed -> },
) {
    if (this == null) {
        return
    }
    var parentTemp = parent
    if (isDirectory) {
        parentTemp += this.getName() + File.separator
        val fileItemList = this.listFiles()
        if (fileItemList != null) {
            if (fileItemList.size > 0) {
                for (f in fileItemList) {
                    f.writeToZip(parentTemp, zos, bufferSize, zipLevel, callback)
                }
            } else {
                try {
                    zos.putNextEntry(ZipEntry(parentTemp))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    } else if (isFile) {
        try {
            val zipEntry = ZipEntry(parent + getName())
            val totalLength = length()
            if (zipLevel == 0) {
                zipEntry.setMethod(ZipOutputStream.STORED)
                zipEntry.setCompressedSize(totalLength)
                zipEntry.setSize(totalLength)
                zipEntry.setCrc(this.cRC32.value)
            }
            zos.putNextEntry(zipEntry)
            val fis = inputStream()
            fis.writeToZip(zos, bufferSize, callback = callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


suspend fun File.copyToFile(
    destinationFile: File,
    bufferSize: Int = 1024 * 20,
    callback: (progress: Long, speed: Long) -> Unit = { process, speed -> },
) {
    FileInputStream(this).copyToFile(FileOutputStream(destinationFile), bufferSize, callback)
}

suspend fun FileInputStream.copyToFile(
    fos: FileOutputStream,
    bufferSize: Int = 1024 * 20,
    callback: (progress: Long, speed: Long) -> Unit = { process, speed -> },
): Boolean {
    return try {
        suspendCancellableCoroutine { continuation ->
            this.use { inputStream ->
                fos.use { outputStream ->
                    val channelInput = inputStream.channel
                    val channelOutput = outputStream.channel
                    val buffer = ByteBuffer.allocate(bufferSize)
                    var progress = 0L
                    var length: Int
                    while ((channelInput.read(buffer)
                            .also { length = it }) > 0 && continuation.isActive
                    ) {
                        progress += length.toLong()
                        buffer.flip() // 切换到读模式
                        channelOutput.write(buffer)
                        buffer.clear() // 清空缓冲区以供下次使用
                        callback(progress, length.toLong())
                    }
                    callback(progress, length.toLong())
                    fos.flush()
                    continuation.resume(true)
                }
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        false
    }
}

fun File.decodeFileToBitmap(screenWidth: Int, screenHeight: Int): Bitmap? {
    try {
        val mScreenWidth = screenWidth
        val mScreenHeight = screenHeight
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(FileInputStream(this), null, o)
        dLog { "decodeFileToBitmap, o: $o" }
        val width_tmp = o.outWidth
        val height_tmp = o.outHeight
        var scale = 1
        if (width_tmp <= mScreenWidth && height_tmp <= mScreenHeight) {
            scale = 1
        } else {
            val widthFit: Double = width_tmp * 1.0 / mScreenWidth
            val heightFit: Double = height_tmp * 1.0 / mScreenHeight
            val fit = max(widthFit, heightFit)
            scale = (fit + 0.5).toInt()
        }
        dLog { "decodeFileToBitmap, scale: $scale,width_tmp:$width_tmp,height_tmp:$height_tmp" }
        var bitmap: Bitmap? = null
        if (scale == 1) {
            bitmap = BitmapFactory.decodeStream(FileInputStream(this))
        } else {
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            bitmap = BitmapFactory.decodeStream(FileInputStream(this), null, o2)
        }
        if (bitmap != null) {
            eLog { "scale = " + scale + " bitmap.size = " + (bitmap.getRowBytes() * bitmap.getHeight()) }
        }
        dLog { "decodeFileToBitmap, bitmap: $bitmap" }
        return bitmap
    } catch (e: Throwable) {
        eLog { "fileNotFoundException, e: $e" }
    }
    return null
}


fun File.adjustBitmapOrientation(): Bitmap? {
    return try {
        var exifInterface: ExifInterface? = null
        var bitmap = BitmapFactory.decodeStream(FileInputStream(this))
        try {
            exifInterface = ExifInterface(this)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        val matrix = orientationMatrix
        dLog { "adjustBitmapOrientation, adjust degree " + matrix + "to 0." }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

val File.orientationMatrix: Matrix
    get() {
        val matrix = Matrix()
        try {
            val exif = ExifInterface(this)
            val orientation =
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )

            when (orientation) {
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return matrix
    }

/**
 * 获取一个文件的CRC32值
 */
@get:Throws(java.lang.Exception::class)
val File.cRC32: CRC32
    get() = FileInputStream(this).cRC32


val File.mimeTypeFromFilePath: String?
    get() {
        return name.mimeTypeFromFilePath
    }

val String.mimeTypeFromFilePath: String?
    get() {
        val extension = substringAfterLast('.', "")
        dLog { "openWithFile>>>>extension：$extension" }
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension)
    }