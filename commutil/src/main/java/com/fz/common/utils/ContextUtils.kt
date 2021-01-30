@file:JvmName("ContextUtils")
@file:JvmMultifileClass

package com.fz.common.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment

internal var mContext: Context? = null
fun initContext(context: Context) {
    mContext = context
}

/**
 * 检查当前[any]是否可转为[Context]
 * @return 返回当前上下文，如果当前对象不能获取[Context],则返回当前[mContext]
 * @param any
 * @author dingpeihua
 * @date 2021/1/18 16:37
 * @version 1.0
 */
fun checkContext(any: Any?): Context? {
    return when (any) {
        is Context -> {
            any
        }
        is Activity -> {
            any
        }
        is View -> {
            any.context
        }
        is Fragment -> {
            any.context
        }
        is Dialog -> {
            any.context
        }
        else -> {
            mContext
        }
    }
}

/**
 * 检查当前[any]是否可转为[Context]
 * @return 返回当前上下文，如果当前对象不能获取[Context],则返回null
 * @author dingpeihua
 * @date 2021/1/18 16:39
 * @version 1.0
 */
fun checkContextOrNull(any: Any?): Context? {
    return when (any) {
        is Context -> {
            any
        }
        is Activity -> {
            any
        }
        is View -> {
            any.context
        }
        is Fragment -> {
            any.context
        }
        is Dialog -> {
            any.context
        }
        else -> {
            null
        }
    }
}

/**
 * 判断通知是否启用
 *
 * @param context
 * @author dingpeihua
 * @date 2019/12/17 16:30
 * @version 1.0
 */
fun Any?.isEnabledNotification(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

/**
 * 判断通知是否启用
 *
 * @author dingpeihua
 * @date 2019/12/17 16:30
 * @version 1.0
 */
fun Context?.isEnabledNotification(): Boolean {
    return this != null && isEnabledNotification(this)
}

/**
 * 跳转到通知设置页面
 * 需要有回调
 */
fun Any?.startNotificationSettingsForResult(context: Activity, requestCode: Int) {
    context.startActivityForResult(buildIntent(context), requestCode)
}

/**
 * 跳转到通知设置页面
 *
 * @param context
 * @author dingpeihua
 * @date 2019/12/17 16:27
 * @version 1.0
 */
fun Any?.startNotificationSettings(context: Context) {
    context.startActivity(buildIntent(context))
}

private fun buildIntent(context: Context): Intent {
    val packageName = context.packageName
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val uid = context.applicationInfo.uid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid)
        } else {
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", packageName)
            intent.putExtra("app_uid", uid)
        }
    } else
    // < 4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
    intent.data = Uri.fromParts("package", context.packageName, null)
    return intent
}

