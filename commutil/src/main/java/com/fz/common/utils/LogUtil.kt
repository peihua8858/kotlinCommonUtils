@file:JvmName("LogUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import com.socks.library.KLog

private const val STACK_TRACE_INDEX = 6
fun <T> T?.aLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.A, null, message)
    return this
}

fun <T> T?.vLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.V, null, message)
    return this
}

fun <T> T?.jsonLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.JSON, null, message)
    return this
}

fun <T> T?.xmlLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.XML, null, message)
    return this
}

fun <T> T?.wLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.W, null, message)
    return this
}

fun <T> T?.eLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.E, null, message)
    return this
}

fun <T> T?.dLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.D, null, message)
    return this
}

fun <T> T?.iLog(lazyMessage: () -> Any): T? {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.I, null, message)
    return this
}

fun Int.dLog(lazyMessage: () -> Any): Int {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.D, null, message)
    return this
}

fun Double.dLog(lazyMessage: () -> Any): Double {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.D, null, message)
    return this
}

fun Float.dLog(lazyMessage: () -> Any): Float {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.D, null, message)
    return this
}

fun Long.dLog(lazyMessage: () -> Any): Long {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.D, null, message)
    return this
}


fun Int.eLog(lazyMessage: () -> Any): Int {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.D, null, message)
    return this
}

fun Double.eLog(lazyMessage: () -> Any): Double {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.E, null, message)
    return this
}

fun Float.eLog(lazyMessage: () -> Any): Float {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.E, null, message)
    return this
}

fun Long.eLog(lazyMessage: () -> Any): Long {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.E, null, message)
    return this
}

fun Int.wLog(lazyMessage: () -> Any): Int {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.W, null, message)
    return this
}

fun Double.wLog(lazyMessage: () -> Any): Double {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.W, null, message)
    return this
}

fun Float.wLog(lazyMessage: () -> Any): Float {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.W, null, message)
    return this
}

fun Long.wLog(lazyMessage: () -> Any): Long {
    val message = lazyMessage()
    KLog.printLog(STACK_TRACE_INDEX, KLog.W, null, message)
    return this
}