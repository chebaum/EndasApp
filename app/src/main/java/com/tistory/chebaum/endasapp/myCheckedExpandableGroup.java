package com.tistory.chebaum.endasapp;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cheba on 2018-01-23.
 * 홈화면의 리스트(RecyclerView list)를 구성할때 사용되는 클래스.
 * 거의 대부분의 리스트관련 클래스들은 최종적으로 CheckableChannelRecyclerViewAdapter 클래스에서 사용된다.
 */

public abstract class myCheckedExpandableGroup extends ExpandableGroup {
    public boolean[] selectedChannels;

    public myCheckedExpandableGroup(String title, List items) {
        super(title, items);
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
}
