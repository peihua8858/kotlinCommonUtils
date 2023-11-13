package com.fz.common.utils

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ApplicationInfoFlags
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import com.fz.common.ContextInitializer
import com.fz.common.text.isNonEmpty
import android.content.ComponentName

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
        val appInfo = this.appInfo
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

val Context.appInfo: ApplicationInfo
    get() {
        return if (isTiramisu)
            packageManager.getApplicationInfo(
                packageName,
                ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }
val Context.activityInfo:ActivityInfo
    get() {
        return if (isTiramisu)
            packageManager.getActivityInfo(ComponentName<Activity>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getActivityInfo(ComponentName<Activity>(), PackageManager.GET_META_DATA)
    }
val Context.serviceInfo: ServiceInfo
    get() {
        return if (isTiramisu)
            packageManager.getServiceInfo(ComponentName<Service>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getServiceInfo(ComponentName<Service>(), PackageManager.GET_META_DATA)
    }
val Context.providerInfo: ProviderInfo
    get() {
        return if (isTiramisu)
            packageManager.getProviderInfo(ComponentName<Service>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getProviderInfo(ComponentName<Service>(), PackageManager.GET_META_DATA)
    }
val Context.receiverInfo: ActivityInfo
    get() {
        return if (isTiramisu)
            packageManager.getReceiverInfo(ComponentName<Service>(),
                PackageManager.ComponentInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        else
            packageManager.getReceiverInfo(ComponentName<Service>(), PackageManager.GET_META_DATA)
    }
fun applicationMetaDataOf(name: String): String? =
    ContextInitializer.mContext.appInfo.metaData.getString(name)

fun activityMetaDataOf(name: String): String? =
    ContextInitializer.mContext.activityInfo.metaData.getString(name)

 fun serviceMetaDataOf(name: String): String? =
    ContextInitializer.mContext.serviceInfo.metaData.getString(name)

inline fun <reified T : BroadcastReceiver> providerMetaDataOf(name: String): String? =
    ContextInitializer.mContext.providerInfo.metaData.getString(name)

inline fun <reified T : BroadcastReceiver> receiverMetaDataOf(name: String): String? =
    ContextInitializer.mContext.receiverInfo.metaData.getString(name)

inline fun <reified T> ComponentName() = ComponentName(ContextInitializer.mContext, T::class.java)