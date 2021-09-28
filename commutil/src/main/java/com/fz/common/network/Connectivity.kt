package com.fz.common.network

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.fz.common.utils.connectivityManager
import com.fz.common.utils.telephonyManager

/**
 * 网络连接工具
 *
 * @author dingpeihua.
 * @version 1.0
 * @date 2016-10-19 16:33
 */
internal object Connectivity {

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.connectivityManager
        return connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isConnectedM(connectivityManager)
            } else {
                isConnectedL(connectivityManager)
            }
        } ?: false
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun isConnectedM(connectivityManager: ConnectivityManager): Boolean {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            //Indicates this network uses a Cellular transport
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //Indicates this network uses a Wi-Fi transport.
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            //Indicates this network uses a VPN transport.
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
            //Indicates this network uses a Wi-Fi Aware transport.
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> true
            //Indicates this network uses a LoWPAN transport.
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> true
            else -> false
        }
    }

    private fun isConnectedL(connectivityManager: ConnectivityManager): Boolean {
        val networkInfo: NetworkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isAvailable && networkInfo.isConnected
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */
    fun isConnectedWifi(context: Context): Boolean {
        val networkType: NetworkType = getNetworkType(context)
        return networkType == NetworkType.NETWORK_WIFI
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    fun isConnectedMobile(context: Context): Boolean {
        val networkType: NetworkType = getNetworkType(context)
        return networkType == NetworkType.NETWORK_CELLULAR
                || networkType == NetworkType.NETWORK_2G
                || networkType == NetworkType.NETWORK_3G
                || networkType == NetworkType.NETWORK_4G
                || networkType == NetworkType.NETWORK_5G
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    fun isConnectionFast(context: Context): Boolean {
        val networkType: NetworkType =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                getNetworkTypeM(context)
            } else {
                getNetworkTypeL(context)
            }
        return when (networkType) {
            NetworkType.NETWORK_3G,
            NetworkType.NETWORK_4G,
            NetworkType.NETWORK_5G,
            NetworkType.NETWORK_WIFI,
            NetworkType.NETWORK_ETHERNET,
            -> true
            else -> false
        }
    }

    /**
     * 获取当前网络类型
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return 网络类型
     *
     *  [NetworkType.NETWORK_WIFI]
     *  [NetworkType.NETWORK_ETHERNET]
     *  [NetworkType.NETWORK_CELLULAR]
     *  [NetworkType.NETWORK_5G]
     *  [NetworkType.NETWORK_4G]
     *  [NetworkType.NETWORK_3G]
     *  [NetworkType.NETWORK_2G]
     *  [NetworkType.NETWORK_UNKNOWN]
     *  [NetworkType.NETWORK_NO]
     *
     */
    fun getNetworkType(context: Context): NetworkType {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            getNetworkTypeM(context)
        } else {
            getNetworkTypeL(context)
        }
    }

    private fun getNetworkTypeL(context: Context): NetworkType {
        val cm = context.connectivityManager
        val networkInfo: NetworkInfo? = cm?.activeNetworkInfo
        if (networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected) {
            return when (networkInfo.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    NetworkType.NETWORK_WIFI
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    val type: NetworkType = getNetworkType(networkInfo.subtype)
                    if (type == NetworkType.NETWORK_CELLULAR) {
                        val subtypeName = networkInfo.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            NetworkType.NETWORK_3G
                        } else {
                            NetworkType.NETWORK_CELLULAR
                        }
                    }
                    return type
                }
                else -> {
                    NetworkType.NETWORK_UNKNOWN
                }
            }
        }
        return NetworkType.NETWORK_NO
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getNetworkTypeM(context: Context): NetworkType {
        val connectivityManager = context.connectivityManager
        val nw = connectivityManager?.activeNetwork ?: return NetworkType.NETWORK_NO
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return NetworkType.NETWORK_NO
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.NETWORK_WIFI
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.NETWORK_ETHERNET
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val tm = context.telephonyManager
                    return tm?.let {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                            getNetworkType(tm.dataNetworkType)
                        } else {
                            getNetworkType(tm.networkType)
                        }
                    } ?: NetworkType.NETWORK_UNKNOWN
                }
                return NetworkType.NETWORK_CELLULAR
            }
            else -> NetworkType.NETWORK_NO
        }
    }

    private fun getNetworkType(networkType: Int): NetworkType {
        when (networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN,
            TelephonyManager.NETWORK_TYPE_GSM -> return NetworkType.NETWORK_2G
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP,
            TelephonyManager.NETWORK_TYPE_TD_SCDMA -> return NetworkType.NETWORK_3G
            TelephonyManager.NETWORK_TYPE_LTE,
                //TelephonyManager.NETWORK_TYPE_LTE_CA=19
            TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> return NetworkType.NETWORK_4G
            TelephonyManager.NETWORK_TYPE_NR -> return NetworkType.NETWORK_5G
            else -> return NetworkType.NETWORK_CELLULAR
        }
    }
}