package com.fz.common.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.fz.common.view.utils.dip2px
import java.io.IOException
import java.io.InputStream

internal fun Any?.getResource(): Resources {
    val context: Context? = checkContext(this)
    checkNotNull(context, "请主动调用ContextUtils.initContext()")
    return context.resources
}

fun Any?.getNameWithResId(@IdRes resourceId: Int): String? {
    return try {
        getResource().getResourceName(resourceId)
    } catch (e: Exception) {
        null
    }
}

fun Any?.getIdWithName(resourceTypeName: String, resourceName: String): Int {
    val context: Context? = checkContext(this)
    checkNotNull(context, "请主动调用ContextUtils.initContext()")
    return context.resources.getIdentifier(
            resourceName,
            resourceTypeName,
            context.packageName
    )
}

fun Any?.getLayoutResIdWithName(resourceName: String): Int {
    return getIdWithName("layout", resourceName)
}

fun Any?.getStringResIdWithName(resourceName: String): Int {
    return getIdWithName("string", resourceName)
}

fun Any?.getString(context: Context, @StringRes resourceId: Int): String {
    return context.getString(resourceId)
}

fun Any?.getString(context: Context, @StringRes resourceId: Int, vararg formatArgs: Any?): String {
    return context.getString(resourceId, *formatArgs)
}

fun Any?.getString(@StringRes resourceId: Int): String {
    return getResource().getString(resourceId)
}

fun Any?.getStringArray(@ArrayRes resourceId: Int): Array<String> {
    return getResource().getStringArray(resourceId)
}

fun Any?.getString(resourceName: String): String {
    return getResource().getString(getIdWithName(resourceName, "string"))
}

fun Any?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    return getResource().getString(resourceId, *formatArgs) ?: ""
}

fun Any?.getString(resourceName: String, vararg formatArgs: Any?): String {
    return getResource().getString(getIdWithName(resourceName, "string"), *formatArgs)
}

fun Any?.getColor(@ColorRes resourceId: Int): Int {
    val context: Context? = checkContext(this)
    checkNotNull(context, "请主动调用ContextUtils.initContext()")
    return getColor(context, resourceId)
}

fun Any?.getColor(context: Context?, @ColorRes resourceId: Int): Int {
    checkNotNull(context)
    return ContextCompat.getColor(context, resourceId)
}

fun Any?.openAssetsFile(fileName: String): InputStream? {
    try {
        return getResource().assets?.open(fileName)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun Any?.openAssetsFile(fileName: String, accessMode: Int): InputStream? {
    try {
        return getResource().assets?.open(fileName, accessMode)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun Any?.getAssetsFiles(dir: String): Array<String>? {
    try {
        return getResource().assets?.list(dir)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

/**
 * 获取dimen
 *
 * @param resId 尺寸资源id
 * @return
 */
fun Any?.getDimens(@DimenRes resId: Int): Int {
    val value = TypedValue()
    try {
        getResource().getValue(resId, value, true)
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return dip2px(TypedValue.complexToFloat(value.data))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return getResource().getDimensionPixelSize(resId)
}

/**
 * 获取drawable
 *
 * @param resId 图片的资源id
 * @return drawable对象
 */
fun Any?.getDrawable(@DrawableRes resId: Int): Drawable? {
    val context: Context? = checkContext(this)
    checkNotNull(context, "请主动调用ContextUtils.initContext()")
    return getDrawable(context, resId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Any?.getResourceId(context: Context, attrId: Int): Int {
    val ta = context.obtainStyledAttributes(intArrayOf(attrId))
    val resourceId = ta.getResourceId(0, -1)
    ta.recycle()
    return resourceId
}

/**
 * 获取drawable
 *
 * @param resId 图片的资源id
 * @return drawable对象
 */
fun Any?.getDrawable(context: Context?, @DrawableRes resId: Int): Drawable? {
    checkNotNull(context)
    return if (AppCompatDelegate.isCompatVectorFromResourcesEnabled()) {
        AppCompatResources.getDrawable(context, resId)
    } else ContextCompat.getDrawable(context, resId)
}