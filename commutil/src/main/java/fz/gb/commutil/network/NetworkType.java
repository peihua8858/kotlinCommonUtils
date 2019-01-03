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

/**
 * 手机网络类型
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2017/11/8 09:05
 */
public enum NetworkType {
    /**
     * WiFi
     */
    NETWORK_WIFI("WiFi"),
    /**
     * 移动4G
     */
    NETWORK_4G("4G"),
    /**
     * 移动3G
     */
    NETWORK_3G("3G"),
    /**
     * 移动2G
     */
    NETWORK_2G("2G"),
    /**
     * 未知网络类型
     */
    NETWORK_UNKNOWN("Unknown"),
    /**
     * 无网络
     */
    NETWORK_NO("No network");

    private String desc;

    NetworkType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
