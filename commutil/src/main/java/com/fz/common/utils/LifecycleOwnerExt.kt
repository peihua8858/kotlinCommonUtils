package com.fz.common.utils

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.fz.common.model.ApiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.fz.common.coroutine.runCatching

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.STARTED]状态时，运行给定的块[block]。
 * @param block 阻塞执行代码块
 * @param onError 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> LifecycleOwner.launchWithStart(
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
fun <T> LifecycleOwner.apiWithLaunchStarted(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.apiWithLaunchStarted(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.launchWhenCreated(
        block: suspend CoroutineScope.() -> T,
        callback: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onComplete: () -> Unit = {},
): Job {
    return lifecycleScope.launch {
        whenCreated { com.fz.common.coroutine.runCatching(block, callback, onError, complete = onComplete) }
    }
}

/**
 * 当[LifecycleOwner]的[Lifecycle]至少处于[Lifecycle.State.CREATED]状态时，运行给定的块[api]。
 * @param api 错误回调
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun <T> LifecycleOwner.apiWithLaunchCreated(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.apiWithLaunchCreated(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.launchWhenResumed(
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
fun <T> LifecycleOwner.apiWithLaunchResumed(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.apiWithLaunchResumed(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.launch(
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
fun <T> LifecycleOwner.apiWithLaunch(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.apiWithLaunch(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.asyncWhenCreated(
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
fun <T> LifecycleOwner.apiWithAsyncCreated(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.apiWithAsyncCreated(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.asyncWhenStart(
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
fun <T> LifecycleOwner.apiWithAsyncStarted(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.apiWithAsyncStarted(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.asyncWhenResumed(
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
fun <T> LifecycleOwner.apiWithAsyncResumed(api: ApiModel<T>.() -> Unit): Job {
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
fun <T> LifecycleOwner.apiWithAsyncResumed(manager: FragmentManager? = null, isShowDialog: Boolean = true, api: ApiModel<T>.() -> Unit): Job {
    return lifecycleScope.launch {
        whenResumed {
            ApiModel<T>(manager, isShowDialog).apply(api).launch()
        }
    }
}