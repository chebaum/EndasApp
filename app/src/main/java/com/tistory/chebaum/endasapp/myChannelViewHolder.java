package com.tistory.chebaum.endasapp;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by cheba on 2018-01-23.
 */

public class myChannelViewHolder extends ChildViewHolder implements View.OnClickListener {
    public TextView c_num;
    public TextView c_title;

    public myChannelViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        c_num=(TextView)itemView.findViewById(R.id.row_c_num);
        c_title=(TextView)itemView.findViewById(R.id.row_c_title);
    }
    @Override
    public void onClick(View view) {
        c_num.setTextColor(Color.BLUE);
        Toast.makeText(view.getContext(),c_title.getText(),Toast.LENGTH_SHORT).show();
    }
}
