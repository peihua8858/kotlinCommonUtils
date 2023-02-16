@file:JvmName("ContextUtils")
@file:JvmMultifileClass

package com.fz.common.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fz.common.file.createFileName
import com.fz.common.file.deleteFileOrDir
import com.fz.common.file.formatSize
import com.fz.common.file.getFileSize
import com.fz.common.text.isNonEmpty
import java.io.File
import java.util.*


@SuppressLint("StaticFieldLeak")
internal var mContext: Context? = null
fun initContext(context: Context) {
    mContext = context
}

/**
 * 网络链接管理器
 * @author dingpeihua
 * @date 2021/9/28 16:24
 * @version 1.0
 */
val Context?.connectivityManager: ConnectivityManager?
    get() = this?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

/**
 * 剪贴板管理器
 * @author dingpeihua
 * @date 2021/9/28 16:40
 * @version 1.0
 */
val Context?.clipboardManager: ClipboardManager?
    get() = this?.applicationContext?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

/**
 * 下载管理器
 * @author dingpeihua
 * @date 2021/9/28 16:41
 * @version 1.0
 */
val Context?.downloadManager: DownloadManager?
    get() = this?.applicationContext?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager

/**
 * 电话管理器
 * @author dingpeihua
 * @date 2021/9/28 16:41
 * @version 1.0
 */
val Context?.telephonyManager: TelephonyManager?
    get() = this?.applicationContext?.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

/**
 * 输入管理器
 * @author dingpeihua
 * @date 2021/9/28 16:41
 * @version 1.0
 */
val Context?.inputMethodManager: InputMethodManager?
    get() = this?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

/**
 *位置管理器
 * @author dingpeihua
 * @date 2021/9/28 16:54
 * @version 1.0
 */
val Context?.locationManager: LocationManager?
    get() = this?.applicationContext?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager

/**
 *显示管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.displayManager: DisplayManager?
    get() = this?.applicationContext?.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager

/**
 *相机管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.cameraManager: CameraManager?
    get() = this?.applicationContext?.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

/**
 *窗口管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.windowManager: WindowManager?
    get() = this?.applicationContext?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager

/**
 * 通知管理器
 * @author dingpeihua
 * @date 2021/9/28 16:55
 * @version 1.0
 */
val Context?.notificationManager: NotificationManager?
    get() = this?.applicationContext?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

/**
 * 活动管理器
 * @author dingpeihua
 * @date 2021/9/28 16:56
 * @version 1.0
 */
val Context?.activityManager: ActivityManager?
    get() = this?.applicationContext?.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

/**
 * 警报管理器
 * @author dingpeihua
 * @date 2021/9/28 16:56
 * @version 1.0
 */
val Context?.alarmManager: AlarmManager?
    get() = this?.applicationContext?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

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
            try {
                any.context
            } catch (e: Exception) {
                mContext
            }
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
    copyToClipBoard(lazyContent) {}
}

/**
 * 粘贴到系统剪贴板
 */
fun Context?.copyToClipBoard(lazyContent: () -> CharSequence, callback: ((Boolean) -> Unit)? = null) {
    val content = lazyContent()
    val context = checkContext(this)
    if (context != null) context.copyToClipBoard(
        content,
        callback
    ) else callback?.invoke(false).eLog { "Context  is null." }
}

/**
 * 粘贴到系统剪贴板
 */
