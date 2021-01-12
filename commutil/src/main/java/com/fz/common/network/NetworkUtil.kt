package com.fz.common.network

import android.content.Context
import androidx.fragment.app.Fragment
import com.fz.common.R
import com.fz.common.text.isNonEmpty
import com.fz.toast.ToastCompat

/**
 * 网络工具类
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2017/11/8 10:18
 */
object NetworkUtil {
    private var mContext: Context? = null

    @JvmStatic
    fun init(context: Context) {
        mContext = context
    }

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    @JvmStatic
    fun isConnected(): Boolean {
        return mContext?.let { Connectivity.isConnected(it) } ?: false
    }

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    @JvmStatic
    fun isConnected(context: Context): Boolean {
        return Connectivity.isConnected(context)
    }

    /**
     * 检测网络是否连接
     *
     * @param showNetworkErrorTips true 在没有网络时提示，否则不提示
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    @JvmStatic
    fun isConnected(context: Context, showNetworkErrorTips: Boolean): Boolean {
        val isConnect = isConnected(context)
        if (!isConnect && showNetworkErrorTips) {
            showToast(context, context.getString(R.string.tips_check_network))
        }
        return isConnect
    }

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    @JvmStatic
    fun isConnected(fragment: Fragment?, showNetworkErrorTips: Boolean): Boolean {
        if (fragment != null) {
            val isConnect = isConnected(fragment.requireContext())
            if (!isConnect && showNetworkErrorTips) {
                showToast(fragment.context, fragment.getString(R.string.tips_check_network))
            }
            return isConnect
        }
        return false
    }

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    @JvmStatic
    fun isConnected(fragment: Fragment?): Boolean {
        return isConnected(fragment, false)
    }

    @JvmStatic
    fun getNetworkType(context: Context): NetworkType {
        return Connectivity.getNetworkType(context)
    }

    @JvmStatic
    fun isConnectedWifi(context: Context): Boolean {
        return Connectivity.isConnectedWifi(context)
    }

    @JvmStatic
    fun isConnectedMobile(context: Context): Boolean {
        return Connectivity.isConnectedMobile(context)
    }

    @JvmStatic
    fun isConnectionFast(context: Context): Boolean {
        return Connectivity.isConnectionFast(context)
    }

    private fun showToast(context: Context?, message: String?) {
        if (message.isNonEmpty()) {
            ToastCompat.showMessage(context, message)
        }
    }
}