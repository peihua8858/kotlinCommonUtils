package com.fz.common

import java.lang.Exception

/**
 * 带错误码的异常
 * @author dingpeihua
 * @date 2022/3/28 17:39
 * @version 1.0
 */
class CodeException(msg: String, val errorCode: Int) : Exception(msg)