@file:JvmName("DensityUtils")

package com.fz.common.view.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.fragment.app.Fragment
import com.fz.common.utils.checkContext
import com.fz.common.utils.checkNotNull
import com.fz.common.utils.eLog
import com.fz.common.utils.mContext

/**
 *  在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2021/1/18 16:30
 * @version 1.0
 */
fun Any?.dip2px(dpValue: Int): Int {
    return dip2px(dpValue.toFloat())
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.dip2px(dpValue: Float): Int {
    val context = checkContext(this) ?: return 0.eLog { "Context not found." }
    return dip2px(context, dpValue)
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法
 * @param resources [Resources]
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.dip2px(resources: Resources, dpValue: Float): Int {
    return resources.dip2px(dpValue)
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Resources?.dip2px(dpValue: Int): Int {
    return dip2px(dpValue.toFloat())
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Resources?.dip2px(dpValue: Float): Int {
    val scale: Float = this?.displayMetrics?.density ?: 0f
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param context 当前上下文
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.dip2px(context: Context, dpValue: Float): Int {
    return dip2px(context.resources, dpValue)
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将px值转dp
 * @param pxValue px 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
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
    val context = checkContext(this) ?: return 0.eLog { "Context not found." }
    return px2dip(context.resources, pxValue)
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将px值转dp
 * @param context 当前上下文
 * @param pxValue px 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.px2dip(context: Context, pxValue: Float): Int {
    return px2dip(context.resources, pxValue)
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将px值转dp
 * @param resources [Resources]
 * @param pxValue px 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.px2dip(resources: Resources, pxValue: Float): Int {
    return resources.px2dip(pxValue)
}

/**
 * 使用[Resources]可直接使用该方法将px值转dp
 * @param pxValue px 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Resources?.px2dip(pxValue: Int): Int {
    return px2dip(pxValue.toFloat())
}

/**
 * 使用[Resources]可直接使用该方法将px值转dp
 * @param pxValue px 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Resources?.px2dip(pxValue: Float): Int {
    if (pxValue == 0f) {
        return 0
    }
    var resource = this
    if (resource == null) {
        resource = mContext?.resources
    }
    val scale: Float = resource?.displayMetrics?.density ?: 0f
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(sp: Int): Int {
    val context = checkContext(this) ?: return 0.eLog { "Context not found." }
    return sp2px(context, sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(sp: Float): Int {
    val context = checkContext(this) ?: return 0.eLog { "Context not found." }
    return sp2px(context, sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(context: Context, sp: Int): Int {
    return sp2px(context.resources, sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(context: Context, sp: Float): Int {
    return sp2px(context.resources, sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(resources: Resources, sp: Float): Int {
    return resources.sp2px(sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(resources: Resources, sp: Int): Int {
    return resources.sp2px(sp.toFloat())
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Resources?.sp2px(sp: Int): Int {
    return sp2px(sp.toFloat())
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Resources?.sp2px(sp: Float): Int {
    if (sp == 0f) {
        return 0
    }
    var resource = this
    if (resource == null) {
        resource = mContext?.resources
    }
    val res = resource ?: return 0.eLog { "Resources not found." }
    val scaledDensity = res.displayMetrics.scaledDensity
    return (sp * scaledDensity + 0.5f).toInt()
}