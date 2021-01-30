@file:JvmName("FragmentUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@ColorInt
fun Fragment?.getColor(@ColorRes colorRes: Int): Int {
    val context = this?.context ?: return 0.eLog { "Context  is null." }
    return ContextCompat.getColor(context, colorRes)
}

fun Fragment?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context = this?.context ?: return null.eLog { "Context  is null." }
    return ContextCompat.getDrawable(context, drawableRes)
}

fun Fragment?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return context.getDimens(resId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Fragment?.getResourceId(attrId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return context.getResourceId(attrId)
}

/**
 * 解析当前上下文主题，获取主题样式
 *
 * @param context    当前上下文
 * @param resId      资源ID
 * @param defaultRes 默认主题样式
 * @author dingpeihua
 * @date 2020/7/7 10:31
 * @version 1.0
 */
fun Fragment?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return resolveAttribute(context, resId, defaultRes)
}

/**
 * 解析当前上下文主题，获取主题样式
 *
 * @param context 当前上下文
 * @param resId   资源ID
 * @author dingpeihua
 * @date 2020/7/7 10:31
 * @version 1.0
 */
fun Fragment?.resolveAttribute(resId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return resolveAttribute(context, resId)
}