@JvmOverloads
fun Context.copyToClipBoard(content: CharSequence, callback: ((Boolean) -> Unit)? = null) {
    val cm = clipboardManager
    val clipData = ClipData.newPlainText("text", content)
    cm?.let {
        try {
            cm.setPrimaryClip(clipData)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        callback?.let {
            cm.addPrimaryClipChangedListener(object :
                ClipboardManager.OnPrimaryClipChangedListener {
                override fun onPrimaryClipChanged() {
                    cm.removePrimaryClipChangedListener(this)
                    callback(true)
                }
            })
        }
    }
}

@JvmOverloads
fun Context.copyToClipBoard(intent: Intent, callback: ((Boolean) -> Unit)? = null) {
    val cm = clipboardManager
    val clipData = ClipData.newIntent("Intent", intent)
    cm?.let {
        try {
            cm.setPrimaryClip(clipData)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        callback?.let {
            cm.addPrimaryClipChangedListener(object :
                ClipboardManager.OnPrimaryClipChangedListener {
                override fun onPrimaryClipChanged() {
                    cm.removePrimaryClipChangedListener(this)
                    callback(true)
                }
            })
        }
    }
}

@JvmOverloads
fun Context.copyToClipBoard(uri: Uri, callback: ((Boolean) -> Unit)? = null) {
    val cm = clipboardManager
    val clipData = ClipData.newUri(contentResolver, "URI", uri)
    cm?.let {
        try {
            cm.setPrimaryClip(clipData)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        callback?.let {
            cm.addPrimaryClipChangedListener(object :
                ClipboardManager.OnPrimaryClipChangedListener {
                override fun onPrimaryClipChanged() {
                    cm.removePrimaryClipChangedListener(this)
                    callback(true)
                }
            })
        }
    }
}

fun Context.pasteText(): CharSequence {
    return pasteText(0)
}

fun Context.pasteText(index: Int): CharSequence {
    val cm = clipboardManager
    try {
        if (cm != null && cm.hasPrimaryClip()) {
            val primaryClip = cm.primaryClip
            val result = primaryClip?.getItemAt(index)?.text
            if (result.isNonEmpty()) {
                return result
            }

        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return ""
}

fun Context.pasteIntent(): Intent? {
    return pasteIntent(0)
}

fun Context.pasteIntent(index: Int): Intent? {
    val cm = clipboardManager
    try {
        if (cm != null && cm.hasPrimaryClip()) {
            val primaryClip = cm.primaryClip
            return primaryClip?.getItemAt(index)?.intent
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}

fun Context.pasteUri(): Uri? {
    return pasteUri(0)
}

fun Context.pasteUri(index: Int): Uri? {
    val cm = clipboardManager
    try {
        if (cm != null && cm.hasPrimaryClip()) {
            val primaryClip = cm.primaryClip
            return primaryClip?.getItemAt(index)?.uri
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
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

fun Context?.getColorCompat(@ColorRes resId: Int): Int {
    val ctx = this ?: mContext
    return ctx?.let {
        ContextCompat.getColor(it, resId)
    } ?: -1
}

fun Context?.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    val ctx = this ?: mContext
    return ctx?.let {
        ContextCompat.getDrawable(it, resId)
    }
}

fun Context?.getDimensionPixelOffset(@DimenRes resId: Int): Int {
    val ctx = this ?: mContext
    return ctx?.resources?.getDimensionPixelOffset(resId) ?: 0
}

fun Context.clearCacheFile() {
    getDiskCacheDir().deleteFileOrDir()
}

fun Context.getCacheSize(): Long {
    return getDiskCacheDir().getFileSize()
}

fun Context.getCacheFormatSize(): String {
    return getDiskCacheDir().formatSize()
}

fun Context.getDiskCacheDir(): File? {
    //如果SD卡存在通过getExternalCacheDir()获取路径，
    //放在路径 /sdcard/Android/data/<application package>/cache/
    val file = externalCacheDir
    //如果SD卡不存在通过getCacheDir()获取路径，
    //放在路径 /data/data/<application package>/cache/
    if (file != null && file.exists()) {
        return file
    }
    return cacheDir
}

/**
 * 根据key获取Manifest里面配置的值
 *
 * @author dingpeihua
 * @date 2018/8/27 16:12
 * @version 1.0
 */
fun Context?.getMetaData(key: String, default: String): String {
    if (this == null) {
        return default
    }
    try {
        val packageManager = packageManager
        val appInfo =
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val notifyChannel =
            appInfo.metaData.getString(key)
        if (notifyChannel.isNonEmpty()) {
            return notifyChannel
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return default
}

/**
 * 保存一个图片文件[source]到相册
 * @param source 图片文件
 * @param description 文件描述
 */
fun Context.saveImageToGallery(source: File, description: String): String? {
    return saveBitmapToGallery(BitmapFactory.decodeFile(source.absolutePath), source.name, description)
}

/**
 * 根据路径[path]保存图片到相册
 * @param path 图片路径
 * @param description 文件描述
 */
fun Context.saveImageToGallery(path: String, description: String): String? {
    return saveBitmapToGallery(BitmapFactory.decodeFile(path), path.createFileName("jpg"), description)
}

/**
 * 保存一个图片[source]到相册
 * @param source 图片对象
 * @param title 文件显示名称
 * @param description 文件描述
 */
fun Context.saveBitmapToGallery(source: Bitmap, title: String, description: String): String? {
    return contentResolver.saveBitmapToGallery(source, title, description)
}
