@file:JvmName("MD5Util")
@file:JvmMultifileClass
package com.fz.common.encrypt

import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
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

fun Any?.md5(): String {
    if (this == null) {
        return ""
    }
    try {
        val md5 = MessageDigest.getInstance("MD5")
        when (this) {
            is String -> {
                md5.update(this.toByte())
            }
            is ByteBuffer -> {
                md5.update(this)
            }
            is ByteArray -> {
                md5.update(this)
            }
            else -> {
                md5.update(this.toString().toByte())
            }
        }
        return md5.digest().toHex()
    } catch (e: Exception) {
        return ""
    }
}

fun ByteArray?.toHex(): String {
    if (this == null) {
        return ""
    }
    val bi = BigInteger(1, this)
    return bi.toString(16)
}