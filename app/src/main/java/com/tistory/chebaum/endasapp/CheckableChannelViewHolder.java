package com.tistory.chebaum.endasapp;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.tistory.chebaum.endasapp.listeners.OnChannelCheckChangedListener;

import android.view.View;
import android.widget.Checkable;

/**
 * Created by cheba on 2018-01-31.
 */

public abstract class CheckableChannelViewHolder extends ChildViewHolder {

    private OnChannelCheckChangedListener listener;
    private Checkable checkable;

    public CheckableChannelViewHolder(View itemView) {
        super(itemView);
        //itemView.setOnClickListener(this);
    }
    public void onBindViewHolder(int flatPos, boolean checked){
        checkable = getCheckable();
        checkable.setChecked(checked);
    }
/*
TODO 제대로 작동안하면 myChannelViewHolder에서 다시 빼온다
    @Override
    public void onClick(View view) {
        checkable.toggle();
        if(listener!=null){
            listener.onChildCheckChanged(view, checkable.isChecked(), getAdapterPosition());
        }
    }
*/
    public void setOnChannelCheckedListener(OnChannelCheckChangedListener listener){
        this.listener=listener;
    }
    public OnChannelCheckChangedListener getOnChannelCheckedListener(){
        return this.listener;
    }

    public abstract Checkable getCheckable();
}
