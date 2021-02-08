@file:JvmName("IOUtil")
package com.fz.common.file

import android.database.Cursor
import android.text.TextUtils
import androidx.annotation.NonNull
import com.socks.library.KLog
import java.io.*

fun Closeable?.close() {
    if (this != null) {
        try {
            close()
        } catch (e: Throwable) {
            e.printStackTrace()
            KLog.e(e)
        }
    }
}

fun Cursor?.closeQuietly() {
    if (this != null) {
        try {
            this.close()
        } catch (e: Throwable) {
            KLog.e(e)
        }
    }
}

@Throws(Exception::class)
fun InputStream?.readBytes(): ByteArray? {
    return this?.let {
        BufferedInputStream(this).use { input ->
            val out = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var len: Int
            while (input.read(buf).also { len = it } != -1) {
                out.write(buf, 0, len)
            }
            out.toByteArray()
        }
    }
}

@Throws(IOException::class)
fun InputStream?.readBytes(skip: Long, size: Int): ByteArray? {
    return this?.let { input ->
        var tempSkip = skip
        val result = ByteArray(size)
        if (tempSkip > 0) {
            var skipped: Long = 0
            while (tempSkip > 0 && input.skip(tempSkip).also { skipped = it } > 0) {
                tempSkip -= skipped
            }
        }
        for (i in 0 until size) {
            result[i] = this.read().toByte()
        }
        result
    }
}

/**
 *
 * @param charset 编码方式
 * @return 如果读取成功，返回读取到的内容，否则返回空字符串
 * @author dingpeihua
 * @date 2020/11/25 20:09
 * @version 1.0
 */
@JvmOverloads
@Throws(IOException::class)
@NonNull
fun InputStream?.readStr(charset: String = "UTF-8"): String {
    return this?.let { input ->
        BufferedInputStream(input).use { input1 ->
            val reader: Reader = InputStreamReader(input1, charset)
            val sb = StringBuilder()
            val buf = CharArray(1024)
            var len: Int
            while (reader.read(buf).also { len = it } >= 0) {
                sb.append(buf, 0, len)
            }
            sb.toString()
        }
    } ?: ""
}

@JvmOverloads
@Throws(IOException::class)
fun OutputStream?.writeStr(str: String?, charset: String = "UTF-8") {
    this?.let {
        val writer: Writer = OutputStreamWriter(this, charset)
        writer.write(str)
        writer.flush()
    }
}

@Throws(IOException::class)
fun InputStream?.copy(out: OutputStream) {
    this?.let { it ->
        BufferedInputStream(it).use { input ->
            BufferedOutputStream(out).use { output ->
                var len = 0
                val buffer = ByteArray(1024)
                while (input.read(buffer).also { len = it } != -1) {
                    output.write(buffer, 0, len)
                }
                output.flush()
            }
        }
    }
}
