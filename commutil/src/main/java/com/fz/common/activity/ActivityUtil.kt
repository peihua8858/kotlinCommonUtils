@file:JvmName("ActivityUtil")

package com.fz.common.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.fz.common.model.ApiModel
import com.fz.common.coroutine.runCatching
import com.fz.common.utils.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

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

fun Activity?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    val context: Context = checkContext(this) ?: return ""
    return context.getString(resourceId, *formatArgs)
}

fun Activity?.getString(@StringRes resourceId: Int): String {
    val context: Context = checkContext(this) ?: return ""
    return context.getString(resourceId)
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

/**
 * 创建协程上下文并处理回调异常到[onError]
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun ComponentActivity.createHandler(onError: (CoroutineContext, Throwable) -> Unit): CoroutineContext {
    return CoroutineExceptionHandler { context, e -> onError(context, e) }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.STARTED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.launchWithStart(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        whenStarted { runCatching(block, callback, onError, complete = onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunchStarted(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenStarted {
            ApiModel<T>().apply(api).syncLaunch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[api]。
 * @param isShowDialog 是否显示loading 弹窗
 * @param manager fragment 管理器
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunchStarted(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenStarted { ApiModel<T>(manager, isShowDialog).apply(api).syncLaunch() }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.launchWhenCreated(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        whenCreated { runCatching(block, callback, onError, complete = onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunchCreated(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenCreated {
            ApiModel<T>().apply(api).syncLaunch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[api]。
 * @param isShowDialog 是否显示loading 弹窗
 * @param manager fragment 管理器
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunchCreated(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenCreated { ApiModel<T>(manager, isShowDialog).apply(api).syncLaunch() }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.launchWhenResumed(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        whenResumed { runCatching(block, callback, onError, complete = onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunchResumed(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenResumed {
            ApiModel<T>().apply(api).syncLaunch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[api]。
 * @param isShowDialog 是否显示loading 弹窗
 * @param manager fragment 管理器
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunchResumed(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenResumed { ApiModel<T>(manager, isShowDialog).apply(api).syncLaunch() }
    }
}

/**
 * 异步协程
 * @param block 阻塞执行代码块
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.launch(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        runCatching(block, callback, onError, complete = onComplete)
    }
}

/**
 * 异步协程
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunch(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        ApiModel<T>().apply(api).syncLaunch()
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[api]。
 * @param isShowDialog 是否显示loading 弹窗
 * @param manager fragment 管理器
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithLaunch(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        ApiModel<T>(manager, isShowDialog).apply(api).syncLaunch()
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param callback 结果回调
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.asyncWhenCreated(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        whenCreated { runCatching(block, callback, onError, Dispatchers.IO, onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithAsyncCreated(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenCreated {
            ApiModel<T>().apply(api).launch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[api]。
 * @param isShowDialog 是否显示loading 弹窗
 * @param manager fragment 管理器
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithAsyncCreated(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenCreated {
            ApiModel<T>(manager, isShowDialog).apply(api).launch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.STARTED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param callback 结果回调
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.asyncWhenStart(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        whenStarted { runCatching(block, callback, onError, Dispatchers.IO, onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.STARTED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithAsyncStarted(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenStarted {
            ApiModel<T>().apply(api).launch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[api]。
 * @param isShowDialog 是否显示loading 弹窗
 * @param manager fragment 管理器
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithAsyncStarted(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenStarted {
            ApiModel<T>(manager, isShowDialog).apply(api).launch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param callback 结果回调
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.asyncWhenResumed(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        whenResumed { runCatching(block, callback, onError, Dispatchers.IO, onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[block]。
 * @param api 阻塞执行代码块
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithAsyncResumed(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenResumed {
            ApiModel<T>().apply(api).launch()
        }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.RESUMED]状态时，运行给定的块[api]。
 * @param isShowDialog 是否显示loading 弹窗
 * @param manager fragment 管理器
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> ComponentActivity.apiWithAsyncResumed(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenResumed {
            ApiModel<T>(manager, isShowDialog).apply(api).launch()
        }
    }
}