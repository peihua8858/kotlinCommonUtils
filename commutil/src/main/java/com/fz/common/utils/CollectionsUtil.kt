@file:JvmName("CollectionsUtil")

package com.fz.common.utils

import java.util.*
import kotlin.collections.ArrayList
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <T> Collection<T>?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return this != null && size > 0 && isNotEmpty()
}

/**
 * 集合转成String输出
 *
 * @param <T>  泛型参数，集合中放置的元素数据类型
 * @return 如果集合不为空返回输出字符串，否则返回"null"
</T> */
fun <T> Collection<T>.string(): String {
    return toString(this, ",")
}

/**
 * 集合转成String输出
 *
 * @param list      集合
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param separator 分隔符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
</T> */
fun <T> Collection<T>.toString(separator: String?): String {
    if (separator.isNullOrEmpty()) {
        throw NullPointerException("separator is null.")
    }
    if (size > 0) {
        val sb = StringBuilder()
        for (item in this) {
            sb.append(item).append(separator)
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
fun <T> Collection<T>.deepCopyOfCollection(): Collection<T> {
    val arrList = ArrayList<T>()
    for (elem in this) {
        if (elem == null)
            arrList.add(elem)
        else
            arrList.add(elem.deepCopy())
    }
    return arrList
}

/**
 * 深度拷贝，性能不佳
 * @author dingpeihua
 * @date 2021/2/26 11:01
 * @version 1.0
 */
fun <T> Set<T>.deepCopyOfSet(): Collection<T> {
    val newSet = mutableSetOf<T>()
    for (elem in this) {
        if (elem == null)
            newSet.add(elem)
        else
            newSet.add(elem.deepCopy())
    }
    return newSet
}