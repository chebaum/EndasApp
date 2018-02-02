package com.tistory.chebaum.endasapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by cheba on 2018-01-12.
 */

public class MyRecyclerAdapter extends CheckableChannelRecyclerViewAdapter<myGroupViewHolder,myChannelViewHolder> {

    private List<Group> groups;
    private List<Group> selected_groups;
    View view;

    public MyRecyclerAdapter(List<Group> g, List<Group> groups, List<Group> selected_groups, View view) {
        super(g);
        this.groups=groups;
        this.selected_groups = selected_groups;
        this.view=view;
    }


    @Override
    public myChannelViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_row_layout,parent,false);
        return new myChannelViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(myGroupViewHolder holder, int flatPosition, ExpandableGroup expandableGroup) {
        Group group=(Group)expandableGroup;
        //Group group = groups.get(flatPosition);
        holder.g_id.setText(Long.toString(group.getG_id()));
        holder.g_name.setText(group.getG_title());
        holder.g_ip.setText(group.getG_url());
        holder.g_ip.setTextSize(holder.g_name.getTextSize()/4);
        holder.g_reg_date.setText(R.string.Registration_date);
        holder.g_reg_date.setTextSize(holder.g_name.getTextSize()/4);

        if(selected_groups.contains(group)){
            //TODO 선택된 항목임..색 다르게 설정해준다.
            holder.g_name.setTextColor(view.getResources().getColor(R.color.colorBackground));
            holder.g_ip.setTextColor(view.getResources().getColor(R.color.colorBackground));
            holder.g_reg_date.setTextColor(view.getResources().getColor(R.color.colorBackground));
            holder.g_row_layout.setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
        }
        else{
            //TODO
            holder.g_name.setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            holder.g_ip.setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            holder.g_reg_date.setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            holder.g_row_layout.setBackgroundColor(view.getResources().getColor(R.color.colorBackground));
        }
    }

    @Override
    public void onBindCheckChildViewHolder(myChannelViewHolder holder, int flatPosition, myCheckedExpandableGroup group, int childIndex) {
        final Channel ch = (Channel) group.getItems().get(childIndex);
        //holder.c_num.setText(Integer.toString(ch.getC_num()));
        holder.c_title_checkable.setText(ch.getC_title());
    }

    @Override
    public myGroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_row_layout,viewGroup,false);
        return new myGroupViewHolder(view);
    }


}
