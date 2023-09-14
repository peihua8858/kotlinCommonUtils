@file:JvmName("MD5Util")
@file:JvmMultifileClass

package com.fz.common.encrypt

import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun File?.md5(): String {
    if (this == null) {
        return ""
    }
    try {
        FileInputStream(this).use { input ->
            val byteBuffer = input.channel
                .map(FileChannel.MapMode.READ_ONLY, 0, this.length())
            return byteBuffer.md5()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

fun Any?.md5(toLowerCase: Boolean = true): String {
    if (this == null) {
        return ""
    }
    try {
        val md5 = MessageDigest.getInstance("MD5")
        when (this) {
            is String -> {
                md5.update(this.toByteArray(StandardCharsets.UTF_8))
            }

            is ByteBuffer -> {
                md5.update(this)
            }

            is ByteArray -> {
                md5.update(this)
            }

            else -> {
                md5.update(this.toString().toByteArray(StandardCharsets.UTF_8))
            }
        }
        return String(Hex.encodeHex(md5.digest(), toLowerCase))
    } catch (e: Exception) {
        return ""
    }
}
