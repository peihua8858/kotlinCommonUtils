@file:JvmName("FragmentUtil")
@file:JvmMultifileClass

package com.peihua8858.android.tools.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.peihua8858.tools.ContextInitializer
import com.peihua8858.tools.utils.getDimens
import com.peihua8858.tools.utils.getResourceId
import com.peihua8858.tools.utils.startNotificationSettings
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext
import com.peihua8858.tools.utils.resolveAttribute

@ColorInt
fun Fragment?.getColor(@ColorRes colorRes: Int): Int {
    val context = this?.context ?: ContextInitializer.context
    return ContextCompat.getColor(context, colorRes)
}

fun Fragment?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context = this?.context ?: ContextInitializer.context
    return ContextCompat.getDrawable(context, drawableRes)
}

fun Fragment?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = this?.context ?: ContextInitializer.context
    return context.getDimens(resId)
}
fun Fragment?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    val context: Context = this?.context ?: ContextInitializer.context
    return context.getString(resourceId, *formatArgs)
}

fun Fragment?.getString(@StringRes resourceId: Int): String {
    val context: Context = this?.context ?: ContextInitializer.context
    return context.getString(resourceId)
}
/**
 * 跳转到通知设置页面
 *
 * @author dingpeihua
 * @date 2019/12/17 16:27
 * @version 1.0
 */
fun Fragment.startNotificationSettings() {
    context?.startNotificationSettings()
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Fragment?.getResourceId(attrId: Int): Int {
    val context: Context = this?.context ?: ContextInitializer.context
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
    val context: Context = this?.context ?: ContextInitializer.context
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
    val context: Context = this?.context ?: ContextInitializer.context
    return context.resolveAttribute( resId)
}


/**
 * 创建协程上下文并处理回调异常到[onError]
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun Fragment.createHandler(onError: (CoroutineContext, Throwable) -> Unit): CoroutineContext {
    return CoroutineExceptionHandler { context, e -> onError(context, e) }
}
