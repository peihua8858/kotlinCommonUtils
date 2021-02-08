@file:JvmName("ActivityUtil")

package com.fz.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager

@ColorInt
fun Activity?.getColor(@ColorRes colorRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return ContextCompat.getColor(context, colorRes)
}

fun Activity?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context: Context = checkContext(this) ?: return null.eLog { "Context  is null." }
    return ContextCompat.getDrawable(context, drawableRes)
}

fun Activity?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return context.getDimens(resId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Activity?.getResourceId(attrId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return getResourceId(context, attrId)
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
fun Activity?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
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
fun Activity?.resolveAttribute(resId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return resolveAttribute(context, resId)
}

fun Any?.findActivity(): Activity? {
    return findActivity(checkContextOrNull(this))
}

fun Context?.getActivity(): Activity? {
    return getActivity(this)
}

fun Any?.getActivity(context: Context?): Activity? {
    var cxt = context
    while (cxt is ContextWrapper) {
        if (cxt is Activity) {
            return cxt
        }
        cxt = cxt.baseContext
    }
    return null
}

/**
 * 查找当前上下文activity
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:38
 * @version 1.0
 */
fun Any?.findActivity(context: Context?): Activity? {
    return getActivity(context)
}

/**
 * 判断activity是否销毁
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Context?.isDestroy(): Boolean {
    return isDestroy(findActivity(this))
}

/**
 * 判断activity是否关闭
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isFinish(context: Context): Boolean {
    return isFinish(findActivity(context))
}

/**
 * 判断activity是否销毁
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Activity?.isDestroy(): Boolean {
    return this == null || this.isDestroyed
}

/**
 * 判断activity是否销毁
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isDestroy(activity: Activity?): Boolean {
    return activity == null || activity.isDestroyed
}

/**
 * 判断activity是否关闭
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isFinish(activity: Activity?): Boolean {
    return activity == null || activity.isFinishing
}

inline fun <reified T : Fragment?> FragmentActivity.newFragment(clazz: Class<T>, args: Bundle?): T? {
    if (classLoader == null) {
        return null
    }
    val factory: FragmentFactory = supportFragmentManager.fragmentFactory
    val fragment: Fragment = factory.instantiate(classLoader, clazz.name)
    fragment.arguments = args
    return if (fragment is T) fragment else null
}