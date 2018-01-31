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

/* TODO 이제 이부분은 homeFragment로... 선택된 채널들 모아서 한번에 LiveView한테 넘긴다
   TODO 그리고 1개 이하: 1개짜리 4개이하 4개짜리....이런식으로 구성한다.
        // bundle객체 안에 동영상 재생과 관련된 정보를 담아서 LiveViewFragment에게 전송한다.
        // LiveViewFragment에서는 전달된 정보를 받은뒤 해당 영상을 재생한다.
        LiveViewFragment fragment = new LiveViewFragment();
        Bundle arguments=new Bundle();
        arguments.putString("data", c_title_checkable.getText().toString());
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = ((MainActivity)view.getContext()).getSupportFragmentManager();
        FragmentTransaction transaction =   fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment).commit();
        ((MainActivity)view.getContext()).getSupportActionBar().setTitle("라이브 영상");
        */
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
