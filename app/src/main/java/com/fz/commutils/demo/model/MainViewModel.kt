package com.fz.commutils.demo.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fz.common.entity.IHttpResponse
import com.fz.common.model.*
import com.fz.common.utils.compareUri
import com.fz.common.utils.dLog
import com.fz.common.utils.isMainThread
import com.fz.common.utils.isSuccessFull
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MainViewModel : ViewModel() {
    val viewState: MutableLiveData<ViewModelState<HttpResponse<PointResponse?>>> = MutableLiveData()
    fun onRequest(currentPage: Int, pageSize: Int) {
        apiRequest<HttpResponse<PointResponse?>> {
            onRequest {
                dLog { "request:" + if (isMainThread()) "在主线程" else "在子线程" }
                val param = RequestParam()
                param.put("page", currentPage)
                param.put("page_size", pageSize)
                val response = HttpResponse<PointResponse?>()
                response.statusCode = 200
                response
            }
            onResponse {
                viewState.parseResponse(it)
            }
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