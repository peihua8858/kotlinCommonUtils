@file:JvmName("FragmentUtil")
@file:JvmMultifileClass
package com.fz.common.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.fz.common.model.ApiModel
import com.fz.common.coroutine.runCatching
import com.fz.common.utils.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

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
fun Fragment?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    val context: Context = checkContext(this) ?: return ""
    return context.getString(resourceId, *formatArgs)
}

fun Fragment?.getString(@StringRes resourceId: Int): String {
    val context: Context = checkContext(this) ?: return ""
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


/**
 * 创建协程上下文并处理回调异常到[onError]
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun Fragment.createHandler(onError: (CoroutineContext, Throwable) -> Unit): CoroutineContext {
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
fun <T> Fragment.launchWithStart(
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
fun <T> Fragment.apiWithLaunchStarted(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.apiWithLaunchStarted(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.launchWhenCreated(
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
fun <T> Fragment.apiWithLaunchCreated(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.apiWithLaunchCreated(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.launchWhenResumed(
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
fun <T> Fragment.apiWithLaunchResumed(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.apiWithLaunchResumed(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.launch(
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
fun <T> Fragment.apiWithLaunch(api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        ApiModel<T>().apply(api).launch()
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
fun <T> Fragment.apiWithLaunch(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.asyncWhenCreated(
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
fun <T> Fragment.apiWithAsyncCreated(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.apiWithAsyncCreated(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.asyncWhenStart(
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
fun <T> Fragment.apiWithAsyncStarted(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.apiWithAsyncStarted(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.asyncWhenResumed(
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
fun <T> Fragment.apiWithAsyncResumed(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> Fragment.apiWithAsyncResumed(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenResumed {
            ApiModel<T>(manager, isShowDialog).apply(api).launch()
        }
    }
}