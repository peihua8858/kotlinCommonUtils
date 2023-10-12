package com.fz.common.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
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
 * @param context 当前上下文
 * @author dingpeihua
 * @date 2019/9/2 19:06
 * @version 1.0
 */
fun Context.getStatusHeight(): Int {
    val statusHeight = getSysResourceDimensionPixelSize("status_bar_height")
    Log.i("ScreenMatchUtil", "statusBarHeight:$statusHeight")
    return if (statusHeight <= 0) dip2px(24) else statusHeight
}

/**
 * 获取底部导航栏高度
 *
 * @param context 当前上下文
 * @author dingpeihua
 * @date 2019/9/2 18:59
 * @version 1.0
 */
fun Context.getNavigationBarHeight(): Int {
    val navigationBarHeight = getSysResourceDimensionPixelSize("navigation_bar_height")
    Log.i("TAG", "navigationBarHeight:$navigationBarHeight")
    return if (navigationBarHeight <= 0) dip2px(48) else navigationBarHeight
}


/**
 * 获取系统资源id
 *
 * @author dingpeihua
 * @date 2019/9/2 19:04
 * @version 1.0
 */
fun Resources.getSystemIdentifier(name: String?): Int {
    return getIdentifier(name, "dimen", "android")
}

/**
 * 获取系统资源
 *
 * @author dingpeihua
 * @date 2019/9/2 19:02
 * @version 1.0
 */
fun Context.getSysResourceDimensionPixelSize(name: String?): Int {
    try {
        val resources = resources
        return resources.getDimensionPixelSize(resources.getSystemIdentifier(name))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return -1
}