package com.tistory.chebaum.endasapp;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cheba on 2018-01-23.
 */

public abstract class myCheckedExpandableGroup extends ExpandableGroup {
    //private String id;
    //private String ip;
    //private String reg_date;
    public boolean[] selectedChannels;

    public myCheckedExpandableGroup(String title, List items) {
        super(title, items);
        //this.id=id;
        //this.ip = ip;
        //this.reg_date = reg_date;
        selectedChannels=new boolean[items.size()];
        for(int i=0;i<items.size();i++)
            selectedChannels[i]=false;

    }
    public void checkChannel(int channelIndex){ selectedChannels[channelIndex]=true; }
    public void unCheckChannel(int channelIndex){ selectedChannels[channelIndex]=false; }
    public boolean isChannelChecked(int channelIndex){ return selectedChannels[channelIndex]; }

    public void clearSelections(){
        if(selectedChannels!=null)
            Arrays.fill(selectedChannels, false);
    }
    protected myCheckedExpandableGroup(Parcel in){
        super(in);
        selectedChannels=in.createBooleanArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeBooleanArray(selectedChannels);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public abstract void onChannelClicked(int channelIndex, boolean checked);
/*
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }*/
}
