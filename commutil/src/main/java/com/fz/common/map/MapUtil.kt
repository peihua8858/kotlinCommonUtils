@file:JvmName("MapUtil")

package com.fz.common.map

import android.os.BadParcelableException
import android.os.Parcel
import android.os.Parcelable
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
