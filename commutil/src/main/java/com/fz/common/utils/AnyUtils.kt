@file:JvmName("AnyUtils")
package com.fz.common.utils

import android.os.Parcel
import android.os.Parcelable
import java.io.*

/**
 * 对象深度拷贝
 * @author dingpeihua
 * @date 2021/3/2 15:29
 * @version 1.0
 */
fun <T : Serializable> T.deepCloneSerializable(): T? {
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

fun <T : Parcelable> T.deepCloneParcelable(): T? {
    return try {
        val tClass = this::class.java
        val source = Parcel.obtain()
        this.writeToParcel(source, this.describeContents())
        val bytes: ByteArray = source.marshall()
        source.recycle()
        val newSource = Parcel.obtain()
        newSource.unmarshall(bytes, 0, bytes.size)
        newSource.setDataPosition(0)
        val name = tClass.toString()
        val f = tClass.getField("CREATOR")
        val creator: Parcelable.Creator<T> = f[null] as Parcelable.Creator<T>?
                ?: throw Exception("Parcelable protocol requires a non-null static Parcelable.Creator object called CREATOR on class $name")
        val p = creator.createFromParcel(newSource) as T
        newSource.recycle()
        p
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}