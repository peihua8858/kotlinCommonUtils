package com.fz.common.utils

import java.util.ArrayList

/**
 * 数组转成String输出
 *
 * @param <T>   泛型参数，数组中放置的元素数据类型
 * @return 如果集合不为空返回输出字符串，否则返回""
</T> */
fun <T> Array<T>.string(): String {
    if (size > 0) {
        val sb = StringBuilder()
        for (item in this) {
            sb.append(item).append(",")
        }
        return sb.deleteCharAt(sb.length - 1).toString()
    }
    return ""
}

/**
 * 深度拷贝，性能不佳
 * @author dingpeihua
 * @date 2021/2/26 11:01
 * @version 1.0
 */
fun <T> Array<T>.deepCopyOfArray(): Array<T> {
    val arrList = ArrayList<Any?>()
    for (elem in this) {
        if (elem == null)
            arrList.add(elem)
        else
            arrList.add(elem.deepCopy())
    }
    val newArray = clone()
    arrList.toArray(newArray)
    return newArray
}