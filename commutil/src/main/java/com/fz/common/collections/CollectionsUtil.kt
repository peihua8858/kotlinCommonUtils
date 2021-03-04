@file:JvmName("CollectionsUtil")

package com.fz.common.collections

import android.os.Parcel
import android.os.Parcelable
import com.fz.common.utils.toString
import java.io.*
import java.util.*
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
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <T : Parcelable> Set<T>.deepCloneParcelableSet(): Set<T>? {
    return try {
        val tClass = this.iterator().next().javaClass
        val source = Parcel.obtain()
        val aa = arrayListOf<T>()
        for (v in this) {
            aa.add(v)
        }
        source.writeTypedArray(aa.toArray(arrayOfNulls(size)), 0)
        val bytes: ByteArray = source.marshall()
        source.recycle()
        val newSource = Parcel.obtain()
        newSource.unmarshall(bytes, 0, bytes.size)
        newSource.setDataPosition(0)
        val name = tClass.toString()
        val data = mutableSetOf<T>()
        val f = tClass.getField("CREATOR")
        val creator: Parcelable.Creator<T> = f[null] as Parcelable.Creator<T>?
                ?: throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
        val a: Array<T>? = newSource.createTypedArray(creator)
        if (!a.isNullOrEmpty()) {
            for (v in a) {
                data.add(v)
            }
        }
        newSource.recycle()
        data
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <T : Serializable> Collection<T>.deepClone(): Collection<T>? {
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
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <T : Parcelable> List<T>.deepCloneParcelableList(): List<T>? {
    return try {
        val tClass = this[0].javaClass
        val source = Parcel.obtain()
        source.writeTypedList(this)
        val bytes: ByteArray = source.marshall()
        source.recycle()
        val newSource = Parcel.obtain()
        newSource.unmarshall(bytes, 0, bytes.size)
        newSource.setDataPosition(0)
        val name = tClass.toString()
        val data = arrayListOf<T>()
        val f = tClass.getField("CREATOR")
        val creator: Parcelable.Creator<T> = f[null] as Parcelable.Creator<T>?
                ?: throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
        newSource.readTypedList(data, creator)
        newSource.recycle()
        data
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}