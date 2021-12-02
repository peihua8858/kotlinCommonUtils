@file:JvmName("AnyUtils")
@file:JvmMultifileClass

package com.fz.common.utils

import android.os.Parcel
import android.os.Parcelable
import com.fz.common.reflect.getDeclaredField
import java.io.*
import java.lang.reflect.Field
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

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
        val source = Parcel.obtain()// Must be one or more of: Parcelable.PARCELABLE_WRITE_RETURN_VALUE
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

/**
 * 将[source]源数据对象（被复制对象）的不为空的数据值拷贝到[this]目标对象中对应的属性值
 * [this] 是目标对象，也是返回对象
 * @param source 源对象
 * @return [this]
 * @author dingpeihua
 * @date 2019/3/21 09:17
 * @version 1.0
 */
fun <T : Any> T.copyField(source: T): T {
    val sourceFields: Array<Field> = source.javaClass.declaredFields
    val targetFields: Array<Field> = javaClass.declaredFields
    for (i in sourceFields.indices) {
        try {
            val sourceField = sourceFields[i]
            val targetField = targetFields[i]
            sourceField.isAccessible = true
            targetField.isAccessible = true
            val sourceValue = sourceField[source]
            val targetValue = targetField[this]
            if (sourceValue != null) {
                if (sourceValue.checkData() || targetValue == null) {
                    if ("" != sourceValue) {
                        //不替换整型基本类型为0的数据
                        if (sourceField.type == Int::class.javaPrimitiveType && 0 == sourceValue as Int) {
                            continue
                        }
                        targetField[this] = sourceValue
                    }
                } else {
                    targetValue.copyField(sourceValue)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return this
}

/**
 * 检查当前对象类型是否是基本数据类型（包括[Date]、
 * [BigDecimal]、[BigInteger]及[Character]）
 *
 * @param value
 * @author dingpeihua
 * @date 2019/4/1 14:29
 * @version 1.0
 */
fun Any.checkData(): Boolean {
    return this.javaClass.checkData()
}

/**
 * 检查当前类型是否是基本数据类型（包括[Date]、
 * [BigDecimal]、[BigInteger]及[Character]）
 *
 * @author dingpeihua
 * @date 2019/4/1 14:29
 * @version 1.0
 */
fun Class<*>.checkData(): Boolean {
    return this == String::class.java || this == Int::class.java
            || this == Int::class.javaPrimitiveType
            || this == Byte::class.java || this == Byte::class.javaPrimitiveType
            || this == Long::class.java || this == Long::class.javaPrimitiveType
            || this == Double::class.java || this == Double::class.javaPrimitiveType
            || this == Float::class.java || this == Float::class.javaPrimitiveType
            || this == Char::class.java || this == Short::class.java
            || this == Short::class.javaPrimitiveType || this == BigDecimal::class.java
            || this == BigInteger::class.java || this == Boolean::class.java
            || this == Boolean::class.javaPrimitiveType || this == Date::class.java
            || this.isPrimitive
}