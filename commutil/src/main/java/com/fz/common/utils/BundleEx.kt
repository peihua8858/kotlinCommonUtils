@file:JvmName("BundleUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import android.os.Bundle
import androidx.core.os.bundleOf
import org.json.JSONObject
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


fun Map<String, Any?>.toBundle(): Bundle = bundleOf(*this.toList().toTypedArray())
fun <K, V> JSONObject.toMap(): Map<K, V> = mapOf(*this.toList<K, V>().toTypedArray())
fun <K, V> JSONObject.toList(): List<Pair<K, V>> {
    val size = length()
    if (size == 0) return emptyList()
    val keys = keys()
    val iterator = keys.iterator()
    if (!iterator.hasNext()) return emptyList()
    val result = java.util.ArrayList<Pair<K, V>>(size)
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

fun <I, T> List<I>.convert(p: (I) -> T): List<T> {
    val result = mutableListOf<T>()
    for (item in this) {
        result.add(p(item))
    }
    return result
}