package com.tistory.chebaum.endasapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

/**
 * Created by cheba on 2018-01-12.
 */

public class MyRecyclerAdapter extends ExpandableRecyclerAdapter<MyRecyclerAdapter.ChannelParentViewHolder,MyRecyclerAdapter.ChannelChildViewHolder> {

    private List<ParentObject> channels;
    private List<ParentObject> selected_channels;
    private int itemLayout;
    View view;

    public MyRecyclerAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
    }

    public MyRecyclerAdapter(Context context, List<ParentObject> channels, List<ParentObject> selected_channels, int itemLayout, View view) {
        super(context, channels);
        this.channels = channels;
        this.selected_channels = selected_channels;
        this.itemLayout = itemLayout;
        this.view = view;
    }



    @Override
    public ChannelParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_row_layout,viewGroup,false);
        return new ChannelParentViewHolder(view);
    }

    public void onBindParentViewHolder(ChannelParentViewHolder parentViewHolder, int i, Object obj) {
        Channel channel=(Channel)obj;

        parentViewHolder.c_name.setText(channel.getC_title());
        parentViewHolder.c_ip.setText("172.31.7.11");
        parentViewHolder.c_ip.setTextSize(parentViewHolder.c_name.getTextSize()/4);
        parentViewHolder.c_reg_date.setText("등록일: 2018.01.18");
        parentViewHolder.c_reg_date.setTextSize(parentViewHolder.c_name.getTextSize()/4);
        if(selected_channels.contains(channel)){
            ((TextView)parentViewHolder.itemView.findViewById(R.id.row_c_name)).setTextColor(view.getResources().getColor(R.color.colorBackground));
            parentViewHolder.itemView.findViewById(R.id.row_layout).setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
        }
        else{
            ((TextView)parentViewHolder.itemView.findViewById(R.id.row_c_name)).setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            parentViewHolder.itemView.findViewById(R.id.row_layout).setBackgroundColor(view.getResources().getColor(R.color.colorBackground));
        }
    }

    @Override
    public ChannelChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_row_layout,viewGroup,false);
        return new ChannelChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ChannelChildViewHolder childViewHolder, int i, Object obj) {
        ChildChannel cChannel=(ChildChannel)obj;

        //childViewHolder.child_c_num.setText(cChannel.getChild_c_num());
        //childViewHolder.child_c_title.setText(cChannel.getChild_c_title());
        childViewHolder.child_c_num.setText("7");
        childViewHolder.child_c_title.setText("Channel 7");
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

    @Override
    public int getItemCount() {
        return channels.size();
    }
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
    public static class ChannelParentViewHolder extends ParentViewHolder{

        public TextView c_name;
        public TextView c_ip;
        public TextView c_reg_date;

        public ChannelParentViewHolder(View itemView){
            super(itemView);
            c_name = (TextView)itemView.findViewById(R.id.row_c_name);
            c_ip=(TextView)itemView.findViewById(R.id.row_c_ip);
            c_reg_date=((TextView)itemView.findViewById(R.id.row_c_reg_date));
        }
    }

    public static class ChannelChildViewHolder extends ChildViewHolder{

        public TextView child_c_num;
        public TextView child_c_title;

        public ChannelChildViewHolder(View itemView){
            super(itemView);
            child_c_num = (TextView)itemView.findViewById(R.id.child_row_c_num);
            child_c_title=(TextView)itemView.findViewById(R.id.child_row_c_title);
        }
    }

}
