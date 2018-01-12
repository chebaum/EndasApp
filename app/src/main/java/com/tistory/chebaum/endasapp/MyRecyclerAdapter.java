package com.tistory.chebaum.endasapp;

import android.support.v7.widget.RecyclerView;
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
    private int itemLayout;


    public MyRecyclerAdapter(List<Channel> channels, int itemLayout){
        this.channels=channels;
        this.itemLayout=itemLayout;
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
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView c_name;

        public ViewHolder(View itemView){
            super(itemView);

            c_name = (TextView)itemView.findViewById(R.id.row_c_name);
        }
    }

}
