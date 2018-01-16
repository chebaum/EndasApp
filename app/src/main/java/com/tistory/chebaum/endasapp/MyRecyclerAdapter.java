package com.tistory.chebaum.endasapp;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by cheba on 2018-01-12.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    final private List<Channel> channels;
    final private List<Channel> selected_channels;
    private int itemLayout;
    View view;




    public MyRecyclerAdapter(List<Channel> channels, List<Channel> selected_channels, View view, int itemLayout){
        this.channels=channels;
        this.selected_channels=selected_channels;
        this.itemLayout=itemLayout;
        this.view=view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel channel=channels.get(position);
        holder.c_name.setText(channel.getC_name());
        if(selected_channels.contains(channel)){
            ((TextView)holder.itemView.findViewById(R.id.row_c_name)).setTextColor(view.getResources().getColor(R.color.colorBackground));
            holder.itemView.findViewById(R.id.row_layout).setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
        }
        else{
            ((TextView)holder.itemView.findViewById(R.id.row_c_name)).setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            holder.itemView.findViewById(R.id.row_layout).setBackgroundColor(view.getResources().getColor(R.color.colorBackground));
        }
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView c_name;

        public ViewHolder(View itemView){
            super(itemView);
            c_name = (TextView)itemView.findViewById(R.id.row_c_name);
        }
    }

}
