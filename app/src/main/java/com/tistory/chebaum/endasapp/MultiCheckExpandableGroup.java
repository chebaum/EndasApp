package com.tistory.chebaum.endasapp;

import android.os.Parcel;

import java.util.List;

/**
 * Created by cheba on 2018-01-31.
 * 홈화면의 리스트(RecyclerView list)를 구성할때 사용되는 클래스.
 * 거의 대부분의 리스트관련 클래스들은 최종적으로 CheckableChannelRecyclerViewAdapter 클래스에서 사용된다.
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
