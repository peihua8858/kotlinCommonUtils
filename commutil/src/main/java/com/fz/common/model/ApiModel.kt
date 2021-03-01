package com.fz.common.model

import androidx.fragment.app.FragmentManager
import com.fz.common.utils.eLog
import com.fz.common.utils.getStackTraceMessage
import com.fz.dialog.LoadingDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * api 方法封装
 * @author dingpeihua
 * @date 2021/2/19 17:45
 * @version 1.0
 */
class ApiModel<Response>(private val fragmentManager: FragmentManager? = null, private val isShowDialog: Boolean = false) {

    private var processDialog: LoadingDialogFragment? = null
    internal lateinit var request: suspend () -> Response?

    private var onStart: (() -> Unit?)? = null

    private var onResponse: ((Response?) -> Unit?)? = null

    private var onError: ((Throwable) -> Unit?)? = null

    private var onComplete: (() -> Unit?)? = null

    constructor() : this(null, false)

    internal fun isOnStart(): Boolean {
        return onStart != null
    }

    internal fun isOnResponse(): Boolean {
        return onResponse != null
    }

    internal fun isOnError(): Boolean {
        return onError != null
    }

    internal fun isOnComplete(): Boolean {
        return onComplete != null
    }

    infix fun onStart(onStart: (() -> Unit?)?): ApiModel<Response> {
        this.onStart = onStart
        return this
    }

    infix fun onRequest(request: suspend () -> Response?): ApiModel<Response> {
        this.request = request
        return this
    }

    infix fun onResponse(onResponse: ((Response?) -> Unit)?): ApiModel<Response> {
        this.onResponse = onResponse
        return this
    }

    infix fun onError(onError: ((Throwable) -> Unit)?): ApiModel<Response> {
        this.onError = onError
        return this
    }

    infix fun onComplete(onComplete: (() -> Unit)?): ApiModel<Response> {
        this.onComplete = onComplete
        return this
    }

    private fun showProcessDialog() {
        if (fragmentManager != null && isShowDialog) {
            showProcessDialog(fragmentManager)
        }
    }

    fun showProcessDialog(fragmentManager: FragmentManager, isCancelable: Boolean = false) {
        if (processDialog == null) {
            processDialog = LoadingDialogFragment()
            processDialog!!.isCancelable = isCancelable
            processDialog!!.setCanceledOnTouchOutside(isCancelable)
        }
        if (processDialog != null && processDialog!!.isShowing) {
            processDialog!!.show(fragmentManager, "ApiLoadingDialog")
        }
    }

    fun showProcessDialog(fragmentManager: FragmentManager) {
        showProcessDialog(fragmentManager, false)
    }

    fun dismissProcessDialog() {
        if (processDialog != null) {
            processDialog!!.dismissAllowingStateLoss()
            processDialog = null
        }
    }

    internal suspend fun syncLaunch() {
        showProcessDialog()
        onStart?.invoke()
        try {
            val response = request()
            onResponse?.invoke(response)
        } catch (e: Exception) {
            eLog { e.getStackTraceMessage() }
            onError?.invoke(e)
        } finally {
            onComplete?.invoke()
            dismissProcessDialog()
        }
    }

    internal suspend fun launch() {
        showProcessDialog()
        onStart?.invoke()
        try {
            val response = withContext(Dispatchers.IO) {
                request()
            }
            onResponse?.invoke(response)
        } catch (e: Exception) {
            eLog { e.getStackTraceMessage() }
            onError?.invoke(e)
        } finally {
            onComplete?.invoke()
            dismissProcessDialog()
        }
    }

    internal fun syncLaunch(viewModelScope: CoroutineScope) {
        viewModelScope.launch(Dispatchers.Main) {
            showProcessDialog()
            onStart?.invoke()
            try {
                val response = request()
                onResponse?.invoke(response)
            } catch (e: Exception) {
                eLog { e.getStackTraceMessage() }
                onError?.invoke(e)
            } finally {
                onComplete?.invoke()
                dismissProcessDialog()
            }
        }
    }

    internal fun launch(viewModelScope: CoroutineScope) {
        viewModelScope.launch(Dispatchers.Main) {
            showProcessDialog()
            onStart?.invoke()
            try {
                val response = withContext(Dispatchers.IO) {
                    request()
                }
                onResponse?.invoke(response)
            } catch (e: Exception) {
                eLog { e.getStackTraceMessage() }
                onError?.invoke(e)
            } finally {
                onComplete?.invoke()
                dismissProcessDialog()
            }
        }
    }
}