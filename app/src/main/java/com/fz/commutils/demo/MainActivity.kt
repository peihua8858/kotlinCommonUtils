package com.fz.commutils.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fz.common.activity.asyncWhenStart
import com.fz.common.coroutine.asyncApi
import com.fz.common.model.ViewModelState
import com.fz.common.utils.*
import com.fz.commutils.demo.model.MainViewModel
import com.fz.commutils.demo.model.RequestParam
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * 测试验证activity
 */
class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.viewState.observe(this, {
            when (it) {
                ViewModelState.Starting -> {
                    dLog { "Starting:" + if (isMainThread()) "在主线程" else "在子线程" }
                    dLog { "开始请求..." }
                }
                ViewModelState.Complete -> {
                    dLog { "Complete:" + if (isMainThread()) "在主线程" else "在子线程" }
                    dLog { "请求完成" }
                }
                is ViewModelState.Error -> {
                    dLog { "Error:" + if (isMainThread()) "在主线程" else "在子线程" }
                    eLog { "请求失败" }
                }
                is ViewModelState.Success -> {

                    dLog { "Success:" + if (isMainThread()) "在主线程" else "在子线程" }
                    dLog { "请求成功" }
                    val response = it.data
                }
            }
        })
        btnExecute.setOnClickListener {
            viewModel.onRequest(1, 20)
        }
        btnExecute1.setOnClickListener {
//            request()
            lifecycleScope.launch {
                asyncApi<String> {
                    onRequest {
                        coroutineScope {
                            eLog { "onRequest:" + if (isMainThread()) "在主线程中" else "在子线程中" }
                            val callResult = async {
                                eLog { "onRequest>>async:" + if (isMainThread()) "在主线程中" else "在子线程中" }
                                val client = OkHttpClient.Builder().build()
                                val call = client.newCall(Request.Builder()
                                        .url("https://www.baidu.com")
                                        .post(RequestParam().createRequestBody())
                                        .build())
                                call.execute()
                            }
                            val response = callResult.await()
                            eLog { if (response.isSuccessful) "网络请求成功" else "网络请求失败" }
                            "请求网络成功>>>"
                        }
                    }
                    onError {
                        eLog { "异常结果：${it.message}" }
                    }
                    onResponse {
                        dLog { "请求结果：$it" }
                    }
                    onComplete {
                        dLog { "执行完成" }
                    }
                }
            }
        }
        textView.setOnClickListener {
//            if (NetworkUtil.isConnected(this,true)) {
//                KLog.d("result>>>yes>>有网络")
//            }
//            null.copyToClipBoard({
//                "sdffff"
//            }, {
//                ToastCompat.showMessage(this, if (it) "拷贝成功" else "拷贝失败")
//            })
        }
//        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            val result = isChecked.yes {
//                KLog.d("result>>>yes>>isChecked：$isChecked")
//                return@yes buttonView
//            }
//            result.otherwise {
//                KLog.d("result>>>yes>>result：${result}")
//            }
//            val result1 =  isChecked.no {
//                KLog.d("result>>>no>>isChecked：$isChecked")
//                return@no buttonView
//            }
//            result1.otherwise {
//                KLog.d("result>>>no>>result1：${result1}")
//            }
//        }
    }

    private fun request() {
        asyncWhenStart({
            eLog { "onRequest:" + if (isMainThread()) "在主线程中" else "在子线程中" }
            val callResult = async {
                eLog { "onRequest>>async:" + if (isMainThread()) "在主线程中" else "在子线程中" }
                val client = OkHttpClient.Builder().build()
                val call = client.newCall(Request.Builder()
                        .url("https://www.baidu.com")
                        .post(RequestParam().createRequestBody())
                        .build())
                call.execute()
            }
            val response = callResult.await()
            eLog { if (response.isSuccessful) "网络请求成功" else "网络请求失败" }
            ""
        }, {

        }, {

        }, {
            eLog { "onComplete:" + if (isMainThread()) "在主线程中" else "在子线程中" }
        })
    }
}