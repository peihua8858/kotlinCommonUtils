package com.fz.commutils.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * 商品类型
 * Created by longxiaolong on 2017/12/26.
 */
public class TagsBean implements Parcelable {
    public String title;
    public String color;

    public TagsBean(String json) throws Exception {
        this(new JSONObject(json));
    }

    public TagsBean(String title, String color) {
        this.title = title;
        this.color = color;
    }

    public TagsBean(JSONObject json) {
        color = json.optString("color");
        title = json.optString("title");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TagsBean() {
    }

    @Override
    public String toString() {
        return "TagsBean{" +
                "title='" + title + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.color);
    }

    protected TagsBean(Parcel in) {
        this.title = in.readString();
        this.color = in.readString();
    }

    public static final Creator<TagsBean> CREATOR = new Creator<TagsBean>() {
        @Override
        public TagsBean createFromParcel(Parcel source) {
            return new TagsBean(source);
        }

        @Override
        public TagsBean[] newArray(int size) {
            return new TagsBean[size];
        }
    };
}
