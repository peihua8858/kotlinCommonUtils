package com.fz.commutils.demo

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fz.common.activity.asyncWhenStart
import com.fz.common.coroutine.asyncApi
import com.fz.common.model.ViewModelState
import com.fz.common.permissions.PermissionRequestDsl
import com.fz.common.permissions.PermissionResult
import com.fz.common.permissions.requestPermissions
import com.fz.common.permissions.requestPermissionsDsl
import com.fz.common.text.isNonEmpty
import com.fz.common.utils.*
import com.fz.commutils.demo.model.*
import com.fz.toast.showToast
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

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
        tv_content.setDrawableStart(R.mipmap.ic_shipping_info)
        btnExecute.setOnClickListener {
//            viewModel.onRequest(1, 20)
//            textView.text = editText.text.emailMask()
            copyTest()
//            val pointBean = PointBean()
//            pointBean.adddate = "ssss"
//            pointBean.balance = "aaaa"
//            pointBean.income = "bbbb"
//            pointBean.json_data = "{ssss}"
//            pointBean.note = "cccc"
//            val pointBean1 = PointBean()
//            pointBean1.adddate = "ssss33"
//            pointBean1.balance = "aaaa33"
//            pointBean1.income = "bbbb33"
//            pointBean1.json_data = "{ssss33}"
//            pointBean1.note = "cccc33"
//            val map: MutableMap<String, MutableList<PointBean>> = mutableMapOf<String, MutableList<PointBean>>()
//            map["2222"] = mutableListOf(pointBean)
//            map["4444"] = mutableListOf(pointBean1)
//            val p = hashMapOf<String, MutableList<PointBean>>()
//            p.putAll(map)
//            KLog.d(">>>>>>" + (map == p))
//            KLog.d(">>>>>>map:$map")
//            KLog.d(">>>>>>p:$p")
//            if (p != null) {
//                for (entry in p) {
////                    entry.value[0].note = "45454545454"
//                    entry.value.removeAt(0)
//                }
//            }
//            KLog.d(">>>>>>map:$map")
//            KLog.d(">>>>>>p:$p")

//            val list = arrayListOf(pointBean)
//            val list1 = list.deepCloneParcelableList()
//            KLog.d(">>>>>>" + (list1 == list))
//            KLog.d(">>>>>>list:$list")
//            KLog.d(">>>>>>list1:$list1")
//            if (list1 != null) {
//                for (entry in list1) {
//                    entry.note = "45454545454"
//                }
//            }
//            KLog.d(">>>>>>list:$list")
//            KLog.d(">>>>>>list1:$list1")

//            val set:Set<PointBean> = setOf(pointBean)
//            val set1 = set.deepCloneParcelableSet()
//            KLog.d(">>>>>>" + (set == set1))
//            KLog.d(">>>>>>set:$set")
//            KLog.d(">>>>>>set1:$set1")
//            if (set1 != null) {
//                for (entry in set1) {
//                    entry.note = "45454545454"
//                }
//            }
//            KLog.d(">>>>>>set:$set")
//            KLog.d(">>>>>>set1:$set1")

//            val array:Array<PointBean>? = arrayOf(pointBean)
//            val array1 = array?.deepCloneParcelableArray()
//            KLog.d(">>>>>>" + (array == array1))
//            KLog.d(">>>>>>array:${Arrays.toString(array)}")
//            KLog.d(">>>>>>set1:${Arrays.toString(array1)}")
//            if (array1 != null) {
//                for (entry in array1) {
//                    entry.note = "45454545454"
//                }
//            }
//            KLog.d(">>>>>>array:${Arrays.toString(array)}")
//            KLog.d(">>>>>>array1:${Arrays.toString(array1)}")

//            val map2 = mutableMapOf<String, PointBean>()
//            map2["12222"] = pointBean
//            val p2 = map2.deepCloneMapOfParcelable()
//            p2["12222"]?.note = "5646456464"
//            KLog.d(">>>>>>map2:$map2")
//            KLog.d(">>>>>>p:$p2")
//            apiWithAsyncCreated<String> {
//                val dialog = processDialog
//                onStart {
//                   showProcessDialog(supportFragmentManager)
//                }
//                onRequest {
//                    try {
//                        Thread.sleep(5000)
//                    } catch (e: Exception) {
//                        eLog { ">>>>>" + e.message }
//                    }
//                    "响应数据"
//                }
//                onResponse {
//                    dLog { ">>>$it" }
//                }
//                onComplete {
//                    dismissProcessDialog()
//                }
//            }
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
            requestPermissions(Manifest.permission.READ_CONTACTS) {
                requestCode = 22
                resultCallback = {
                    when (this) {
                        is PermissionResult.PermissionGranted -> {
                            showToast("Granted!")
                        }
                        is PermissionResult.PermissionDenied -> {
                            val data = deniedPermissions.toString().replace("android.permission.", "")
                            showToast("Denied:$data")
                        }
                        is PermissionResult.ShowRational -> {
                            showToast("ShowRational")
                            showRational(this)
                        }
                        is PermissionResult.PermissionDeniedPermanently -> {
                            val data = permanentlyDeniedPermissions.toString().replace("android.permission.", "")
                            showToast("Denied permanently:$data")
                        }
                    }
                }
            }

//            if (NetworkUtil.isConnected(this,true)) {
//                KLog.d("result>>>yes>>有网络")
//            }
//            null.copyToClipBoard({
//                "sdffff"
//            }, {
//                ToastCompat.showMessage(this, if (it) "拷贝成功" else "拷贝失败")
//            })
        }
        textView1.setOnClickListener {
            this.requestPermissionsDsl(Manifest.permission.READ_CONTACTS) {
                onDenied {
                    val result = it.toString().replace("android.permission.", "")
                    showToast("Denied:$result")
                }
                onGranted {
                    showToast("Granted!")
                }
                onNeverAskAgain {
                    val result = it.toString().replace("android.permission.", "")
                    showToast("NeverAskAgain:$result")
                }
                onShowRationale {
                    val result = it.toString().replace("android.permission.", "")
                    showToast("ShowRationale:$result")
                    showRational(it)
                }
            }
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

    private fun handlePermission(result: PermissionResult) {
        when (result) {
            is PermissionResult.PermissionGranted -> {
                showToast("Granted!")
            }
            is PermissionResult.PermissionDenied -> {
                val data = result.deniedPermissions.toString().replace("android.permission.", "")
                showToast("Denied:$data")
            }
            is PermissionResult.ShowRational -> {
                showToast("ShowRational")
                showRational(result)
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                val data = result.permanentlyDeniedPermissions.toString().replace("android.permission.", "")
                showToast("Denied permanently:$data")
            }
        }
    }

    fun showRational(result: PermissionRequestDsl) {
        AlertDialog.Builder(this)
                .setMessage("We need permission")
                .setTitle("Rational")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("OK") { _, _ ->
                    result.retry()
                }
                .create()
                .show()
    }

    fun showRational(result: PermissionResult) {
        AlertDialog.Builder(this)
                .setMessage("We need permission")
                .setTitle("Rational")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("OK") { _, _ ->
                    val permissions = when (result.requestCode) {
                        1 -> {
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        2 -> {
                            arrayOf(Manifest.permission.READ_CONTACTS)
                        }
                        3 -> {
                            arrayOf(Manifest.permission.CAMERA)
                        }
                        22 -> arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        4 -> {
                            arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.CAMERA
                            )
                        }
                        else -> {
                            arrayOf()
                        }
                    }
                    requestPermissions(*permissions) {
                        requestCode = result.requestCode
                        resultCallback = {
                            handlePermission(this)
                        }
                    }
                }
                .create()
                .show()
    }

    private fun copyTest() {
        val productBean = ProductBean()
        productBean.activityIcon = "ssss"
        productBean.atmos = "aaaa"
        val cateLevel = GoodCatInfoBean()
        cateLevel.first_cat_name = "11111"
        cateLevel.snd_cat_name = "22222"
        cateLevel.four_cat_name = "33333"
        productBean.cat_level_column = cateLevel
        val tags = arrayListOf<TagsBean>()
        tags.add(TagsBean("color", "222"))
        tags.add(TagsBean("color", "333"))
        productBean.tags = tags
        val products = arrayListOf<ProductBean>()
        products.add(productBean.deepCloneParcelable()!!)
        productBean.groupGoodsList = products
        val map: MutableMap<String, MutableList<ProductBean>> = mutableMapOf()
        map["2222"] = mutableListOf(productBean)
        val p = hashMapOf<String, MutableList<ProductBean>>()
        p.putAll(map)
        KLog.d(">>>>>>" + (map == p))
        KLog.d(">>>>>>map:$map")
        KLog.d(">>>>>>p:$p")
        if (p != null) {
            for (entry in p) {
//                    entry.value[0].note = "45454545454"
                entry.value.removeAt(0)
            }
        }
        KLog.d(">>>>>>map:$map")
        KLog.d(">>>>>>p:$p")
    }

    /**
     * 用户电话号码的打码隐藏加星号加*
     *
     * @return 处理完成的身份证
     */
    fun CharSequence?.emailMask(): String {
        var res = ""
        if (this.isNonEmpty()) {
            val stringBuilder = StringBuilder(this)
            val index = indexOf("@")
            res = when {
                index > 5 -> {
                    stringBuilder.replace(5, length, "****").toString()
                }
                index != -1 -> {
                    stringBuilder.replace(index, length, "****").toString()
                }
                else -> {
                    this.toString()
                }
            }
        }
        return res
    }

    private fun request() {
        val job: Job = asyncWhenStart({
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
        job.cancel()
    }
}
