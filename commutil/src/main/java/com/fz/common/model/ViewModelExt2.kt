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
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * ViewModel 状态记录
 * @author dingpeihua
 * @date 2021/2/20 11:28
 * @version 1.0
 */
sealed class ResultData<T> {
    class Starting<T> : ResultData<T>()
    data class Success<T>(val data: T) : ResultData<T>()
    data class Failure<T>(val error: Throwable) : ResultData<T>()
}

@OptIn(ExperimentalContracts::class)
fun <T> ResultData<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is ResultData.Success)
    }
    return this is ResultData.Success
}
@OptIn(ExperimentalContracts::class)
fun <T> ResultData<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is ResultData.Failure)
    }
    return this is ResultData.Failure
}
@OptIn(ExperimentalContracts::class)
fun <T> ResultData<T>.isStarting(): Boolean {
    contract {
        returns(true) implies (this@isStarting is ResultData.Starting)
    }
    return this is ResultData.Starting
}
/**
 * 处理返回值
 *
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultData<T>>.parseResult(result: IHttpResponse<T>?) {
    postValue(
        if (result.isSuccessFull()) {
            ResultData.Success(result.getData())
        } else {
            ResultData.Failure(Throwable(result?.msg ?: "Unknown"))
        }
    )
}

/**
 * [ViewModel]在主线程中开启协程扩展
 */
fun <T> ViewModel.apiSyncRequest(
    viewState: MutableLiveData<ResultData<T>>,
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
    viewState: MutableLiveData<ResultData<T>>,
    apiDSL: ApiModel<T>.() -> Unit,
) {
    ApiModel<T>().apply(apiDSL)
        .parseMethod(viewState)
        .launch(viewModelScope)

}

internal fun <T> ApiModel<T>.parseMethod(viewState: MutableLiveData<ResultData<T>>): ApiModel<T> {
    if (!isOnStart()) {
        onStart { viewState.postValue(ResultData.Starting()) }
    }
    if (!isOnError()) {
        onError { viewState.postValue(ResultData.Failure(it)) }
    }
    if (!isOnResponse()) {
        onResponse { viewState.postValue(ResultData.Success(it)) }
    }
    return this
}

internal fun <T> ApiModel<IHttpResponse<T>?>.parseMethodLimit(viewState: MutableLiveData<ResultData<T>>): ApiModel<IHttpResponse<T>?> {
    if (!isOnStart()) {
        onStart { viewState.postValue(ResultData.Starting()) }
    }
    if (!isOnError()) {
        onError { viewState.postValue(ResultData.Failure(it)) }
    }
    if (!isOnResponse()) {
        onResponse {
            if (it.isSuccessFull()) {
                ResultData.Success(it.getData())
            } else {
                ResultData.Failure(Throwable(it?.msg ?: "Unknown"))
            }
        }
    }
    return this
}

/**
 * [ViewModel]在主线程中开启协程扩展
 */
fun <T> ViewModel.apiSyncRequestLimit(
    viewState: MutableLiveData<ResultData<T>>,
    apiDSL: ApiModel<IHttpResponse<T>?>.() -> Unit,
) {
    ApiModel<IHttpResponse<T>?>()
        .apply(apiDSL)
        .parseMethodLimit(viewState)
        .syncLaunch(viewModelScope)
}

/**
 * [ViewModel]在IO线程中开启协程扩展
 */
fun <T> ViewModel.apiRequestLimit(
    viewState: MutableLiveData<ResultData<T>>,
    apiDSL: ApiModel<IHttpResponse<T>?>.() -> Unit,
) {
    ApiModel<IHttpResponse<T>?>()
        .apply(apiDSL)
        .parseMethodLimit(viewState)
        .launch(viewModelScope)
}

/**
 * [ViewModel]在IO线程中开启协程扩展
 */
fun <T> ViewModel.request(
    viewState: MutableLiveData<ResultData<T>>,
    request: suspend CoroutineScope.() -> T,
) {
    viewModelScope.launch(Dispatchers.Main) {
        viewState.postValue(ResultData.Starting())
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            viewState.postValue(ResultData.Success(response))
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            viewState.postValue(ResultData.Failure(e))
        }
    }
}

/**
 * [ViewModel]在IO线程中开启协程扩展
 */
fun <T> ViewModel.requestLimit(
    viewState: MutableLiveData<ResultData<T>>,
    request: suspend CoroutineScope.() -> IHttpResponse<T>?,
) {
    viewModelScope.launch(Dispatchers.Main) {
        viewState.postValue(ResultData.Starting())
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            viewState.parseResult(response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            viewState.postValue(ResultData.Failure(e))
        }
    }
}