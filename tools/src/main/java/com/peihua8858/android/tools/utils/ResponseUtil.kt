@file:JvmName("ResponseUtil")
@file:JvmMultifileClass

package com.peihua8858.android.tools.utils

import com.peihua8858.android.tools.entity.IHttpResponse
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun IHttpResponse<*>?.isSuccessFull(showToast: Boolean): Boolean {
    contract {
        returns(true) implies (this@isSuccessFull != null)
    }
    return this != null && this.isSuccess(showToast)
}

@OptIn(ExperimentalContracts::class)
fun IHttpResponse<*>?.isSuccessFull(): Boolean {
    contract {
        returns(true) implies (this@isSuccessFull != null)
    }
    return this != null && this.isSuccess
}