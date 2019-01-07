/*
 * Copyright (C) Globalegrow E-Commerce Co. , Ltd. 2007-2018.
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Globalegrow E-Commerce Co. , Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Globalegrow.
 */

package fz.gb.commutil.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import fz.gb.commutil.R;
import fz.gb.commutil.log.CLog;
import fz.gb.commutil.toast.ToastUtil;


/**
 * 网络工具类
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2017/11/8 10:18
 */
public final class NetworkUtil {
    private NetworkUtil() {
        throw new AssertionError();
    }


    /**
     * 检测网络是否连接
     *
     * @param context
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnected();
            CLog.d("isConnected = " + isConnected);
            return isConnected;
        } else {
            throw new NullPointerException("activity is null");
        }
    }


    /**
     * 检测网络是否连接
     *
     * @param context
     * @param showNetworkErrorTips 是否提示显示网络错误信息，是表示显示，否表示不显示
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(Context context, boolean showNetworkErrorTips) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();
        if (!isConnected && showNetworkErrorTips) {
            showToast(context.getString(R.string.im_tips_check_network));
        }
        return isConnected;
    }

    /**
     * 检测网络是否连接
     *
     * @param fragment
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(Fragment fragment) {
        if (fragment != null) {
            return isNetworkConnected(fragment.getActivity());
        } else {
            throw new NullPointerException("fragment is null");
        }
    }

    /**
     * 检测网络是否连接
     *
     * @param fragment
     * @return 是返回true，否返回false
     */
    public static boolean isNetworkConnected(Fragment fragment, boolean showNetworkErrorTips) {
        if (fragment != null && fragment.getActivity() != null) {
            return isNetworkConnected(fragment.getActivity(), showNetworkErrorTips);
        }
        return false;
    }


    /**
     * 判断是不是使用WiFi链接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:37
     * @version 1.0
     */
    public static boolean isConnectedWifi(Context context) {
        return Connectivity.isConnectedWifi(context);
    }

    /**
     * 判断是不是使用手机移动网络链接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:38
     * @version 1.0
     */
    public static boolean isConnectedMobile(Context context) {
        return Connectivity.isConnectedMobile(context);
    }

    /**
     * 检测网络是否连接
     *
     * @param showNetworkErrorTips true 在没有网络时提示，否则不提示
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    public static boolean isConnected(Context context, boolean showNetworkErrorTips) {
        boolean isConnect = Connectivity.isConnected(context);
        if (!isConnect && showNetworkErrorTips) {
            showToast(context.getString(R.string.im_tips_check_network));
        }
        return isConnect;
    }

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    public static boolean isConnected(Fragment fragment) {
        if (fragment != null) {
            boolean isConnect = Connectivity.isConnected(fragment.getActivity());
            if (!isConnect) {
                showToast(fragment.getString(R.string.im_tips_check_network));
            }
            return isConnect;
        }
        return false;
    }

    /**
     * 检测网络是否连接
     *
     * @author dingpeihua
     * @date 2016/10/19 16:42
     * @version 1.0
     */
    public static boolean isConnected(Context context) {
        if (context != null) {
            boolean isConnect = Connectivity.isConnected(context.getApplicationContext());
            if (!isConnect) {
                showToast(context.getString(R.string.im_tips_check_network));
            }
            return isConnect;
        }
        return false;
    }

    public static boolean isConnectedFast(Context context) {
        return Connectivity.isConnectedFast(context);
    }

    public static @NetworkType.Type String  getNetworkType(Context context) {
        return Connectivity.getNetworkType(context);
    }

    public static @NetworkType.Type String  getNetworkType(NetworkInfo info) {
        return Connectivity.getNetworkType(info);
    }//Must specify a repository for deployment

    public static void showToast(final String message) {
        ToastUtil.showShortMessage(message);
    }
}