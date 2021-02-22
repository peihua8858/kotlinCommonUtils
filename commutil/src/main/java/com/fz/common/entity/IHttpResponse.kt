package com.fz.common.entity

/**
 * 接口数据响应
 * @author dingpeihua
 * @date 2021/2/20 9:42
 * @version 1.0
 */
interface IHttpResponse<RESULT> {
    val isSuccess: Boolean
    fun isSuccess(isShowToast: Boolean): Boolean
    val msg: String?
    fun getData(): RESULT?
}