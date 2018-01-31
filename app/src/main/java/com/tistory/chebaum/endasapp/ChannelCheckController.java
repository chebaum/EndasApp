package com.tistory.chebaum.endasapp;

import android.widget.ExpandableListView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableList;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheba on 2018-01-31.
 */

public class ChannelCheckController {
    private ExpandableList expandableList;
    private OnChannelCheckStateChangedListener channelUpdateListener;
    private List<Integer> initialCheckedPositions;

    public ChannelCheckController(ExpandableList expandableList, OnChannelCheckStateChangedListener listener) {
        this.expandableList = expandableList;
        this.channelUpdateListener = listener;
        initialCheckedPositions=getCheckedPositions();
    }

    public void onChannelCheckChanged(boolean checked, ExpandableListPosition listPosition){
        myCheckedExpandableGroup group=(myCheckedExpandableGroup)expandableList.groups.get(listPosition.groupPos);
        group.onChannelClicked(listPosition.childPos,checked);
        if(channelUpdateListener!=null){
            channelUpdateListener.updateChildrenCheckState(
                    expandableList.getFlattenedFirstChildIndex(listPosition),
                    expandableList.getExpandableGroupItemCount(listPosition)
            );
        }
    }
    public void checkChild(boolean checked, int groupIndex, int channelIndex){
        myCheckedExpandableGroup group=(myCheckedExpandableGroup)expandableList.groups.get(groupIndex);
        group.onChannelClicked(channelIndex, checked);
        if(channelUpdateListener!=null){
            boolean isGroupExpanded=expandableList.expandedGroupIndexes[groupIndex];
            if(isGroupExpanded){
                channelUpdateListener.updateChildrenCheckState(
                        expandableList.getFlattenedFirstChildIndex(groupIndex),group.getItemCount()
                );
            }
        }
    }

    public boolean isChildChecked(ExpandableListPosition listPosition){
        myCheckedExpandableGroup group=(myCheckedExpandableGroup)expandableList.groups.get(listPosition.groupPos);
        return group.isChannelChecked(listPosition.childPos);
    }

    public List<Integer> getCheckedPositions(){
        List<Integer> selected=new ArrayList<>();
        for(int i=0;i<expandableList.groups.size();i++){
           if(expandableList.groups.get(i) instanceof myCheckedExpandableGroup){
               myCheckedExpandableGroup group=(myCheckedExpandableGroup)expandableList.groups.get(i);
               for(int j=0;j<group.getItemCount();j++){
                   if(group.isChannelChecked(j)){
                       long packedPosition= ExpandableListView.getPackedPositionForChild(i,j);
                       selected.add(expandableList.getFlattenedChildIndex(packedPosition));
                   }
               }
           }
        }
        return selected;
    }
    public boolean checksChanged(){
        return !initialCheckedPositions.equals(getCheckedPositions());
    }
    public void clearCheckStates(){
        for(int i=0;i<expandableList.groups.size();i++){
            myCheckedExpandableGroup group=(myCheckedExpandableGroup)expandableList.groups.get(i);
            group.clearSelections();
        }
    }
}
