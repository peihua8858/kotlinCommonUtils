package com.fz.common.model

import androidx.fragment.app.FragmentManager
import com.fz.common.utils.eLog
import com.fz.common.utils.getStackTraceMessage
import com.fz.dialog.LoadingDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException

/**
 * api 方法封装
 * @author dingpeihua
 * @date 2021/2/19 17:45
 * @version 1.0
 */
class ApiModel<Response>(
    private val fragmentManager: FragmentManager? = null,
    private val isShowDialog: Boolean = false
) {
    private var coroutineScope: CoroutineScope? = null
    private var isManualClose = false
    private var processDialog: LoadingDialogFragment? = null
    internal lateinit var request: suspend CoroutineScope.() -> Response

    private var onStart: (() -> Unit?)? = null

    private var onResponse: ((Response) -> Unit?)? = null

    private var onError: ((Throwable) -> Unit?)? = null

    private var onComplete: (() -> Unit?)? = null
    private var onCancel: (() -> Unit?)? = null
    private var isCanceledJob = false

    constructor() : this(null, false)

    internal fun isOnStart(): Boolean {
        return onStart != null
    }

    internal fun isOnResponse(): Boolean {
        return onResponse != null
    }

    internal fun isOnCancel(): Boolean {
        return onCancel != null
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

    infix fun onRequest(request: suspend CoroutineScope.() -> Response): ApiModel<Response> {
        this.request = request
        return this
    }

    infix fun onResponse(onResponse: ((Response) -> Unit)?): ApiModel<Response> {
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

    infix fun onCancel(onCancel: (() -> Unit)?): ApiModel<Response> {
        this.onCancel = onCancel
        return this
    }

    private fun showLoadingDialog() {
        if (fragmentManager != null && isShowDialog) {
            showLoadingDialog(fragmentManager)
        }
    }

    fun showLoadingDialog(
        fragmentManager: FragmentManager,
        isManualClose: Boolean = false,
        isCancelable: Boolean = true
    ) {
        this.isManualClose = isManualClose
        if (processDialog == null) {
            processDialog = LoadingDialogFragment()
            processDialog!!.isCancelable = isCancelable
            processDialog!!.setCanceledOnTouchOutside(isCancelable)
            processDialog!!.setCanceledOnBackPressed(isCancelable)
            processDialog!!.setOnDismissListener {
                cancelJob()
            }
        }
        if (processDialog != null && !processDialog!!.isShowing) {
            processDialog!!.show(fragmentManager, "ApiLoadingDialog")
        }
    }

    fun showLoadingDialog(fragmentManager: FragmentManager) {
        showLoadingDialog(fragmentManager, false)
    }

    fun showLoadingDialog(fragmentManager: FragmentManager, isManualClose: Boolean = false) {
        showLoadingDialog(fragmentManager, isManualClose, false)
    }

    val isCanceled: Boolean
        get() = isCanceledJob

    fun cancelJob() {
        isCanceledJob = true
        coroutineScope?.cancel(CancellationException("Close dialog"))
        invokeOnCancel()
    }

    fun dismissLoadingDialog() {
        if (processDialog != null) {
            processDialog!!.dismissAllowingStateLoss()
            processDialog = null
        }
    }

    internal suspend fun syncLaunch() {
        coroutineScope {
            showLoadingDialog()
            invokeOnStart()
            try {
                val response = request()
                invokeOnResponse(response)
            } catch (e: Throwable) {
                eLog { e.getStackTraceMessage() }
                invokeOnError(e)
            } finally {
                invokeOnComplete()
                if (!isManualClose)
                    dismissLoadingDialog()
            }
        }
    }

    internal suspend fun launch() {
        showLoadingDialog()
        invokeOnStart()
        try {
            val response = withContext(Dispatchers.IO) {
                coroutineScope = this
                request()
            }
            invokeOnResponse(response)
        } catch (e: Throwable) {
            eLog { e.getStackTraceMessage() }
            invokeOnError(e)
        } finally {
            invokeOnComplete()
            if (!isManualClose)
                dismissLoadingDialog()
        }
    }

    internal fun syncLaunch(viewModelScope: CoroutineScope) {
        viewModelScope.launch(Dispatchers.Main) {
            coroutineScope = this
            showLoadingDialog()
            invokeOnStart()
            try {
                val response = request()
                invokeOnResponse(response)
            } catch (e: Throwable) {
                eLog { e.getStackTraceMessage() }
                invokeOnError(e)
            } finally {
                invokeOnComplete()
                if (!isManualClose)
                    dismissLoadingDialog()
            }
        }
    }

    internal fun launch(viewModelScope: CoroutineScope) {
        viewModelScope.launch(Dispatchers.Main) {
            coroutineScope = this
            showLoadingDialog()
            invokeOnStart()
            try {
                val response = withContext(Dispatchers.IO) {
                    request()
                }
                invokeOnResponse(response)
            } catch (e: Throwable) {
                eLog { e.getStackTraceMessage() }
                invokeOnError(e)
            } finally {
                invokeOnComplete()
                if (!isManualClose)
                    dismissLoadingDialog()
            }
        }
    }

    fun invokeOnError(e: Throwable) {
        if (isCanceled) return
        this.onError?.invoke(e)
    }

    fun invokeOnResponse(response: Response) {
        if (isCanceled) return
        this.onResponse?.invoke(response)
    }

    fun invokeOnStart() {
        if (isCanceled) return
        this.onStart?.invoke()
    }

    fun invokeOnComplete() {
        if (isCanceled) return
        this.onComplete?.invoke()
    }

    fun invokeOnCancel() {
        this.onCancel?.invoke()
    }
}