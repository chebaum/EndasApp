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
 * 홈화면의 리스트(RecyclerView list)를 구성할때 사용되는 클래스.
 * 거의 대부분의 리스트관련 클래스들은 최종적으로 CheckableChannelRecyclerViewAdapter 클래스에서 사용된다.
 */

public class myChannelViewHolder extends CheckableChannelViewHolder implements View.OnClickListener {
    public CheckedTextView c_title_checkable;

    public myChannelViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
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
}
