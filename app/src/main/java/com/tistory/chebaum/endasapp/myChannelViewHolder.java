package com.tistory.chebaum.endasapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
        Toast.makeText(view.getContext(),c_title.getText(),Toast.LENGTH_SHORT).show();

        // bundle객체 안에 동영상 재생과 관련된 정보를 담아서 LiveViewFragment에게 전송한다.
        // LiveViewFragment에서는 전달된 정보를 받은뒤 해당 영상을 재생한다.
        LiveViewFragment fragment = new LiveViewFragment();
        Bundle arguments=new Bundle();
        arguments.putString("data",c_title.getText().toString());
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = ((MainActivity)view.getContext()).getSupportFragmentManager();
        FragmentTransaction transaction =   fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment).commit();
        ((MainActivity)view.getContext()).getSupportActionBar().setTitle("라이브 영상");
    }
}
