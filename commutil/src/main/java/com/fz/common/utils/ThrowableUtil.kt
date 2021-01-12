@file:JvmName("ThrowableUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import java.io.PrintWriter
import java.io.StringWriter

fun Throwable?.getStackTraceMessage(): String {
    if (this == null) {
        return "未知错误。"
    }
    val errors = StringWriter()
    printStackTrace(PrintWriter(errors))
    return errors.toString()
}

fun Any?.getThreadStackTrace(): String {
    val errors = StringWriter()
    val printWriter = PrintWriter(errors)
    val stackTraceElement = Thread.getAllStackTraces()
    for (element in stackTraceElement) {
        printWriter.println(element.key)
        val stackTraceElements:Array<StackTraceElement> = element.value
        for (stack in stackTraceElements) {
            printWriter.println("\tat $stack")
        }
    }
    return errors.toString()
}