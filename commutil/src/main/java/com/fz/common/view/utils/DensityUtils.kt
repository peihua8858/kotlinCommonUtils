@file:JvmName("DensityUtils")
@file:JvmMultifileClass

package com.fz.common.view.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import com.fz.common.ContextInitializer
import com.fz.common.utils.checkContext
import com.fz.common.utils.eLog
import kotlin.math.roundToInt
import kotlin.math.roundToLong

inline val Int.dp: Int get() = toFloat().dp.roundToInt()

inline val Long.dp: Long get() = toFloat().dp.roundToLong()

inline val Double.dp: Double get() = toFloat().dp.toDouble()

inline val Float.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
inline val Int.sp: Int get() = toFloat().sp.roundToInt()

inline val Long.sp: Long get() = toFloat().sp.roundToLong()

inline val Double.sp: Double get() = toFloat().sp.toDouble()
inline val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

fun Int.pxToDp(): Int = toFloat().px2dp().roundToInt()

fun Long.pxToDp(): Long = toFloat().px2dp().roundToLong()

fun Double.pxToDp(): Double = toFloat().px2dp().toDouble()

fun Float.px2dp(): Float =
    (this / Resources.getSystem().displayMetrics.density + 0.5f)

fun Int.px2sp(): Int = toFloat().px2sp().roundToInt()

fun Long.px2sp(): Long = toFloat().px2sp().roundToLong()

fun Double.px2sp(): Double = toFloat().px2sp().toDouble()

fun Float.px2sp(): Float =
    (this / Resources.getSystem().displayMetrics.scaledDensity + 0.5f)

/**
 *  在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2021/1/18 16:30
 * @version 1.0
 */
fun Any?.dip2px(dpValue: Int): Int {
    val context = checkContext(this) ?: return 0.eLog { "Context not found." }
    return dip2px(context, dpValue)
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
fun Any?.dip2px(context: Context, dpValue: Int): Int {
    return dip2px(context.resources, dpValue)
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
fun Any?.dip2px(resources: Resources, dpValue: Int): Int {
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
    return dip2px(dpValue.toFloat()).roundToInt()
}


/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.dip2px(dpValue: Float): Float {
    val context = checkContext(this) ?: return 0f.eLog { "Context not found." }
    return dip2px(context, dpValue)
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
fun Any?.dip2px(context: Context, dpValue: Float): Float {
    return dip2px(context.resources, dpValue)
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
fun Any?.dip2px(resources: Resources, dpValue: Float): Float {
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
fun Resources?.dip2px(dpValue: Float): Float {
    if (dpValue == 0f) {
        return 0f
    }
    val scale: Float = this?.displayMetrics?.density ?: 0f
    return (dpValue * scale + 0.5f)
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
    val context = checkContext(this) ?: return 0.eLog { "Context not found." }
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
fun Any?.px2dip(resources: Resources, pxValue: Int): Int {
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
    return px2dip(pxValue.toFloat()).roundToInt()
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun Any?.px2dip(pxValue: Float): Float {
    val context = checkContext(this) ?: return 0f.eLog { "Context not found." }
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
fun Any?.px2dip(context: Context, pxValue: Float): Float {
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
fun Any?.px2dip(resources: Resources, pxValue: Float): Float {
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
fun Resources?.px2dip(pxValue: Float): Float {
    if (pxValue == 0f) {
        return 0f
    }
    var resource = this
    if (resource == null) {
        resource = ContextInitializer.mContext.resources
    }
    val scale: Float = resource?.displayMetrics?.density ?: 0f
    return (pxValue / scale + 0.5f)
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
fun Any?.sp2px(context: Context, sp: Int): Int {
    return sp2px(context.resources, sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(resources: Resources, sp: Int): Int {
    return resources.sp2px(sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Resources?.sp2px(sp: Int): Int {
    return sp2px(sp.toFloat()).roundToInt()
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(sp: Float): Float {
    val context = checkContext(this) ?: return 0f.eLog { "Context not found." }
    return sp2px(context, sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(context: Context, sp: Float): Float {
    return sp2px(context.resources, sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Any?.sp2px(resources: Resources, sp: Float): Float {
    return resources.sp2px(sp)
}

/**
 * sp值转换为px值，保证文字大小不变
 *
 * @param sp 字体大小单位
 * @return sp对应的像素
 */
fun Resources?.sp2px(sp: Float): Float {
    if (sp == 0f) {
        return 0f
    }
    var resource = this
    if (resource == null) {
        resource = ContextInitializer.mContext.resources
    }
    val res = resource ?: return 0f.eLog { "Resources not found." }
    val scaledDensity = res.displayMetrics.scaledDensity
    return (sp * scaledDensity + 0.5f)
}