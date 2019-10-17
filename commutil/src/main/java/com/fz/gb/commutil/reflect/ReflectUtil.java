package com.fz.gb.commutil.reflect;

import androidx.annotation.NonNull;

import com.socks.library.KLog;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;

/**
 * 反射操作相关工具类
 *
 * @author Shyky on 2016/5/9.
 * @version 1.2
 * @email sj1510706@163.com
 * @since 1.0
 */
public final class ReflectUtil {
    /**
     * 构造方法
     *
     * @param tag Log日志标签
     */
    protected ReflectUtil(@NonNull String tag) {
        super();
    }

    public static String getUniqueName(@NonNull Class<?> clz) {
        return clz.getName();
    }

    public static String getSimpleName(@NonNull Class<?> clz) {
        return clz.getSimpleName();
    }

    public static String getSimpleName(@NonNull Field field) {
        return field.getName();
    }

    public static String getSimpleName(@NonNull Method method) {
        return method.getName();
    }

    public static String getClassUniqueName(@NonNull Class<?> clz) {
        return getUniqueName(clz);
    }

    public static String getClassSimpleName(@NonNull Class<?> clz) {
        return getSimpleName(clz);
    }

    public static Object getClassInstance(@NonNull Class<?> clz) {
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getSuperclass(@NonNull Class<?> clz) {
        return clz.getSuperclass();
    }

    public static String getSuperclassName(@NonNull Class<?> clz) {
        return clz.getSuperclass().getName();
    }

    public static String getSuperclassSimpleName(@NonNull Class<?> clz) {
        return clz.getSuperclass().getSimpleName();
    }

    public static String getFieldSimpleName(@NonNull Field field) {
        return getSimpleName(field);
    }

    public static Field[] getDeclaredFields(@NonNull Class<?> clz) {
        return clz.getDeclaredFields();
    }

    public static List<Field> getDeclaredFieldList(@NonNull Class<?> clz) {
        return Arrays.asList(clz.getDeclaredFields());
    }

    public static String getDeclaredFieldName(@NonNull Class<?> clz, @NonNull String fieldName) {
        try {
            return clz.getDeclaredField(fieldName).getName();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getDeclaredFieldType(@NonNull Class<?> clz, @NonNull String fieldName) {
        try {
            return clz.getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDeclaredFieldTypeUniqueName(@NonNull Class<?> clz, @NonNull String fieldName) {
        return getUniqueName(getDeclaredFieldType(clz, fieldName));
    }

    public static String getDeclaredFieldTypeSimpleName(@NonNull Class<?> clz, @NonNull String fieldName) {
        return getSimpleName(getDeclaredFieldType(clz, fieldName));
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @param value     : 将要设置的值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        try {
            if (field != null) {
                //抑制Java对其的检查
                field.setAccessible(true);
                //将 object 中 field 所代表的值 设置为 value
                field.set(object, value);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return : 父类中的属性值
     */

    public static Object getFieldValue(Object object, String fieldName) {
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        try {
            if (field != null) {
                //抑制Java对其的检查
                field.setAccessible(true);
                //获取 object 中 field 所代表的属性值
                return field.get(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
            }
        }
        return null;
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
    public static Field getDeclaredField(Class clazz, String fieldName) {
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static Class<?> getRawType(Type type) {
        if (type == null) throw new NullPointerException("type == null");

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }

    public static Type getCallResponseType(Type returnType) {
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException(
                    "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
        }
        return getParameterUpperBound(0, (ParameterizedType) returnType);
    }

    public static Type getParameterUpperBound(int index, ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        if (index < 0 || index >= types.length) {
            throw new IllegalArgumentException(
                    "Index " + index + " not in range [0," + types.length + ") for " + type);
        }
        Type paramType = types[index];
        if (paramType instanceof WildcardType) {
            return ((WildcardType) paramType).getUpperBounds()[0];
        }
        return paramType;
    }

    public static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class) type;
        }
    }

    public static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            KLog.d("hhhhhhhhhhhhhhhh");
            return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }

    public static void instanceActualTypeArguments(Type type) throws Exception {
        // 参数化类型
        if (type instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            for (int i = 0; i < typeArguments.length; i++) {
                // 类型变量
                if (typeArguments[i] instanceof TypeVariable) {
                    KLog.d("第" + (i + 1) + "个泛型参数类型是类型变量" + typeArguments[i] + "，无法实例化。");
                }
                // 通配符表达式
                else if (typeArguments[i] instanceof WildcardType) {
                    KLog.d("第" + (i + 1) + "个泛型参数类型是通配符表达式" + typeArguments[i] + "，无法实例化。");
                }
                // 泛型的实际类型，即实际存在的类型
                else if (typeArguments[i] instanceof Class) {
                    KLog.d("第" + (i + 1) + "个泛型参数类型是:" + typeArguments[i] + "，可以直接实例化对象");
                }
            }
            // 参数化类型数组或类型变量数组
        } else if (type instanceof GenericArrayType) {
            KLog.d("该泛型类型是参数化类型数组或类型变量数组，可以获取其原始类型。");
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            // 类型变量
            if (componentType instanceof TypeVariable) {
                KLog.d("该类型变量数组的原始类型是类型变量" + componentType + "，无法实例化。");
            }
            // 参数化类型，参数化类型数组或类型变量数组
            // 参数化类型数组或类型变量数组也可以是多维的数组，getGenericComponentType()方法仅仅是去掉最右边的[]
            else {
                // 递归调用方法自身
                instanceActualTypeArguments(componentType);
            }
        } else if (type instanceof TypeVariable) {
            KLog.d("该类型是类型变量");
        } else if (type instanceof WildcardType) {
            KLog.d("该类型是通配符表达式");
        } else if (type instanceof Class) {
            KLog.d("该类型不是泛型类型");
        } else {
            throw new Exception();
        }
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 父类中的方法对象
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }
}