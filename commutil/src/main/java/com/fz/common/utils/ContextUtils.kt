package com.fz.common.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment

internal var mContext: Context? = null
fun initContext(context: Context) {
    mContext = context
}

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

