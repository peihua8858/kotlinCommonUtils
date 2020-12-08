package com.fz.common.view.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.fz.common.utils.checkContext
import com.fz.common.utils.checkNotNull

fun Any?.dip2px(dpValue: Int): Int {
    return dip2px(dpValue.toFloat())
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.dip2px(dpValue: Float): Int {
    val context = checkContext(this)
    checkNotNull(context, "Context not found.")
    if (context != null) {
        return dip2px(context, dpValue)
    }
    return dpValue.toInt()
}

fun Any?.dip2px(context: Context, dpValue: Float): Int {
    val scale: Float = context.resources?.displayMetrics?.density ?: 0f
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.px2dip(pxValue: Int): Int {
    return px2dip(pxValue.toFloat())
}

/**
 * 在[Context]及其子类中可直接使用该方法
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Context.px2dip(pxValue: Float): Int {
    return px2dip(this, pxValue)
}

fun Any?.px2dip(pxValue: Int): Int {
    return px2dip(pxValue.toFloat())
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.px2dip(pxValue: Float): Int {
    val context = checkContext(this)
    checkNotNull(context, "Context not found.")
    if (context != null) {
        return px2dip(context, pxValue)
    }
    return pxValue.toInt()
}

fun Any?.px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources?.displayMetrics?.density ?: 0f
    return (pxValue / scale + 0.5f).toInt()
}