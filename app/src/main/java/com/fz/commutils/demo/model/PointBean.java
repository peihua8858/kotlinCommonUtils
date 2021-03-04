package com.fz.commutils.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * 积分列表数据对象
 *
 * @author yeshunda
 * @version 1.1
 * @date 2016/8/31
 * @since 1.0
 */
public class PointBean implements Parcelable, MultiItemEntity,Serializable {
    private String adddate;
    private String balance;
    private String income;
    private String json_data;
    private String note;
    private String outgo;
    /**
     * wh_update_time : 2018-06-25 19:50:02
     */

    private String wh_update_time;

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getJson_data() {
        return json_data;
    }

    public void setJson_data(String json_data) {
        this.json_data = json_data;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOutgo() {
        return outgo;
    }

    public void setOutgo(String outgo) {
        this.outgo = outgo;
    }

    public PointBean() {
    }

    @Override
    public String toString() {
        return "PointBean{" +
                "adddate='" + adddate + '\'' +
                ", balance='" + balance + '\'' +
                ", income='" + income + '\'' +
                ", json_data='" + json_data + '\'' +
                ", note='" + note + '\'' +
                ", outgo='" + outgo + '\'' +
                ", wh_update_time='" + wh_update_time + '\'' +
                '}';
    }

    public String getWh_update_time() {
        return wh_update_time;
    }

    public void setWh_update_time(String wh_update_time) {
        this.wh_update_time = wh_update_time;
    }

    @Override
    public int getItemType() {
        return 222;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adddate);
        dest.writeString(this.balance);
        dest.writeString(this.income);
        dest.writeString(this.json_data);
        dest.writeString(this.note);
        dest.writeString(this.outgo);
        dest.writeString(this.wh_update_time);
    }

    protected PointBean(Parcel in) {
        this.adddate = in.readString();
        this.balance = in.readString();
        this.income = in.readString();
        this.json_data = in.readString();
        this.note = in.readString();
        this.outgo = in.readString();
        this.wh_update_time = in.readString();
    }

    public static final Creator<PointBean> CREATOR = new Creator<PointBean>() {
        @Override
        public PointBean createFromParcel(Parcel source) {
            return new PointBean(source);
        }

        @Override
        public PointBean[] newArray(int size) {
            return new PointBean[size];
        }
    };
}