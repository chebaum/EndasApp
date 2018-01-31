package com.tistory.chebaum.endasapp;

import android.os.Parcel;

import java.util.List;

/**
 * Created by cheba on 2018-01-31.
 */

public class MultiCheckExpandableGroup extends myCheckedExpandableGroup {
    public MultiCheckExpandableGroup(String title, List items) {
        super(title, items);
    }

    @Override
    public void onChannelClicked(int channelIndex, boolean checked) {
        if(checked)
            checkChannel(channelIndex);
        else
            unCheckChannel(channelIndex);
    }

    public MultiCheckExpandableGroup(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
