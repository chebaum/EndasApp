package com.tistory.chebaum.endasapp;

import android.widget.ExpandableListView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableList;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;
import com.tistory.chebaum.endasapp.listeners.OnChannelCheckStateChangedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자가 홈화면에서 재생할 채널을 선택하면 체크박스에 체크가 되고 다시 선택하면 체크가 사라지는데,
 * 그때 선택여부를 저장하고 보여주는 역할을 하는 check controller이다.
 * https://github.com/thoughtbot/expandable-recycler-view
 * 홈화면의 recyclerview 구조는 위의 링크에서 많이 참조해서 작성하였습니다
 * 이 ChannelCheckController 클래스 또한 위의 링크에서 가져와 활용하였습니다.
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
    public List<Position> getCheckedListPositions(){
        List<Position> selected=new ArrayList<>();
        for(int i=0;i<expandableList.groups.size();i++){
            if(expandableList.groups.get(i) instanceof myCheckedExpandableGroup){
                myCheckedExpandableGroup group=(myCheckedExpandableGroup)expandableList.groups.get(i);
                for(int j=0;j<group.getItemCount();j++){
                    if(group.isChannelChecked(j)){
                        Position pos=new Position(i, j);
                        selected.add(pos);
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
