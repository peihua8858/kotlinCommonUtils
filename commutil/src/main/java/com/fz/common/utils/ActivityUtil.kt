@file:JvmName("ActivityUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.Fragment


fun Any?.findActivity(): Activity? {
    return findActivity(checkContextOrNull(this))
}

/**
 * 查找当前上下文activity
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:38
 * @version 1.0
 */
fun Any?.findActivity(context: Context?): Activity? {
    if (context is Activity) {
        if (!context.isFinishing && !context.isDestroyed) {
            return context
        }
    } else if (context is ContextWrapper) {
        return findActivity(context.baseContext)
    }
    return null
}

/**
 * 判断activity是否销毁
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Context?.isDestroy(): Boolean {
    return isDestroy(findActivity(this))
}

/**
 * 判断activity是否关闭
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isFinish(context: Context): Boolean {
    return isFinish(findActivity(context))
}

/**
 * 判断activity是否销毁
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Activity?.isDestroy(): Boolean {
    return this != null && this.isDestroyed
}

/**
 * 判断activity是否销毁
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isDestroy(activity: Activity?): Boolean {
    return activity != null && activity.isDestroyed
}

/**
 * 判断activity是否关闭
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isFinish(activity: Activity?): Boolean {
    return activity != null && activity.isFinishing
}