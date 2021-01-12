@file:JvmName("ParseUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.content.Context
import androidx.annotation.StringRes

/**
 * 将Object对象转成boolean类型
 *
 * @param value
 * @return 如果value不能转成boolean，则默认defaultValue
 */
@JvmOverloads
fun Any?.toBoolean(value: Any?, defaultValue: Boolean = false): Boolean {
    if (value is Boolean) {
        return value
    } else if (value is String) {
        return "true".equals(value as String?, ignoreCase = true)
    }
    return defaultValue
}

/**
 * 将Object对象转成boolean类型
 *
 * @param value
 * @return 如果value不能转成boolean，则默认false
 */
@JvmOverloads
fun Any?.toBoolean(defaultValue: Boolean = false): Boolean {
    return toBoolean(this, defaultValue)
}

/**
 * 将Object对象转成Double类型
 *
 * @param value
 * @return 如果value不能转成Double，则默认defaultValue
 */
@JvmOverloads
fun Any?.toDouble(value: Any?, defaultValue: Double = 0.00): Double {
    when (value) {
        is Double -> {
            return value
        }
        is Number -> {
            return value.toDouble()
        }
        is String -> {
            try {
                return java.lang.Double.valueOf((value as String?)!!)
            } catch (ignored: Exception) {
            }
        }
    }
    return defaultValue
}

/**
 * 将Object对象转成Double类型
 *
 * @param value
 * @return 如果value不能转成Double，则默认0.00
 */
@JvmOverloads
fun Any?.toDouble(defaultValue: Double = 0.00): Double {
    return toDouble(this, defaultValue)
}

/**
 * 将Object对象转成Double类型
 *
 * @param value
 * @return 如果value不能转成Float，则默认defaultValue
 */
@JvmOverloads
fun Any?.toFloat(value: Any?, defaultValue: Float = 0.00f): Float {
    when (value) {
        is Double -> {
            return value as Float
        }
        is Number -> {
            return value.toFloat()
        }
        is String -> {
            try {
                return java.lang.Float.valueOf((value as String?)!!)
            } catch (ignored: Exception) {
            }
        }
    }
    return defaultValue
}

/**
 * 将Object对象转成Double类型
 *
 * @param value
 * @return 如果value不能转成Float，则默认0.00
 */
@JvmOverloads
fun Any?.toFloat(defaultValue: Float = 0.00f): Float {
    return toFloat(this, defaultValue)
}

/**
 * 将Object对象转成Integer类型
 *
 * @param value
 * @return 如果value不能转成Integer，则默认0
 */
@JvmOverloads
fun Any?.toInteger(defaultValue: Int = 0): Int {
    return toInteger(this, defaultValue)
}

/**
 * 将Object对象转成Integer类型
 *
 * @param value
 * @return 如果value不能转成Integer，则默认0
 */
@JvmOverloads
fun Any?.toInteger(value: Any?, defaultValue: Int = 0): Int {
    when (value) {
        is Int -> {
            return value
        }
        is Number -> {
            return value.toInt()
        }
        is String -> {
            try {
                return value.toDouble().toInt()
            } catch (ignored: Exception) {
            }
        }
    }
    return defaultValue
}

/**
 * 将Object对象转成Long类型
 *
 * @param value
 * @return 如果value不能转成Long，则默认0
 */
@JvmOverloads
fun Any?.toLong(defaultValue: Long = 0L): Long {
    return toLong(this, defaultValue)
}

/**
 * 将Object对象转成Long类型
 *
 * @param value
 * @return 如果value不能转成Long，则默认defaultValue
 */
@JvmOverloads
fun Any?.toLong(value: Any?, defaultValue: Long = 0L): Long {
    when (value) {
        is Long -> {
            return value
        }
        is Number -> {
            return value.toLong()
        }
        is String -> {
            try {
                return java.lang.Long.valueOf((value as String?)!!)
            } catch (ignored: NumberFormatException) {
            }
        }
    }
    return defaultValue
}

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(context: Context?, value: Any?, @StringRes resId: Int): String {
    return toString(value, context?.getString(resId) ?: "")
}

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认defaultValue
 */
@JvmOverloads
fun Any?.toString(value: Any?, defaultValue: String = ""): String {
    if (value is String) {
        return value
    } else if (value != null) {
        return value.toString()
    }
    return defaultValue
}

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
@JvmOverloads
fun Any?.toString(defaultValue: String = ""): String {
    return toString(this, defaultValue)
}