package fz.gb.com.commutils.model;

import androidx.core.util.ObjectsCompat;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 首页 trends 的数据
 *
 * @author yeshunda
 * @version 1.1
 * @date 2016/9/23
 * @since 1.0
 */
public class AdapterBean<T> implements MultiItemEntity {
    public int type;
    public T value;
    //-1表示未设置
    public int position = -1;

    public AdapterBean() {
    }
    public AdapterBean(int type) {
        this.type = type;
    }
    public AdapterBean(T value) {
        this.type = 0;
        this.value = value;
    }

    public AdapterBean(int type, T value) {
        this.type = type;
        this.value = value;
    }

    public AdapterBean(int type, T value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdapterBean<?> that = (AdapterBean<?>) o;
        if (type != that.type) return false;
        return ObjectsCompat.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}