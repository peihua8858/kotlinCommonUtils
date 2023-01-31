@file:JvmName("MapUtil")
@file:JvmMultifileClass

package com.fz.common.map

import android.os.BadParcelableException
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.bundleOf
import java.io.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <K, V> Map<K, V>?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return this != null && size > 0 && isNotEmpty()
}

/**
 * map<K,Collection<V> 浅拷贝
 * @author dingpeihua
 * @date 2021/2/26 11:00
 * @version 1.0
 */
fun <K, V> Map<K, MutableList<V>>?.copyOfMapList(): MutableMap<K, MutableList<V>> {
    val cloneMap = mutableMapOf<K, MutableList<V>>()
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
 * map<K,Collection<V> 深拷贝
 * @author dingpeihua
 * @date 2021/2/26 11:00
 * @version 1.0
 */
fun <K, V : Parcelable> Map<K, MutableList<V>>.deepCloneMapOfParcelableList(): MutableMap<K, MutableList<V>> {
    if (this.isNullOrEmpty()) {
        return mutableMapOf()
    }
    val source = Parcel.obtain()
    var size = entries.size
    source.writeInt(size)
    var keyClassLoader: ClassLoader? = null
    var valueClassLoader: ClassLoader? = null
    for ((key, value) in entries) {
        if (keyClassLoader == null && key != null) {
            keyClassLoader = key.javaClass.classLoader
        }
        source.writeValue(key)
        val l = value.size
        source.writeInt(l)
        for (v in value) {
            source.writeParcelable(v, v.describeContents())
            if (valueClassLoader == null) {
                valueClassLoader = v.javaClass.classLoader
            }
        }
        size--
    }
    if (size != 0) {
        throw BadParcelableException("Map size does not match number of entries!")
    }
    val bytes: ByteArray = source.marshall()
    source.recycle()
    val newSource = Parcel.obtain()
    newSource.unmarshall(bytes, 0, bytes.size)
    newSource.setDataPosition(0)
    val data = mutableMapOf<K, MutableList<V>>()
    var n = newSource.readInt()
    while (n > 0) {
        val key: K = newSource.readValue(keyClassLoader) as K
        val list = mutableListOf<V>()
        val s = newSource.readInt()
        for (index in 0 until s) {
            val p = newSource.readParcelable<V>(valueClassLoader)
            if (p != null) {
                list.add(p)
            }
        }
        data[key] = list
        n--
    }
    newSource.recycle()
    return data
}


/**
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <K, V : Parcelable> Map<K, V>.deepCloneMapOfParcelable(): Map<K, V?> {
    val source = Parcel.obtain()
    var size = entries.size
    source.writeInt(size)
    var keyClassLoader: ClassLoader? = null
    var classLoader: ClassLoader? = null
    for ((key, value) in entries) {
        if (keyClassLoader == null && key != null) {
            keyClassLoader = key.javaClass.classLoader
        }
        source.writeValue(key)
        source.writeValue(value)
        if (classLoader == null) {
            classLoader = value.javaClass.classLoader
        }
        size--
    }
    if (size != 0) {
        throw BadParcelableException("Map size does not match number of entries!")
    }
    val bytes: ByteArray = source.marshall()
    source.recycle()
    val newSource = Parcel.obtain()
    newSource.unmarshall(bytes, 0, bytes.size)
    newSource.setDataPosition(0)
    val data = mutableMapOf<K, V?>()
    var n = newSource.readInt()
    while (n > 0) {
        val key: K = newSource.readValue(keyClassLoader) as K
        val value: V? = newSource.readValue(classLoader) as V?
        data[key] = value
        n--
    }
    newSource.recycle()
    return data
}


/**
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <K : Any, V : Any> Map<K, V>.deepClone(): Map<K, V>? {
    try {
        ByteArrayOutputStream().use { byteOut ->
            ObjectOutputStream(byteOut).use { out ->
                out.writeObject(this)
                out.flush()
                ObjectInputStream(ByteArrayInputStream(byteOut.toByteArray())).use { input ->
                    return this::class.java.cast(input.readObject())
                }
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        return null
    }
}


/**
 * 返回匹配给定 [predicate] 的第一个元素，如果没有找到这样的元素，则返回 `null`。
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param predicate 给定条件操作符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
inline fun <K, V> Map<K, V>.findKey(predicate: (Map.Entry<K, V>) -> Boolean): K? {
    for (item in this) {
        if (predicate(item)) {
            return item.key
        }
    }
    return null
}

/**
 * 返回匹配给定 [predicate] 的所有元素列表，如果没有找到这样的元素，则返回空列表。
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param predicate 给定条件操作符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
inline fun <K, V> Map<K, V>.findKeys(predicate: (Map.Entry<K, V>) -> Boolean): MutableList<K> {
    return findTo(mutableListOf(), predicate)
}

/**
 * 返回匹配给定 [predicate] 的所有元素列表，如果没有找到这样的元素，则返回空列表。
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param destination 目标列表
 * @param predicate 给定条件操作符
 * @author dingpeihua
 * @date 2022/4/11 10:20
 * @version 1.0
 */
inline fun <K, V, M : MutableList<in K>> Map<out K, V>.findTo(
    destination: M,
    predicate: (Map.Entry<K, V>) -> Boolean
): M {
    for (element in this) {
        if (predicate(element)) {
            destination.add(element.key)
        }
    }
    return destination
}

/**
 * 集合转成String输出
 *
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
inline fun <K, V, R> Map<K, V>.splicing(action: (Map.Entry<K, V>) -> R): String {
    return splicing(",", action)
}

/**
 * 集合转成String输出
 *
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param separator 分隔符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
inline fun <K, V, R> Map<K, V>.splicing(separator: String, action: (Map.Entry<K, V>) -> R): String {
    val sb = StringBuilder()
    for ((index, item) in this.entries.withIndex()) {
        val result = action(item)
        if (result != null) {
            sb.append(result)
            if (index < size - 1) {
                sb.append(separator)
            }
        }
    }
    return sb.toString()
}

fun Map<String, Any?>.toBundle(): Bundle = bundleOf(*this.toList().toTypedArray())

fun <K, V, T> Map<K, V>.convert(p: (Map.Entry<K, V>) -> Map.Entry<K, T>): Map<K, T> {
    val result = mutableMapOf<K, T>()
    for (item in this) {
        val r = p(item)
        result[r.key] = r.value
    }
    return result
}