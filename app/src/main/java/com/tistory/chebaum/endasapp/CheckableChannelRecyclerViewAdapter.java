package com.tistory.chebaum.endasapp;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;
import com.tistory.chebaum.endasapp.listeners.OnChannelCheckChangedListener;
import com.tistory.chebaum.endasapp.listeners.OnChannelCheckStateChangedListener;
import com.tistory.chebaum.endasapp.listeners.OnCheckChannelClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheba on 2018-01-31.
 * RecyclerView에서 장비 및 채널 항목들을 관리해주는 어댑터
 */

public abstract class CheckableChannelRecyclerViewAdapter<GVH extends myGroupViewHolder,CVH extends myChannelViewHolder> extends ExpandableRecyclerViewAdapter<GVH, CVH>
        implements OnChannelCheckChangedListener, OnChannelCheckStateChangedListener {

    private static final String CHECKED_STATE_MAP="channel_check_controller_checked_state_map";

    private ChannelCheckController channelCheckController;
    private OnCheckChannelClickListener channelClickListener;
    public CheckableChannelRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
        channelCheckController=new ChannelCheckController(expandableList,this);
    }

    @Override
    public CVH onCreateChildViewHolder(ViewGroup parent, int viewType) {
        CVH CVH=onCreateCheckChildViewHolder(parent, viewType);
        CVH.setOnChannelCheckedListener(this);
        return CVH;
    }

    @Override
    public void onBindChildViewHolder(CVH holder, int flatPosition, ExpandableGroup group, int childIndex) {
        ExpandableListPosition listPosition = expandableList.getUnflattenedPosition(flatPosition);
        holder.onBindViewHolder(flatPosition,channelCheckController.isChildChecked(listPosition));
        onBindCheckChildViewHolder(holder,flatPosition,(myCheckedExpandableGroup)group, childIndex);
    }

    @Override
    public void onChildCheckChanged(View view, boolean checked, int flatPos) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(flatPos);
        channelCheckController.onChannelCheckChanged(checked, listPos);
        if (channelClickListener != null) {
            channelClickListener.onCheckChildClick(view, checked,
                    (myCheckedExpandableGroup) expandableList.getExpandableGroup(listPos), listPos.childPos);
        }
    }

    @Override
    public void updateChildrenCheckState(int firstChildFlattenedIndex, int numChildren) {
        notifyItemRangeChanged(firstChildFlattenedIndex, numChildren);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(CHECKED_STATE_MAP, new ArrayList<Parcelable>(expandableList.groups));
        super.onSaveInstanceState(savedInstanceState);
    }

    public void checkChannel(boolean checked, int groupIndex, int channelIndex){
        channelCheckController.checkChild(checked, groupIndex, channelIndex);
        if(channelClickListener != null){
            channelClickListener.onCheckChildClick(null, checked, (myCheckedExpandableGroup)expandableList.groups.get(groupIndex),channelIndex);
        }
    }

    public void clearChoices(){ // 홈화면의 '전체 선택 해제' 버튼을 클릭하는 경우 이 함수가 호출됩니다.
        channelCheckController.clearCheckStates();
        //only update the child views that are visible (i.e. their group is expanded)
        for (int i = 0; i < getGroups().size(); i++) {
            ExpandableGroup group = getGroups().get(i);
            if (isGroupExpanded(group)) {
                notifyItemRangeChanged(expandableList.getFlattenedFirstChildIndex(i), group.getItemCount());
            }
        }
    }
    public List<Position> getCheckedItems(){
        List<Position> selected=channelCheckController.getCheckedListPositions();
        return selected;
    }

    public abstract CVH onCreateCheckChildViewHolder(ViewGroup parent, int viewType);

    public void setChildClickListener(OnCheckChannelClickListener listener){
        channelClickListener=listener;
    }

    public abstract void onBindCheckChildViewHolder(CVH holder, int flatPosition, myCheckedExpandableGroup group, int childIndex);
}
