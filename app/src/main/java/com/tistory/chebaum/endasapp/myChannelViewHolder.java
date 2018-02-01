package com.tistory.chebaum.endasapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cheba on 2018-01-23.
 */

public class myChannelViewHolder extends CheckableChannelViewHolder implements View.OnClickListener {
    //public TextView c_num;
    //public TextView c_title_checkable;
    public CheckedTextView c_title_checkable;

    public myChannelViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        //c_num=(TextView)itemView.findViewById(R.id.row_c_num);
        c_title_checkable =(CheckedTextView)itemView.findViewById(R.id.row_c_title_checkable);
    }
    @Override
    public void onClick(View view) {
        getCheckable().toggle();
        if(getOnChannelCheckedListener()!=null){
            getOnChannelCheckedListener().onChildCheckChanged(view, getCheckable().isChecked(), getAdapterPosition());
        }
        Toast.makeText(view.getContext(), c_title_checkable.getText(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public Checkable getCheckable() {
        return c_title_checkable;
    }
    /* need? TODO 지우자
    public void setChannelName(String channelName){
        c_title_checkable.setText(channelName);
    }
    */
}
