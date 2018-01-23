package com.tistory.chebaum.endasapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cheba on 2018-01-18.
 */

public class Channel implements Parcelable{
    private int c_num;
    private String c_title;
    private int c_group_id; // id number of the parent DB
    // also other attrs...

    public Channel(int c_num, String c_title, int c_group_id) {
        this.c_num = c_num;
        this.c_title = c_title;
        this.c_group_id = c_group_id;
    }

    public Channel(Parcel in) {
        c_num=in.readInt();
        c_title=in.readString();
        c_group_id=in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_num);
        dest.writeString(c_title);
        dest.writeInt(c_group_id);
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel parcel) {
            return new Channel(parcel);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    public int getC_num() {
        return c_num;
    }
    public void setC_num(int c_num) {
        this.c_num = c_num;
    }
    public String getC_title() {
        return c_title;
    }
    public void setC_title(String c_title) {
        this.c_title = c_title;
    }
    public int getC_group_id() {
        return c_group_id;
    }
    public void setC_group_id(int c_group_id) {
        this.c_group_id = c_group_id;
    }
}
