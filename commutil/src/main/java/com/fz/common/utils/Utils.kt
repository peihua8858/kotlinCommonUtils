@file:JvmName("Utils")

package com.fz.common.utils

import android.os.Looper
import android.webkit.WebView
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

fun isMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

fun Any?.checkMainThread(msg: String?): Boolean {
    if (isMainThread()) {
        return true
    }
    throw IllegalStateException(msg)
}

/**
 * 布局方向是从右到左
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Any?.isAppRtl(local: Locale?): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(local) == ViewCompat.LAYOUT_DIRECTION_RTL
}

/**
 * 布局方向是从右到左
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Locale?.isAppRtl(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(this) == ViewCompat.LAYOUT_DIRECTION_RTL
}

/**
 * 布局方向是从左到右
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Locale?.isAppLtr(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(this) == ViewCompat.LAYOUT_DIRECTION_LTR
}

/**
 * 布局方向是从左到右
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Any?.isAppLtr(local: Locale?): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(local) == ViewCompat.LAYOUT_DIRECTION_LTR
}

@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }
    return this != null
}

@OptIn(ExperimentalContracts::class)
fun Any?.isNull(): Boolean {
    contract {
        returns(false) implies (this@isNull != null)
    }
    return this == null
}

@OptIn(ExperimentalContracts::class)
fun Any?.checkNotNull(value: Any?, msg: String): Boolean {
    contract {
        returns() implies (value != null)
    }
    return checkNotNull(value) { msg }
}

@OptIn(ExperimentalContracts::class)
fun <T : Any> checkNotNull(value: T?, lazyMessage: () -> Any): Boolean {
    contract {
        returns() implies (value != null)
    }
    if (value == null) {
        val message = lazyMessage()
        throw IllegalStateException(message.toString())
    } else {
        return false
    }
}

fun Any?.checkNotNull(msg: String?): Boolean {
    if (isNotNull()) {
        return true
    }
    throw NullPointerException(msg)
}

fun Array<Any?>?.checkNotNull(msg: String?): Boolean {
    if (isNotNull() && isNotEmpty()) {
        return true
    }
    throw NullPointerException(msg)
}


fun Any.rangeArray(min: Int, length: Int): Array<String?> {
    val data: Array<String?> = arrayOfNulls(length)
    for (i in 0 until length) {
        data[i] = ((min + i).toString())
    }
    return data
}

/**
 * 处理webview漏洞，删除危险API
 *
 * @param webView
 */
fun WebView?.dealJavascriptLeak() {
    if (this == null) {
        return
    }
    try {
        removeJavascriptInterface("searchBoxJavaBridge_")
        removeJavascriptInterface("accessibility")
        removeJavascriptInterface("accessibilityTraversal")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 深度拷贝，性能不佳
 * @author dingpeihua
 * @date 2021/2/26 11:01
 * @version 1.0
 */
fun <T : Any> T.deepCopy(): T {
    val objClassJava = this::class.java
    // 基本数据类型直接返回
    if (objClassJava.isPrimitive) {
        return this
    } else {
        val objClass = this::class
        if (objClass.javaPrimitiveType != null) {
            return this
        }
        return when (this) {
            is String -> {
                String(this.toByteArray(), Charsets.UTF_8) as T
            }
            is Array<*> -> {
                this.deepCopyOfArray() as T
            }
            is Set<*> -> {
                this.deepCopyOfSet() as T
            }
            is Collection<*> -> {
                this.deepCopyOfCollection() as T
            }
            is Map<*, *> -> {
                this.deepCopyOfMap() as T
            }
            is Date -> {
                Date(time) as T
            }
            else -> {
                val properties = objClass.memberProperties
                val newCopy = objClassJava.newInstance()
                properties.forEach { prop ->
                    val field = prop.javaField
                    if (field != null) {
                        field.isAccessible = true
                        val value = field.get(this)
                        if (value == null)
                            field.set(newCopy, value)
                        else
                            field.set(newCopy, value.deepCopy())
                    }
                }
                newCopy
            }
        }
    }
}