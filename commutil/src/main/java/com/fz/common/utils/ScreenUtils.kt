package com.fz.common.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.fz.common.ContextInitializer
import com.fz.common.view.utils.dip2px


/**
 * 获得屏幕高度
 *
 * @return
 */
fun Context.getScreenWidth(): Int {
    return getDisplayMetrics().widthPixels
}

/**
 * 获得屏幕高度
 *
 * @return
 */
fun View?.getScreenWidth(): Int {
    if (this == null) return 0
    return context.getDisplayMetrics().widthPixels
}

/**
 * 获得屏幕高度
 *
 * @return
 */
fun Fragment?.getScreenWidth(): Int {
    if (this == null) return 0
    return context?.getDisplayMetrics()?.widthPixels ?: 0
}

/**
 * 获得屏幕高度
 *
 * @return
 */
fun Context.getScreenHeight(): Int {
    return getDisplayMetrics().heightPixels
}

/**
 * 获得屏幕高度
 *
 * @return
 */
fun View?.getScreenHeight(): Int {
    if (this == null) return 0
    return context.getDisplayMetrics().heightPixels
}

/**
 * 获得屏幕高度
 *
 * @return
 */
fun Fragment?.getScreenHeight(): Int {
    if (this == null) return 0
    return context?.getDisplayMetrics()?.heightPixels ?: 0
}

/**
 * 获取系统状态栏高度
 *
 * @author dingpeihua
 * @date 2019/9/2 19:06
 * @version 1.0
 */
fun getStatusHeight(): Int {
    return getStatusHeight(ContextInitializer.mContext)
}

/**
 * 获取系统状态栏高度
 *
 * @param context 当前上下文
 * @author dingpeihua
 * @date 2019/9/2 19:06
 * @version 1.0
 */
fun getStatusHeight(context: Context): Int {
    val statusHeight = getSysResourceDimensionPixelSize(context, "status_bar_height")
    Log.i("ScreenMatchUtil", "statusBarHeight:$statusHeight")
    return if (statusHeight <= 0) context.dip2px(24) else statusHeight
}

/**
 * 获取底部导航栏高度
 *
 * @param context 当前上下文
 * @author dingpeihua
 * @date 2019/9/2 18:59
 * @version 1.0
 */
fun getNavigationBarHeight(context: Context): Int {
    val navigationBarHeight = getSysResourceDimensionPixelSize(context, "navigation_bar_height")
    Log.i("TAG", "navigationBarHeight:$navigationBarHeight")
    return if (navigationBarHeight <= 0) context.dip2px(48) else navigationBarHeight
}

/**
 * 获取底部导航栏高度
 *
 * @author dingpeihua
 * @date 2019/9/2 18:59
 * @version 1.0
 */
fun Context.getNavigationBarHeight(): Int {
    return getNavigationBarHeight(this)
}

/**
 * 获取系统资源id
 *
 * @author dingpeihua
 * @date 2019/9/2 19:04
 * @version 1.0
 */
fun getSystemIdentifier(resources: Resources, name: String?): Int {
    return resources.getIdentifier(name, "dimen", "android")
}

/**
 * 获取系统资源
 *
 * @author dingpeihua
 * @date 2019/9/2 19:02
 * @version 1.0
 */
fun getSysResourceDimensionPixelSize(context: Context, name: String?): Int {
    try {
        val resources = context.resources
        return resources.getDimensionPixelSize(getSystemIdentifier(resources, name))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}

/**
 * 获取系统资源
 *
 * @author dingpeihua
 * @date 2019/9/2 19:02
 * @version 1.0
 */
fun Context.getSysResourceDimensionPixelSize(name: String?): Int {
    return getSysResourceDimensionPixelSize(this, name)
}