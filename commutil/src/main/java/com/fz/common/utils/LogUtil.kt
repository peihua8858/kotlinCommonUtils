@file:JvmName("LogUtil")
package com.fz.common.utils

import com.socks.library.KLog

fun <T> T?.wLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.w(message)
    return this
}

fun <T> T?.eLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.e(message)
    return this
}

fun <T> T?.dLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.d(message)
    return this
}

fun <T> T?.iLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.i(message)
    return this
}

fun Int.dLog(lazyMessage: () -> Any): Int {
    val message = lazyMessage()
    KLog.d(message)
    return this
}

fun Double.dLog(lazyMessage: () -> Any): Double {
    val message = lazyMessage()
    KLog.d(message)
    return this
}

fun Float.dLog(lazyMessage: () -> Any): Float {
    val message = lazyMessage()
    KLog.d(message)
    return this
}

fun Long.dLog(lazyMessage: () -> Any): Long {
    val message = lazyMessage()
    KLog.d(message)
    return this
}


fun Int.eLog(lazyMessage: () -> Any): Int {
    val message = lazyMessage()
    KLog.e(message)
    return this
}

fun Double.eLog(lazyMessage: () -> Any): Double {
    val message = lazyMessage()
    KLog.e(message)
    return this
}

fun Float.eLog(lazyMessage: () -> Any): Float {
    val message = lazyMessage()
    KLog.e(message)
    return this
}

fun Long.eLog(lazyMessage: () -> Any): Long {
    val message = lazyMessage()
    KLog.e(message)
    return this
}

fun Int.wLog(lazyMessage: () -> Any): Int {
    val message = lazyMessage()
    KLog.w(message)
    return this
}

fun Double.wLog(lazyMessage: () -> Any): Double {
    val message = lazyMessage()
    KLog.e(message)
    return this
}

fun Float.wLog(lazyMessage: () -> Any): Float {
    val message = lazyMessage()
    KLog.w(message)
    return this
}

fun Long.wLog(lazyMessage: () -> Any): Long {
    val message = lazyMessage()
    KLog.w(message)
    return this
}