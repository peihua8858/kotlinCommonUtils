@file:JvmName("ResourceUtil")
@file:JvmMultifileClass
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
import kotlin.math.roundToInt

internal fun Any?.getResource(): Resources? {
    val context: Context = checkContext(this) ?: return null.eLog { "Context  is null." }
    return context.resources
}

fun Any?.getNameWithResId(@IdRes resourceId: Int): String? {
    return try {
        val resource = getResource() ?: return "".eLog { "Resource  is null." } ?: ""
        resource.getResourceName(resourceId)
    } catch (e: Exception) {
        null
    }
}

fun Any?.getIdWithName(resourceTypeName: String, resourceName: String): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
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
    val resource = getResource() ?: return "".eLog { "Resource  is null." } ?: ""
    return resource.getString(resourceId)
}

fun Any?.getStringArray(@ArrayRes resourceId: Int): Array<String> {
    val d = arrayOf<String>()
    val resource = getResource() ?: return d.eLog { "Resource  is null." } ?: d
    return resource.getStringArray(resourceId)
}

fun Any?.getString(resourceName: String): String {
    return getResource().getString(getIdWithName(resourceName, "string"))
}

fun Any?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    val resource = getResource() ?: return "".eLog { "Resource  is null." } ?: ""
    return resource.getString(resourceId, *formatArgs) ?: ""
}

fun Any?.getString(resourceName: String, vararg formatArgs: Any?): String {
    return getResource().getString(getIdWithName(resourceName, "string"), *formatArgs)
}

fun Any?.getColor(@ColorRes resourceId: Int, @ColorInt defaultColor: Int): Int {
    val context: Context = checkContext(this) ?: return defaultColor.eLog { "Context  is null." }
    return getColor(context, resourceId)
}

fun Any?.getColor(@ColorRes resourceId: Int): Int {
    return getColor(resourceId, 0)
}

fun Any?.getColor(context: Context, @ColorRes resourceId: Int): Int {
    return ContextCompat.getColor(context, resourceId)
}

fun Any?.openAssetsFile(fileName: String): InputStream? {
    try {
        return getResource()?.assets?.open(fileName)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun Any?.openAssetsFile(fileName: String, accessMode: Int): InputStream? {
    try {
        val resource = getResource() ?: return null.eLog { "Resource  is null." }
        return resource.assets?.open(fileName, accessMode)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun Any?.getAssetsFiles(dir: String): Array<String>? {
    try {
        val resource = getResource() ?: return null.eLog { "Resource  is null." }
        return resource.assets?.list(dir)
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
fun Context.getDimens(@DimenRes resId: Int): Int {
    val value = TypedValue()
    val resource = getResource() ?: return 0.eLog { "Resource  is null." }
    try {
        resource.getValue(resId, value, true)
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return resource.dip2px(TypedValue.complexToFloat(value.data)).roundToInt()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return resource.getDimensionPixelSize(resId)
}

/**
 * 获取dimen
 *
 * @param resId 尺寸资源id
 * @return
 */
fun Any?.getDimens(@DimenRes resId: Int): Int {
    val value = TypedValue()
    val resource = getResource() ?: return 0.eLog { "Resource  is null." }
    try {
        resource.getValue(resId, value, true)
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return resource.dip2px(TypedValue.complexToFloat(value.data)).roundToInt()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return resource.getDimensionPixelSize(resId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Any?.getResourceId(context: Context, attrId: Int): Int {
    return context.getResourceId(attrId)
}

/**
 * 获取drawable
 *
 * @param resId 图片的资源id
 * @return drawable对象
 */
fun Any?.getDrawable(@DrawableRes resId: Int): Drawable? {
    val context: Context = checkContext(this) ?: return null.eLog { "Context  is null." }
    return if (AppCompatDelegate.isCompatVectorFromResourcesEnabled()) {
        AppCompatResources.getDrawable(context, resId)
    } else ContextCompat.getDrawable(context, resId)
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
fun Any?.resolveAttribute(
    context: Context,
    resId: Int,
    @StyleRes defaultRes: Int,
): Int {
    val resourceId = resolveAttribute(context, resId)
    return if (resourceId != 0) {
        resourceId
    } else defaultRes
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
fun Any?.resolveAttribute(context: Context, resId: Int): Int {
    val outValue = TypedValue()
    context.theme.resolveAttribute(resId, outValue, true)
    return outValue.resourceId
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
fun Context?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return resolveAttribute(context, resId, defaultRes)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Context.getResourceId(attrId: Int): Int {
    val ta = obtainStyledAttributes(intArrayOf(attrId))
    val resourceId = ta.getResourceId(0, -1)
    ta.recycle()
    return resourceId
}

fun Any?.getColorCompat(@ColorRes resId: Int): Int {
    val context: Context = checkContext(this) ?: return (-1).eLog { "Context  is null." }
    return ContextCompat.getColor(context, resId)
}

fun Any?.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    val context: Context = checkContext(this) ?: return null.eLog { "Context  is null." }
    return ContextCompat.getDrawable(context, resId)
}

fun Any?.getDimensionPixelOffset(@DimenRes resId: Int): Int {
    val resources = getResource()
    return resources?.getDimensionPixelOffset(resId) ?: 0
}