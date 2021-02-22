package com.fz.commutils.demo.model

import com.fz.common.entity.IHttpResponse
import com.fz.network.cache.ICacheResponse

/**
 * {"statusCode":200,"result":[],"msg":""}
 * [.result]可能为空，使用需要做非空判断
 * 如果[.result]类型不是list，而接口返回result是JSONArray,此时数据为空
 *
 * @param <RESULT>
</RESULT> */
class HttpResponse<RESULT> : ICacheResponse, IHttpResponse<RESULT> {
    var statusCode = 0

    /**
     * 直播那边的接口用的code
     */
    var code = 0
    override var msg: String? = null
    var result: RESULT? = null
        private set

    constructor() {}

    public override fun checkResult(): Boolean {
        if (result is Collection<*>) {
            return (result as Collection<*>).size > 0
        }
        return if (result is Map<*, *>) {
            (result as Map<*, *>).size > 0
        } else result != null
    }

    constructor(result: RESULT, isCacheData: Boolean) {
        statusCode = 200
        this.isCacheData = isCacheData
        this.result = result
    }

    fun setResult(result: RESULT) {
        this.result = result
    }

    override val isSuccess: Boolean
        get() {
            if (statusCode == 200 || code == 200) {
                return true
            }
            return if (statusCode == 202) {
                false
            } else false
        }

    override fun isSuccess(isShowToast: Boolean): Boolean {
        if (isSuccess) {
            return true
        }
        if (isShowToast) {
            showError()
        }
        return false
    }

    fun showError() {}
    override fun toString(): String {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}'
    }

    override fun getData(): RESULT? {
        return result
    }
}