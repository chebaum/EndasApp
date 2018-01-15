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
    private int itemLayout;
    private static SparseBooleanArray selectedItems;
    private static boolean selection_mode;


    public MyRecyclerAdapter(List<Channel> channels, int itemLayout){
        this.channels=channels;
        this.itemLayout=itemLayout;
        selectedItems=new SparseBooleanArray();
        selection_mode=false;
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
        holder.itemView.setSelected(selectedItems.get(position,false));
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView c_name;

        public ViewHolder(View itemView){
            super(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selection_mode=true;
                    Toast.makeText(v.getContext(), "long click at adapter", Toast.LENGTH_SHORT).show();

                    if (selectedItems.get(getAdapterPosition(), false)) {
                        selectedItems.delete(getAdapterPosition());
                        v.setSelected(false);
                        ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(v.getResources().getColor(R.color.colorWhite));
                        return true;
                    } else {
                        selectedItems.put(getAdapterPosition(), true);
                        v.setSelected(true);
                        ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(v.getResources().getColor(R.color.colorPrimaryDark));
                        return true;
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selection_mode) {
                        // 이미 선택된 아이템이라면, 취소
                        if (selectedItems.get(getAdapterPosition(), false)) {
                            selectedItems.delete(getAdapterPosition());
                            v.setSelected(false);
                            ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(v.getResources().getColor(R.color.colorWhite));

                        } else {
                            selectedItems.put(getAdapterPosition(), true);
                            v.setSelected(true);
                            ((TextView) v.findViewById(R.id.row_c_name)).setTextColor(v.getResources().getColor(R.color.colorPrimaryDark));

                        }
                    }
                }
            });

            c_name = (TextView)itemView.findViewById(R.id.row_c_name);
        }
    }

    public static boolean getSelection_mode() {
        return selection_mode;
    }

    public static void setSelection_mode(boolean selection_mode) {
        MyRecyclerAdapter.selection_mode = selection_mode;
    }
}
