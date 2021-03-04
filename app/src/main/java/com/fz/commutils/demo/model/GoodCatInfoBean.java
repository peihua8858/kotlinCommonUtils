package com.fz.commutils.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 项目名称:Zaful-Android
 * 类描述：商品分类信息实体bean
 * 创建人：Created by heshuilin
 * 创建时间:2018/8/22 15:17
 */
public class GoodCatInfoBean implements Parcelable {
    public String first_cat_name;
    public String snd_cat_name;
    public String third_cat_name;
    public String four_cat_name;

    public String getFirst_cat_name() {
        return first_cat_name;
    }

    public void setFirst_cat_name(String first_cat_name) {
        this.first_cat_name = first_cat_name;
    }

    public String getSnd_cat_name() {
        return snd_cat_name;
    }

    public void setSnd_cat_name(String snd_cat_name) {
        this.snd_cat_name = snd_cat_name;
    }

    public String getThird_cat_name() {
        return third_cat_name;
    }

    public void setThird_cat_name(String third_cat_name) {
        this.third_cat_name = third_cat_name;
    }

    public String getFour_cat_name() {
        return four_cat_name;
    }

    public void setFour_cat_name(String four_cat_name) {
        this.four_cat_name = four_cat_name;
    }

    public GoodCatInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.first_cat_name);
        dest.writeString(this.snd_cat_name);
        dest.writeString(this.third_cat_name);
        dest.writeString(this.four_cat_name);
    }

    protected GoodCatInfoBean(Parcel in) {
        this.first_cat_name = in.readString();
        this.snd_cat_name = in.readString();
        this.third_cat_name = in.readString();
        this.four_cat_name = in.readString();
    }

    public static final Creator<GoodCatInfoBean> CREATOR = new Creator<GoodCatInfoBean>() {
        @Override
        public GoodCatInfoBean createFromParcel(Parcel source) {
            return new GoodCatInfoBean(source);
        }

        @Override
        public GoodCatInfoBean[] newArray(int size) {
            return new GoodCatInfoBean[size];
        }
    };
}
