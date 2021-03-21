@file:JvmName("ContextUtils")
package com.fz.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.fz.common.text.isNonEmpty
import java.util.*

@SuppressLint("StaticFieldLeak")
internal var mContext: Context? = null
fun initContext(context: Context) {
    mContext = context.applicationContext
}

/**
 * 检查当前[any]是否可转为[Context]
 * @return 返回当前上下文，如果当前对象不能获取[Context],则返回当前[mContext]
 * @param any
 * @author dingpeihua
 * @date 2021/1/18 16:37
 * @version 1.0
 */
inline fun checkContext(any: Any?, block: (context: Context?) -> Unit) = block(checkContext(any))

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
fun Activity.startNotificationSettingsForResult(requestCode: Int) {
    startActivityForResult(buildIntent(this), requestCode)
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
 * @author dingpeihua
 * @date 2019/12/17 16:27
 * @version 1.0
 */
fun Context.startNotificationSettings() {
    startActivity(buildIntent(this))
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

/**
 * 粘贴到系统剪贴板
 */
fun Context?.copyToClipBoard(lazyContent: () -> CharSequence) {
    val content = lazyContent()
    if (this != null) copyToClipBoard(content, null) else eLog { "Context  is null." }
}

/**
 * 粘贴到系统剪贴板
 */
fun Context?.copyToClipBoard(lazyContent: () -> CharSequence, callback: (Boolean) -> Unit) {
    val content = lazyContent()
    if (this != null) copyToClipBoard(content, callback) else callback(false).eLog { "Context  is null." }
}

/**
 * 粘贴到系统剪贴板
 */
fun Context.copyToClipBoard(content: CharSequence, callback: ((Boolean) -> Unit)?) {
    val context = this.applicationContext
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val text = ClipData.newPlainText("url", content)
    cm.setPrimaryClip(text)
    callback?.let {
        cm.addPrimaryClipChangedListener(object : ClipboardManager.OnPrimaryClipChangedListener {
            override fun onPrimaryClipChanged() {
                cm.removePrimaryClipChangedListener(this)
                callback(true)
            }
        })
    }
}

/**
 * 判断当前用户系统使用的语言为阿拉伯语，true表示为阿拉伯语，false表示其它语言
 *
 * @return
 */
fun Locale?.isUsingArLanguage(): Boolean {
    try {
        val language = this?.language
        return language.isNonEmpty() && language.equals("ar", ignoreCase = true)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 判断当前用户系统使用的语言为阿拉伯语，true表示为阿拉伯语，false表示其它语言
 *
 * @return
 */
fun Any?.isUsingArLanguage(): Boolean {
    try {
        val context = checkContext(this)
        return context?.isUsingArLanguage() ?: false
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

/**
 * 判断当前用户系统使用的语言为阿拉伯语，true表示为阿拉伯语，false表示其它语言
 *
 * @return
 */
fun Context.isUsingArLanguage(): Boolean {
    try {
        val locale: Locale? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }
        return locale.isUsingArLanguage()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}