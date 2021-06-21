package com.fz.common.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fz.common.entity.IHttpResponse
import com.fz.common.utils.eLog
import com.fz.common.utils.getStackTraceMessage
import com.fz.common.utils.isSuccessFull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel 状态记录
 * @author dingpeihua
 * @date 2021/2/20 11:28
 * @version 1.0
 */
class ViewModelState<T> {
    var data: T? = null
    var error: Throwable? = null
    var state: Int = STARTING


    /**
     * 失败
     */
    fun isStarting(): Boolean {
        return state == STARTING
    }

    /**
     * 成功
     */
    fun isSuccess(): Boolean {
        return state == SUCCESS
    }

    /**
     * 错误
     */
    fun isError(): Boolean {
        return state == ERROR
    }

    companion object {
        const val STARTING = 11
        const val SUCCESS = 12
        const val ERROR = 13
    }
}

/**
 * 异常转换异常处理
 */
fun <T> ViewModelState<T>.parseStaring(liveData: MutableLiveData<ViewModelState<T>>) {
    this.state = ViewModelState.STARTING
    liveData.value = this
}

/**
 * 处理返回值
 *
 * @param result 请求结果
 */
fun <T> ViewModelState<T>.parseResponse(liveData: MutableLiveData<ViewModelState<T>>, result: T?) {
    this.data = result
    this.state = ViewModelState.SUCCESS
    liveData.value = this
}

/**
 * 处理返回值
 *
 * @param result 请求结果
 */
fun <T> ViewModelState<T>.parseResult(liveData: MutableLiveData<ViewModelState<T>>, result: IHttpResponse<T?>?) {
    liveData.value = if (result.isSuccessFull()) {
        this.state = ViewModelState.SUCCESS
        this.data = result.getData()
        this
    } else {
        this.state = ViewModelState.ERROR
        this.error = Throwable(result?.msg ?: "Unknown")
        this.data = null
        this
    }
}

/**
 * 异常转换异常处理
 */
fun <T> ViewModelState<T>.parseError(liveData: MutableLiveData<ViewModelState<T>>, e: Throwable) {
    this.state = ViewModelState.ERROR
    this.error = e
    this.data = null
    liveData.value = this
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.apiRequest(apiDSL: ApiModel<T>.() -> Unit) {
    ApiModel<T>().apply(apiDSL).syncLaunch(viewModelScope)
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.apiRequest(
        viewState: MutableLiveData<ViewModelState<T>>,
        apiDSL: ApiModel<T>.() -> Unit,
) {
    ApiModel<T>().apply(apiDSL)
            .parseMethod(viewState)
            .syncLaunch(viewModelScope)

}

internal fun <T> ApiModel<T>.parseMethod(viewState: MutableLiveData<ViewModelState<T>>): ApiModel<T> {
    val state = ViewModelState<T>()
    if (!isOnStart()) {
        onStart { state.parseStaring(viewState) }
    }
    if (!isOnError()) {
        onError { state.parseError(viewState, it) }
    }
    if (!isOnResponse()) {
        onResponse { state.parseResponse(viewState, it) }
    }
    return this
}

internal fun <T> ApiModel<IHttpResponse<T?>>.parseMethodLimit(viewState: MutableLiveData<ViewModelState<T>>): ApiModel<IHttpResponse<T?>> {
    val state = ViewModelState<T>()
    if (!isOnStart()) {
        onStart { state.parseStaring(viewState) }
    }
    if (!isOnError()) {
        onError { state.parseError(viewState, it) }
    }
    if (!isOnResponse()) {
        onResponse { state.parseResult(viewState, it) }
    }
    return this
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.apiRequestLimit(
        viewState: MutableLiveData<ViewModelState<T>>,
        apiDSL: ApiModel<IHttpResponse<T?>>.() -> Unit,
) {
    ApiModel<IHttpResponse<T?>>()
            .apply(apiDSL)
            .parseMethodLimit(viewState)
            .syncLaunch(viewModelScope)
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.request(
        viewState: MutableLiveData<ViewModelState<T>>,
        request: suspend CoroutineScope.() -> T?,
) {
    viewModelScope.launch(Dispatchers.Main) {
        val state = ViewModelState<T>()
        state.parseStaring(viewState)
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            state.parseResponse(viewState, response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            state.parseError(viewState, e)
        }
    }
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.requestLimit(
        viewState: MutableLiveData<ViewModelState<T>>,
        request: suspend CoroutineScope.() -> IHttpResponse<T?>?,
) {
    viewModelScope.launch(Dispatchers.Main) {
        val state = ViewModelState<T>()
        state.parseStaring(viewState)
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            state.parseResult(viewState, response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            state.parseError(viewState, e)
        }
    }
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.request(
        request: suspend CoroutineScope.() -> T,
        onSuccess: (T) -> Unit,
        onStart: () -> Unit = { },
        onError: (Throwable) -> Unit = { },
        onComplete: () -> Unit = {},
) {
    viewModelScope.launch(Dispatchers.Main) {
        onStart()
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            onSuccess(response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            onError(e)
        } finally {
            onComplete()
        }
    }
}