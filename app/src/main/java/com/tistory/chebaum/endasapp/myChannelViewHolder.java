package com.tistory.chebaum.endasapp;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by cheba on 2018-01-23.
 */

public class myChannelViewHolder extends ChildViewHolder {
    public TextView c_num;
    public TextView c_title;

    public myChannelViewHolder(View itemView){
        super(itemView);
        c_num=(TextView)itemView.findViewById(R.id.row_c_num);
        c_title=(TextView)itemView.findViewById(R.id.row_c_title);
    }
}
