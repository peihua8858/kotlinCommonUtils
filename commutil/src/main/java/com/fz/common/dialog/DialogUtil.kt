@file:JvmName("DialogUtil")

package com.fz.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import com.fz.common.model.ApiModel
import com.fz.common.coroutine.runCatching
import com.fz.common.utils.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@ColorInt
fun Dialog?.getColor(@ColorRes colorRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return ContextCompat.getColor(context, colorRes)
}

fun Dialog?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context = checkContext(this) ?: return null.eLog { "Context  is null." }
    return ContextCompat.getDrawable(context, drawableRes)
}

fun Dialog?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return context.getDimens(resId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Dialog?.getResourceId(attrId: Int): Int {
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
fun Dialog?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
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
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.STARTED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> DialogFragment.launchWithStart(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
) {
    lifecycleScope.launch {
        whenStarted { runCatching(block, callback, onError,complete = onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> DialogFragment.apiWithLaunchStarted(api: ApiModel<T>.() -> Unit) {
    lifecycleScope.launch {
        whenStarted {
            ApiModel<T>().apply(api).syncLaunch()
        }
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
fun <T> DialogFragment.launchWhenCreated(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
) {
    lifecycleScope.launch {
        whenCreated { runCatching(block, callback, onError,complete = onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> DialogFragment.apiWithLaunchCreated(api: ApiModel<T>.() -> Unit) {
    lifecycleScope.launch {
        whenCreated {
            ApiModel<T>().apply(api).syncLaunch()
        }
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
fun <T> DialogFragment.launchWhenResumed(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
) {
    lifecycleScope.launch {
        whenResumed { runCatching(block, callback, onError,complete = onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> DialogFragment.apiWithLaunchResumed(api: ApiModel<T>.() -> Unit) {
    lifecycleScope.launch {
        whenResumed {
            ApiModel<T>().apply(api).syncLaunch()
        }
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
fun <T> DialogFragment.launch(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
) {
    lifecycleScope.launch {
        runCatching(block, callback, onError,complete = onComplete)
    }
}

/**
 * 异步协程
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> DialogFragment.apiWithLaunch(api: ApiModel<T>.() -> Unit) {
    lifecycleScope.launch {
        ApiModel<T>().apply(api).launch()
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
fun <T> DialogFragment.asyncWhenCreated(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
) {
    lifecycleScope.launch {
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
fun <T> DialogFragment.apiWithAsyncCreated(api: ApiModel<T>.() -> Unit) {
    lifecycleScope.launch {
        whenCreated {
            ApiModel<T>().apply(api).launch()
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
fun <T> DialogFragment.asyncWhenStart(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
) {
    lifecycleScope.launch {
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
fun <T> DialogFragment.apiWithAsyncStarted(api: ApiModel<T>.() -> Unit) {
    lifecycleScope.launch {
        whenStarted {
            ApiModel<T>().apply(api).launch()
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
fun <T> DialogFragment.asyncWhenResumed(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
) {
    lifecycleScope.launch {
        whenResumed { runCatching(block, callback, onError, Dispatchers.IO, onComplete) }
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
fun <T> DialogFragment.apiWithAsyncResumed(api: ApiModel<T>.() -> Unit) {
    lifecycleScope.launch {
        whenResumed {
            ApiModel<T>().apply(api).launch()
        }
    }
}