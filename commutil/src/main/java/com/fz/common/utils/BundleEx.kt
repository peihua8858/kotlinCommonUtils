@file:JvmName("BundleUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import android.os.Bundle

fun Bundle.toMapOf(): Map<String, Any?> = toMap()
fun <K, V> Bundle.toMap(): Map<K, V> = mapOf(*this.toList<K, V>().toTypedArray())
fun <K, V> Bundle.toList(): List<Pair<K, V>> {
    val size = size()
    if (size == 0) return emptyList()
    val keys = keySet()
    val iterator = keys.iterator()
    if (!iterator.hasNext()) return emptyList()
    val result = ArrayList<Pair<K, V>>(size)
    while (iterator.hasNext()) {
        val key = iterator.next()
        val value = get(key)
        if (value is Bundle) {
            val m = value.toMapOf()
            result.add(Pair(key as K, m as V))

        } else {
            result.add(Pair(key as K, value as V))
        }
    }
    return result
}