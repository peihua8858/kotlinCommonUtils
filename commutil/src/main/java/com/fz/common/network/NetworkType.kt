package com.fz.common.network

/**
 * 手机网络类型
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2020/11/25 17:17
 */
enum class NetworkType(private val desc: String) {
    /**
     * ETHERNET
     */
    NETWORK_ETHERNET("ETHERNET"),

    /**
     * WiFi
     */
    NETWORK_WIFI("WiFi"),

    /**
     * 移动5G
     */
    NETWORK_5G("5G"),

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
     * 未知移动网络类型
     */
    NETWORK_CELLULAR("CELLULAR"),

    /**
     * 未知网络类型
     */
    NETWORK_UNKNOWN("Unknown"),

    /**
     * 无网络
     */
    NETWORK_NO("No network");

    override fun toString(): String {
        return desc
    }
}