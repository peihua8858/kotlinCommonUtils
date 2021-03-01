package com.fz.common.utils

/**
 * map<K,Collection<V> 浅拷贝
 * @author dingpeihua
 * @date 2021/2/26 11:00
 * @version 1.0
 */
fun <K, V> Map<K, Collection<V?>>?.copyOfMapList(): MutableMap<K, MutableList<V?>> {
    val cloneMap = mutableMapOf<K, MutableList<V?>>()
    if (this.isNullOrEmpty()) {
        return cloneMap
    }
    val it = this.iterator()
    while (it.hasNext()) {
        val entry = it.next()
        cloneMap[entry.key] = java.util.ArrayList(entry.value)
    }
    return cloneMap
}

/**
 * 深度拷贝，性能不佳
 * @author dingpeihua
 * @date 2021/2/26 11:01
 * @version 1.0
 */
fun <K, V> Map<K, V>.deepCopyOfMap(): Map<K, V> {
    val newMap = mutableMapOf<K, V>()
    for ((key, value) in this) {
        if (key == null) {
            if (value == null)
                newMap[key] = value
            else
                newMap[key] = value.deepCopy()
        } else {
            if (value === null)
                newMap[key.deepCopy()] = value
            else
                newMap[key.deepCopy()] = value.deepCopy()
        }
    }
    return newMap
}