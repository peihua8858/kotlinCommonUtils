@file:JvmName("ParseUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes

///**
// * 将Object对象转成boolean类型
// *
// * @param value
// * @return 如果value不能转成boolean，则默认false
// */
//inline fun Any?.toBoolean(block: (result: Boolean) -> Unit) = block(toBoolean())

/**
 * 将Object对象转成boolean类型
 *
 * @param value
 * @return 如果value不能转成boolean，则默认false
 */
fun Any?.toBoolean(): Boolean {
    return toBoolean(this, false)
}

/**
 * 将Object对象转成boolean类型
 *
 * @param value
 * @return 如果value不能转成boolean，则默认false
 */
fun Any?.toBoolean(defaultValue: Boolean = false): Boolean {
    return toBoolean(this, defaultValue)
}

/**
 * 将Object对象转成boolean类型
 *
 * @param value
 * @return 如果value不能转成boolean，则默认defaultValue
 */
fun Any?.toBoolean(value: Any?, defaultValue: Boolean = false): Boolean {
    if (value is Boolean) {
        return value
    } else if (value is String) {
        return "true".equals(value as String?, ignoreCase = true)
    }
    return defaultValue
}

///**
// * 将Object对象转成Double类型
// *
// * @return 如果value不能转成Double，则默认0.00
// */
//inline fun Any?.toDouble(block: (result: Double) -> Unit) = block(toDouble())

/**
 * 将Object对象转成Double类型
 *
 * @return 如果value不能转成Double，则默认0.00
 */
fun Any?.toDouble(): Double {
    return toDouble(0.00)
}

/**
 * 将Object对象转成Double类型
 *
 * @param value
 * @return 如果value不能转成Double，则默认0.00
 */
fun Any?.toDouble(defaultValue: Double = 0.00): Double {
    when (this) {
        is Double -> {
            return this
        }
        is Number -> {
            return this.toDouble()
        }
        is String -> {
            try {
                return java.lang.Double.valueOf((this as String?)!!)
            } catch (ignored: Exception) {
            }
        }
        else -> {
            try {
                return java.lang.Double.valueOf((this.toString()))
            } catch (ignored: NumberFormatException) {
            }
        }
    }
    return defaultValue
}

///**
// * 将Object对象转成Double类型
// *
// * @return 如果value不能转成Float，则默认0.00
// */
//inline fun Any?.toFloat(block: (result: Float) -> Unit) = block(toFloat())

/**
 * 将Object对象转成Float类型
 *
 * @return 如果value不能转成Float，则默认0.00
 */
fun Any?.toFloat(): Float {
    return toFloat(0.00f)
}

/**
 * 将Object对象转成Float类型
 *
 * @param value
 * @return 如果value不能转成Float，则默认0.00
 */
fun Any?.toFloat(defaultValue: Float = 0.00f): Float {
    when (this) {
        is Double -> {
            return this as Float
        }
        is Number -> {
            return this.toFloat()
        }
        is String -> {
            try {
                return java.lang.Float.valueOf((this))
            } catch (ignored: Exception) {
            }
        }
        else -> {
            try {
                return java.lang.Float.valueOf((this.toString()))
            } catch (ignored: NumberFormatException) {
            }
        }
    }
    return defaultValue
}

///**
// * 将Object对象转成Integer类型
// *
// * @return 如果value不能转成Integer，则默认0
// */
//inline fun Any?.toInteger(block: (result: Int) -> Unit) = block(toInteger())

/**
 * 将Object对象转成Integer类型
 *
 * @param value
 * @return 如果value不能转成Integer，则默认0
 */
fun Any?.toInteger(): Int {
    return toInteger(0)
}

/**
 * 将Object对象转成Integer类型
 *
 * @param value
 * @return 如果value不能转成Integer，则默认0
 */
fun Any?.toInteger(defaultValue: Int = 0): Int {
    when (this) {
        is Int -> {
            return this
        }
        is Number -> {
            return this.toInt()
        }
        is String -> {
            try {
                return this.toDouble().toInt()
            } catch (ignored: Exception) {
            }
        }
        else -> {
            try {
                return Integer.valueOf((this.toString()))
            } catch (ignored: NumberFormatException) {
            }
        }
    }
    return defaultValue
}

///**
// * 将Object对象转成Long类型
// *
// * @return 如果value不能转成Long，则默认0
// */
//inline fun Any?.toLong(block: (result: Long) -> Unit) = block(toLong())

/**
 * 将Object对象转成Long类型
 *
 * @param value
 * @return 如果value不能转成Long，则默认0
 */
fun Any?.toLong(): Long {
    return toLong(0L)
}

/**
 * 将Object对象转成Long类型
 *
 * @param value
 * @return 如果value不能转成Long，则默认0
 */
fun Any?.toLong(defaultValue: Long = 0L): Long {
    when (this) {
        is Long -> {
            return this
        }
        is Number -> {
            return this.toLong()
        }
        is String -> {
            try {
                return java.lang.Long.valueOf(this)
            } catch (ignored: NumberFormatException) {
            }
        }
        else -> {
            try {
                return java.lang.Long.valueOf((this.toString()))
            } catch (ignored: NumberFormatException) {
            }
        }
    }
    return defaultValue
}

///**
// * 将Object对象转成String类型
// *
// * @return 如果value不能转成String，则默认""
// */
//inline fun Any?.toString(block: (result: String) -> Unit) = block(toString())

/**
 * 将Object对象转成String类型
 *
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(): String = toString("")

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(defaultValue: String = ""): String {
    return when {
        this is String -> {
            this
        }
        this is EditText -> {
            text.toString()
        }
        this is TextView -> {
            text.toString()
        }
        this != null -> {
            this.toString()
        }
        else -> defaultValue
    }
}

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(context: Context?, value: Any?, @StringRes resId: Int): String {
    return value.toString(context?.getString(resId) ?: "")
}

/**
 * 将Object对象转成String类型
 *
 * @param value
 * @return 如果value不能转成String，则默认""
 */
fun Any?.toString(context: Context?, @StringRes resId: Int): String {
    return toString(context?.getString(resId) ?: "")
}

