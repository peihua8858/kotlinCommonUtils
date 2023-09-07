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
sealed class Result<T> {
    data class Stating<T>(val message: String = "") : Result<T>()
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val e: Throwable) : Result<T>()
}

/**
 * 处理返回值
 *
 * @param result 请求结果
 */
fun <T> MutableLiveData<Result<T?>>.parseResult(result: IHttpResponse<T?>?) {
    postValue(
        if (result.isSuccessFull()) {
            Result.Success(result.getData())
        } else {
            Result.Failure(Throwable(result?.msg ?: "Unknown"))
        }
    )
}

/**
 * [ViewModel]在主线程中开启协程扩展
 */
fun <T> ViewModel.apiSyncRequest(
    viewState: MutableLiveData<Result<T>>,
    apiDSL: ApiModel<T>.() -> Unit,
) {
    ApiModel<T>().apply(apiDSL)
        .parseMethod(viewState)
        .syncLaunch(viewModelScope)

}

/**
 * [ViewModel]在IO线程中开启协程扩展
 */
fun <T> ViewModel.apiRequest(
    viewState: MutableLiveData<Result<T>>,
    apiDSL: ApiModel<T>.() -> Unit,
) {
    ApiModel<T>().apply(apiDSL)
        .parseMethod(viewState)
        .launch(viewModelScope)

}

internal fun <T> ApiModel<T>.parseMethod(viewState: MutableLiveData<Result<T>>): ApiModel<T> {
    if (!isOnStart()) {
        onStart { viewState.postValue(Result.Stating()) }
    }
    if (!isOnError()) {
        onError { viewState.postValue(Result.Failure(it)) }
    }
    if (!isOnResponse()) {
        onResponse { viewState.postValue(Result.Success(it)) }
    }
    return this
}

internal fun <T> ApiModel<IHttpResponse<T?>>.parseMethodLimit(viewState: MutableLiveData<Result<T?>>): ApiModel<IHttpResponse<T?>> {
    if (!isOnStart()) {
        onStart { viewState.postValue(Result.Stating()) }
    }
    if (!isOnError()) {
        onError { viewState.postValue(Result.Failure(it)) }
    }
    if (!isOnResponse()) {
        onResponse { viewState.postValue(Result.Success(it.getData())) }
    }
    return this
}

/**
 * [ViewModel]在主线程中开启协程扩展
 */
fun <T> ViewModel.apiSyncRequestLimit(
    viewState: MutableLiveData<Result<T?>>,
    apiDSL: ApiModel<IHttpResponse<T?>>.() -> Unit,
) {
    ApiModel<IHttpResponse<T?>>()
        .apply(apiDSL)
        .parseMethodLimit(viewState)
        .syncLaunch(viewModelScope)
}

/**
 * [ViewModel]在IO线程中开启协程扩展
 */
fun <T> ViewModel.apiRequestLimit(
    viewState: MutableLiveData<Result<T?>>,
    apiDSL: ApiModel<IHttpResponse<T?>>.() -> Unit,
) {
    ApiModel<IHttpResponse<T?>>()
        .apply(apiDSL)
        .parseMethodLimit(viewState)
        .launch(viewModelScope)
}

/**
 * [ViewModel]在IO线程中开启协程扩展
 */
fun <T> ViewModel.request(
    viewState: MutableLiveData<Result<T?>>,
    request: suspend CoroutineScope.() -> T?,
) {
    viewModelScope.launch(Dispatchers.Main) {
        viewState.postValue(Result.Stating())
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            viewState.postValue(Result.Success(response))
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            viewState.postValue(Result.Failure(e))
        }
    }
}

/**
 * [ViewModel]在IO线程中开启协程扩展
 */
fun <T> ViewModel.requestLimit(
    viewState: MutableLiveData<Result<T?>>,
    request: suspend CoroutineScope.() -> IHttpResponse<T?>?,
) {
    viewModelScope.launch(Dispatchers.Main) {
        viewState.postValue(Result.Stating())
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            viewState.parseResult(response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            viewState.postValue(Result.Failure(e))
        }
    }
}