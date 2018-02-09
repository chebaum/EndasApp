package com.tistory.chebaum.endasapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * TODO 아직은 채널 번호, 이름, 속해있는 장비의 번호 - 총 3개의 멤버변수 만을 가집니다.
 * 실제로 영상 재생과 관련된 멤버변수 추가 필요함
 * Group(장비)클래스에 g_channel_list라는 이름으로 List<Channel> 멤버변수가 있는데 거기에 사용됨.
 */

public class Channel implements Parcelable{
    private int c_num;
    private String c_title;
    private long c_group_id; // id number of the parent DB
    // TODO need more member variables!  -   ex) private String c_urlPath;

    public Channel(int c_num, String c_title, long c_group_id) {
        this.c_num = c_num;
        this.c_title = c_title;
        this.c_group_id = c_group_id;
    }

    public Channel(Parcel in) {
        try {
            c_num = in.readInt();
            c_title = in.readString();
            c_group_id = in.readLong();
        }catch (Exception e){
            // TODO 코드내에 printStackTrace() 있으면 보안문제있다함 - 마무리단계에서 제거해야
            e.printStackTrace();
        }
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(c_num);
        dest.writeString(c_title);
        dest.writeLong(c_group_id);
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
    public long getC_group_id() {
        return c_group_id;
    }
    public void setC_group_id(long c_group_id) {
        this.c_group_id = c_group_id;
    }
}
