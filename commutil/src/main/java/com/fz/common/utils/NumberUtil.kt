@file:JvmName("NumberUtil")
@file:JvmMultifileClass

package com.fz.common.utils

inline fun Double?.ifNullOrEmpty(defaultValue: () -> Any): Double =
    if (this == null || isNaN()) defaultValue().toDouble() else this

inline fun Int?.ifNullOrEmpty(defaultValue: () -> Any): Int =
    this ?: defaultValue().toInteger()

inline fun Float?.ifNullOrEmpty(defaultValue: () -> Any): Float =
    if (this == null || isNaN()) defaultValue().toFloat() else this

inline fun Long?.ifNullOrEmpty(defaultValue: () -> Any): Long =
    this ?: defaultValue().toLong()
