package com.fz.commutils.demo.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fz.common.model.*
import com.fz.common.utils.dLog
import com.fz.common.utils.eLog
import com.fz.common.utils.isMainThread
import com.fz.common.utils.isSuccessFull
import com.fz.commutils.demo.api.ApiManager
import java.lang.NullPointerException

class MainViewModel : ViewModel() {
    val viewState: MutableLiveData<ResultData<PointResponse?>> = MutableLiveData()
    fun onRequest(currentPage: Int, pageSize: Int) {
        apiRequestLimit(viewState) {
            onRequest {
                dLog { "request:" + if (isMainThread()) "在主线程" else "在子线程" }
                val param = RequestParam()
                param.put("page", currentPage)
                param.put("page_size", pageSize)
                val response = ApiManager.userApi().requestPoints(param.createRequestBody())
                if (response.isSuccessFull()) {
                    return@onRequest response
                }
                throw NullPointerException("error")
            }
            onComplete {
                eLog { "执行自定》》》》onComplete" }
//                viewState.parseComplete()
            }
            onResponse {
//                viewState.parseResult(it)
            }
        }
        request(viewState){
            dLog { "request:" + if (isMainThread()) "在主线程" else "在子线程" }
            val param = RequestParam()
            param.put("page", currentPage)
            param.put("page_size", pageSize)
            val response = ApiManager.userApi().requestPoints(param.createRequestBody())
            if (response.isSuccessFull()) {
                return@request response.result
            }
            throw NullPointerException("error")
        }
    }

    fun onRequest2(currentPage: Int, pageSize: Int) {
//        request(viewState) {
//            coroutineScope {
//                dLog { "request:" + if (isMainThread()) "在主线程" else "在子线程" }
//                val re = async {
//                    dLog { "request>>async:" + if (isMainThread()) "在主线程" else "在子线程" }
//                    val param = RequestParam()
//                    param.put("page", currentPage)
//                    param.put("page_size", pageSize)
//                    val response = HttpResponse<PointResponse?>()
//                    response.statusCode = 200
//                    response
//                }
//                re.await()
//            }
//        }
    }
}