package com.fz.gb.commutil.collection;

import java.util.Collection;

/**
 * Created by caolz on 2018/6/21.
 */
public class CollectionUtil {

    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否包含数据
     * @param collection
     * @return
     */
    public static boolean notEmpty(Collection collection) {
        return !isEmpty(collection);
    }
}
