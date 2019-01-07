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

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.RetentionPolicy;

/**
 * 手机网络类型
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2017/11/8 09:05
 */
public class NetworkType {
    /**
     * WiFi
     */
    public static final String NETWORK_WIFI = ("WiFi");
    /**
     * 移动4G
     */
    public static final String NETWORK_4G =("4G");
    /**
     * 移动3G
     */
    public static final String NETWORK_3G =("3G");
    /**
     * 移动2G
     */
    public static final String NETWORK_2G = ("2G");
    /**
     * 未知网络类型
     */
    public static final String NETWORK_UNKNOWN = ("Unknown");
    /**
     * 无网络
     */
    public static final String NETWORK_NO =("No network");

    private String desc;

    @StringDef({NETWORK_WIFI,NETWORK_4G,NETWORK_3G,NETWORK_2G,NETWORK_UNKNOWN,NETWORK_NO})
    public @interface Type{

    }
    NetworkType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
