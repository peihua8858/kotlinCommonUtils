@file:JvmName("AdapterUtil")

package com.fz.common.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.fz.common.view.utils.dip2px


@ColorInt
fun BaseQuickAdapter<*, *>?.getColor(@ColorRes colorRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return ContextCompat.getColor(context, colorRes)
}

fun BaseQuickAdapter<*, *>?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context: Context = checkContext(this) ?: return null.eLog { "Context  is null." }
    return ContextCompat.getDrawable(context, drawableRes)
}

fun BaseQuickAdapter<*, *>?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return context.getDimens(resId)
}

/**
 * 在[View]、 [Context]、[Activity]、[Fragment]、[Dialog]中可直接使用该方法将dp值转px
 * @param dpValue dp 值
 * @return 返回转换后的像素值
 * @author dingpeihua
 * @date 2020/7/7 17:50
 * @version 1.0
 */
fun BaseQuickAdapter<*, *>?.dip2px(dpValue: Int): Int {
    return dip2px(dpValue.toFloat())
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun BaseQuickAdapter<*, *>?.getResourceId(attrId: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return getResourceId(context, attrId)
}

/**
 * 解析当前上下文主题，获取主题样式
 *
 * @param context    当前上下文
 * @param resId      资源ID
 * @param defaultRes 默认主题样式
 * @author dingpeihua
 * @date 2020/7/7 10:31
 * @version 1.0
 */
fun BaseQuickAdapter<*, *>?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0.eLog { "Context  is null." }
    return resolveAttribute(context, resId, defaultRes)
}
