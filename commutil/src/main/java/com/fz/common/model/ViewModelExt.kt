package com.fz.common.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fz.common.entity.IHttpResponse
import com.fz.common.utils.eLog
import com.fz.common.utils.getStackTraceMessage
import com.fz.common.utils.isSuccessFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel 状态记录
 * @author dingpeihua
 * @date 2021/2/20 11:28
 * @version 1.0
 */
sealed class ViewModelState<out T> {
    object Starting : ViewModelState<Nothing>()
    data class Success<out T>(val data: T?) : ViewModelState<T>()
    data class Error(val error: Throwable) : ViewModelState<Nothing>()
    object Complete : ViewModelState<Nothing>()
}

/**
 * 处理返回值
 *
 * @param result 请求结果
 */
fun <T> MutableLiveData<ViewModelState<T>>.parseResponse(result: T?) {
    value = ViewModelState.Success(result)
}

/**
 * 处理返回值
 *
 * @param result 请求结果
 */
fun <T> MutableLiveData<ViewModelState<T>>.parseResult(result: IHttpResponse<T?>?) {
    value = if (result.isSuccessFull()) ViewModelState.Success(result.getData()) else
        ViewModelState.Error(Throwable(result?.msg ?: "Unknown"))
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ViewModelState<T>>.parseError(e: Throwable) {
    this.value = ViewModelState.Error(e)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ViewModelState<T>>.parseComplete() {
    this.value = ViewModelState.Complete
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ViewModelState<T>>.parseStaring() {
    this.value = ViewModelState.Starting
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
    if (!isOnStart()) {
        onStart { viewState.parseStaring() }
    }
    if (!isOnError()) {
        onError { viewState.parseError(it) }
    }
    if (!isOnResponse()) {
        onResponse { viewState.parseResponse(it) }
    }
    if (!isOnComplete()) {
        onComplete { viewState.parseComplete() }
    }
    return this
}

internal fun <T> ApiModel<IHttpResponse<T?>>.parseMethodLimit(viewState: MutableLiveData<ViewModelState<T>>): ApiModel<IHttpResponse<T?>> {
    if (!isOnStart()) {
        onStart { viewState.parseStaring() }
    }
    if (!isOnError()) {
        onError { viewState.parseError(it) }
    }
    if (!isOnResponse()) {
        onResponse { viewState.parseResult(it) }
    }
    if (!isOnComplete()) {
        onComplete { viewState.parseComplete() }
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
        request: suspend () -> T?,
) {
    viewModelScope.launch(Dispatchers.Main) {
        viewState.parseStaring()
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            viewState.parseResponse(response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            viewState.parseError(e)
        } finally {
            viewState.parseComplete()
        }
    }
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.requestLimit(
        viewState: MutableLiveData<ViewModelState<T>>,
        request: suspend () -> IHttpResponse<T?>?,
) {
    viewModelScope.launch(Dispatchers.Main) {
        viewState.parseStaring()
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            viewState.parseResult(response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            viewState.parseError(e)
        } finally {
            viewState.parseComplete()
        }
    }
}

/**
 * [ViewModel]开启协程扩展
 */
fun <T> ViewModel.request(
        request: suspend () -> T,
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