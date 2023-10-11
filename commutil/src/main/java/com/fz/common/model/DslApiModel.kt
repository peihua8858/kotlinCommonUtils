package com.fz.common.model

/**
 * dsl 回调监听
 * @author dingpeihua
 * @date 2023/10/11 11:39
 * @version 1.0
 */
class DslApiModel<Response> {

    private var onStart: (() -> Unit?)? = null

    private var onResponse: ((Response) -> Unit?)? = null

    private var onError: ((Throwable) -> Unit?)? = null

    private var onComplete: (() -> Unit?)? = null
    private var onCancel: (() -> Unit?)? = null

    infix fun onStart(onStart: (() -> Unit?)?): DslApiModel<Response> {
        this.onStart = onStart
        return this
    }

    infix fun onResponse(onResponse: ((Response) -> Unit)?): DslApiModel<Response> {
        this.onResponse = onResponse
        return this
    }

    infix fun onError(onError: ((Throwable) -> Unit)?): DslApiModel<Response> {
        this.onError = onError
        return this
    }

    infix fun onComplete(onComplete: (() -> Unit)?): DslApiModel<Response> {
        this.onComplete = onComplete
        return this
    }

    infix fun onCancel(onCancel: (() -> Unit)?): DslApiModel<Response> {
        this.onCancel = onCancel
        return this
    }

    fun invokeOnError(e: Throwable) {
        this.onError?.invoke(e)
    }

    fun invokeOnResponse(response: Response) {
        this.onResponse?.invoke(response)
    }

    fun invokeOnStart() {
        this.onStart?.invoke()
    }

    fun invokeOnComplete() {
        this.onComplete?.invoke()
    }

    fun invokeOnCancel() {
        this.onCancel?.invoke()
    }
}