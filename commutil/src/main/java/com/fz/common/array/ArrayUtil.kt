@file:JvmName("ArrayUtil")
@file:JvmMultifileClass

package com.fz.common.array

import android.os.Parcel
import android.os.Parcelable
import java.io.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <T> Array<T>?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return this != null && size > 0 && isNotEmpty()
}

/**
 * 数组转成String输出
 *
 * @param <T>   泛型参数，数组中放置的元素数据类型
 * @return 如果集合不为空返回输出字符串，否则返回""
</T> */
@Deprecated("please use splicing method.", ReplaceWith("splicing()"))
fun <T> Array<T>.string(): String {
    return toString(",")
}

/**
 * 集合转成String输出
 *
 * @param list      集合
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param separator 分隔符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
</T> */
@Deprecated("please use splicing method.", ReplaceWith("splicing(separator)"))
fun <T> Array<T>.toString(separator: String): String {
    return splicing(separator)
}

/**
 * 集合转成String输出
 *
 * @param <T>  泛型参数，集合中放置的元素数据类型
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
fun <T> Array<T>.splicing(): String {
    return splicing(",")
}

/**
 * 集合转成String输出
 *
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param separator 分隔符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
fun <T> Array<T>.splicing(separator: String): String {
    val sb = StringBuilder()
    for (index in indices) {
        val result = this[index]
        if (result != null) {
            sb.append(result)
            if (index < size - 1) {
                sb.append(separator)
            }
        }
    }
    return sb.toString()
}

/**
 * 对象深度拷贝
 * [T] extend [Serializable]
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <V : Serializable> Array<V>.deepClone(): Array<V>? {
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
fun <T : Parcelable> Array<T>.deepCloneParcelableArray(): Array<T>? {
    return try {
        val tClass = this[0].javaClass
        val source = Parcel.obtain()
        source.writeTypedArray(this, 0)
        val bytes: ByteArray = source.marshall()
        source.recycle()
        val newSource = Parcel.obtain()
        newSource.unmarshall(bytes, 0, bytes.size)
        newSource.setDataPosition(0)
        val name = tClass.toString()
        val f = tClass.getField("CREATOR")
        val creator: Parcelable.Creator<T> = f[null] as Parcelable.Creator<T>?
            ?: throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
        val data = newSource.createTypedArray(creator)
        newSource.recycle()
        data
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 返回匹配给定 [predicate] 的第一个元素，如果未找到元素，则返回默认返回索引[0]元素。
 * @author dingpeihua
 * @date 2022/4/19 14:26
 * @version 1.0
 */
fun <T> Array<T>.findFirst(predicate: (T) -> Boolean): T {
    for (item in this) {
        if (predicate(item)) {
            return item
        }
    }
    return this[0]
}