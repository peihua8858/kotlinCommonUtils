@file:JvmName("DialogUtil")
@file:JvmMultifileClass

package com.peihua8858.android.tools.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.peihua8858.tools.ContextInitializer
import com.peihua8858.tools.utils.getDimens
import com.peihua8858.tools.utils.getResourceId
import com.peihua8858.tools.utils.startNotificationSettings
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext
import com.peihua8858.tools.utils.resolveAttribute

@ColorInt
fun Dialog?.getColor(@ColorRes colorRes: Int): Int {
    val context: Context = this?.context ?: ContextInitializer.context
    return ContextCompat.getColor(context, colorRes)
}

fun Dialog?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context = this?.context ?: ContextInitializer.context
    return ContextCompat.getDrawable(context, drawableRes)
}

fun Dialog?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = this?.context ?: ContextInitializer.context
    return context.getDimens(resId)
}

fun Dialog?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    val context: Context = this?.context ?: ContextInitializer.context
    return context.getString(resourceId, *formatArgs)
}

fun Dialog?.getString(@StringRes resourceId: Int): String {
    val context: Context = this?.context ?: ContextInitializer.context
    return context.getString(resourceId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Dialog?.getResourceId(attrId: Int): Int {
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
fun Dialog?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
    val context: Context = this?.context ?: ContextInitializer.context
    return resolveAttribute(context, resId, defaultRes)
}

/**
 * 创建协程上下文并处理回调异常到[onError]
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun DialogFragment.createHandler(onError: (CoroutineContext, Throwable) -> Unit): CoroutineContext {
    return CoroutineExceptionHandler { context, e -> onError(context, e) }
}

/**
 * 跳转到通知设置页面
 *
 * @author dingpeihua
 * @date 2019/12/17 16:27
 * @version 1.0
 */
fun Dialog.startNotificationSettings() {
    context.startNotificationSettings()
}