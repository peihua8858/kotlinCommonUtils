@file:JvmName("ReflectUtil")
@file:JvmMultifileClass
package com.fz.common.reflect

import com.socks.library.KLog
import java.lang.reflect.*
import java.util.*


/**
 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
 *
 * @param object    : 子类对象
 * @param fieldName : 父类中的属性名
 * @param value     : 将要设置的值
 */
fun Any?.setFieldValue(fieldName: String?, value: Any?) {
    //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
    val field = getDeclaredField(fieldName)
    try {
        if (field != null) {
            //抑制Java对其的检查
            field.isAccessible = true
            //将 object 中 field 所代表的值 设置为 value
            field[this] = value
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
 *
 * @param object    : 子类对象
 * @param fieldName : 父类中的属性名
 * @return : 父类中的属性值
 */
fun Any?.getFieldValue(fieldName: String?): Any? {
    if (this == null || fieldName.isNullOrEmpty()) {
        return null
    }
    //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
    val field = getDeclaredField(fieldName)
    try {
        if (field != null) {
            //抑制Java对其的检查
            field.isAccessible = true
            //获取 object 中 field 所代表的属性值
            return field[this]
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * 反射获取对象的私有属性
 *
 * @param object    需要反射的对象
 * @param fieldName 属性名称
 * @return 返回当前对象的私有属性
 * @author dingpeihua
 * @date 2016/5/5 8:51
 * @version 1.0
 */
fun Any?.getDeclaredField(fieldName: String?): Field? {
    val clazz = this?.javaClass
    return clazz.getDeclaredField(fieldName)
}

/**
 * 反射获取类的私有属性
 *
 * @param clazz     需要反射的类
 * @param fieldName 属性名称
 * @return 返回当前对象的私有属性
 * @author dingpeihua
 * @date 2016/5/5 8:53
 * @version 1.0
 */
fun Class<*>?.getDeclaredField(fieldName: String?): Field? {
    if (this == null || fieldName.isNullOrEmpty()) {
        return null
    }
    var clazz = this
    var field: Field?
    while (clazz != Any::class.java) {
        try {
            field = clazz?.getDeclaredField(fieldName)
            field?.isAccessible = true
            return field
        } catch (e: Exception) {
        }
        clazz = clazz?.superclass
    }
    return null
}

/**
 * 循环向上转型, 获取对象的 DeclaredMethod
 *
 * @param object         : 子类对象
 * @param methodName     : 父类中的方法名
 * @param parameterTypes : 父类中的方法参数类型
 * @return 父类中的方法对象
 */
fun Any?.getDeclaredMethod(methodName: String?, vararg parameterTypes: Class<*>?): Method? {
    if (this == null || methodName.isNullOrEmpty()) {
        return null
    }
    var method: Method? = null
    var clazz: Class<*> = javaClass
    while (clazz != Any::class.java) {
        try {
            method = clazz.getDeclaredMethod(methodName, *parameterTypes)
            return method
        } catch (e: Exception) {
            //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
            //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
        }
        clazz = clazz.superclass
    }
    return null
}

fun Type?.getClass(i: Int): Class<*>? {
    return when (this) {
        is ParameterizedType -> { // 处理泛型类型
            getGenericClass(i)
        }
        is TypeVariable<*> -> {
            this.bounds[0].getClass(0) // 处理泛型擦拭对象
        }
        else -> { // class本身也是type，强制转型
            this as Class<*>?
        }
    }
}

fun ParameterizedType.getGenericClass(i: Int): Class<*>? {
    return when (val genericClass: Any = this.actualTypeArguments[i]) {
        is ParameterizedType -> { // 处理多级泛型
            genericClass.rawType as Class<*>
        }
        is GenericArrayType -> { // 处理数组泛型
            genericClass.genericComponentType as Class<*>
        }
        is TypeVariable<*> -> { // 处理泛型擦拭对象
            KLog.d("hhhhhhhhhhhhhhhh")
            genericClass.bounds[0].getClass(0)
        }
        else -> {
            genericClass as Class<*>
        }
    }
}