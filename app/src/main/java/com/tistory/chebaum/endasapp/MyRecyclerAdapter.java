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

public class MyRecyclerAdapter extends ExpandableRecyclerViewAdapter<myGroupViewHolder,myChannelViewHolder> {

    private List<Group> groups;
    private List<Group> selected_groups;
    View view;

    public MyRecyclerAdapter(List<? extends ExpandableGroup> g, List<Group> groups, List<Group> selected_groups, View view) {
        super(g);
        this.groups=groups;
        this.selected_groups = selected_groups;
        this.view=view;
    }

    @Override
    public myGroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_row_layout,viewGroup,false);
        return new myGroupViewHolder(view);
    }

    @Override
    public myChannelViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.channel_row_layout,viewGroup,false);
        return new myChannelViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(myGroupViewHolder holder, int flatPosition, ExpandableGroup expandableGroupgroup) {
        Group group=(Group)expandableGroupgroup;
        //Group group = groups.get(flatPosition);
        holder.g_id.setText(Long.toString(group.getG_id()));
        holder.g_name.setText(group.getG_title());
        holder.g_ip.setText(group.getG_url());
        holder.g_ip.setTextSize(holder.g_name.getTextSize()/4);
        holder.g_reg_date.setText("등록일: 2018.01.18");
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
    public void onBindChildViewHolder(myChannelViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Channel ch = ((Group)group).getItems().get(childIndex);
        holder.c_num.setText(Integer.toString(ch.getC_num()));
        holder.c_title.setText(ch.getC_title());
    }

/*
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ViewHolder(view);
    }
*/
/*
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel channel=channels.get(position);
        holder.c_name.setText(channel.getC_title());
        holder.c_ip.setText("172.31.7.11");
        holder.c_ip.setTextSize(holder.c_name.getTextSize()/4);
        holder.c_reg_date.setText("등록일: 2018.01.18");
        holder.c_reg_date.setTextSize(holder.c_name.getTextSize()/4);
        if(selected_channels.contains(channel)){
            ((TextView)holder.itemView.findViewById(R.id.row_c_name)).setTextColor(view.getResources().getColor(R.color.colorBackground));
            holder.itemView.findViewById(R.id.row_layout).setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
        }
        else{
            ((TextView)holder.itemView.findViewById(R.id.row_c_name)).setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            holder.itemView.findViewById(R.id.row_layout).setBackgroundColor(view.getResources().getColor(R.color.colorBackground));
        }
    }
    */

/*
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView c_name;
        public TextView c_ip;
        public TextView c_reg_date;

        public ViewHolder(View itemView){
            super(itemView);
            c_name = (TextView)itemView.findViewById(R.id.row_c_name);
            c_ip=(TextView)itemView.findViewById(R.id.row_c_ip);
            c_reg_date=((TextView)itemView.findViewById(R.id.row_c_reg_date));
        }
    }
*/

}
