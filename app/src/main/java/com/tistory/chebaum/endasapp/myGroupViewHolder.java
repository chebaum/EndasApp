package com.tistory.chebaum.endasapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import static android.view.animation.Animation.RELATIVE_TO_SELF;


/**
 * Created by cheba on 2018-01-23.
 * 홈화면의 리스트(RecyclerView list)를 구성할때 사용되는 클래스.
 * 거의 대부분의 리스트관련 클래스들은 최종적으로 CheckableChannelRecyclerViewAdapter 클래스에서 사용된다.
 */

public class myGroupViewHolder extends GroupViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public ImageView arrow;
    public TextView g_id;
    public TextView g_name;
    public TextView g_ip;
    public TextView g_reg_date;
    public LinearLayout g_row_layout;

    public myGroupViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        g_id= (TextView)itemView.findViewById(R.id.row_g_id);
        g_name = (TextView)itemView.findViewById(R.id.row_g_name);
        g_ip=(TextView)itemView.findViewById(R.id.row_g_ip);
        g_reg_date=((TextView)itemView.findViewById(R.id.row_g_reg_date));
        arrow=(ImageView)itemView.findViewById(R.id.group_list_item_expand_arrow);
        g_row_layout=(LinearLayout)itemView.findViewById(R.id.row_layout);
    }

    @Override
    public void onClick(View v) {
        if(getSelectionMode(v)){
            onLongClick(v);
            return;
        }
        super.onClick(v);
    }

    @Override
    public boolean onLongClick(View view) {
        Group group;
        long id=Long.parseLong(((TextView)view.findViewById(R.id.row_g_id)).getText().toString());
        if(!getSelectionMode(view)) {
            // 처음으로 장비 선택모드에 진입하는경우, homeFragment에 브로드캐스트를 이용하여 알려준다.
            Intent intent = new Intent("group.longclick.action");
            view.getContext().sendBroadcast(intent);
        }
        setSelectionMode(view, true);

        // 새롭게 선택되는경우, 선택된 항목임을 표시하기위하여 배경과 글씨색을 반전시킨다.
        if((group=getGroupIfExistsInSelectedGroup(view, id))!=null){
            ((MainActivity)view.getContext()).get_selected_groups().remove(group);
            g_name.setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            g_ip.setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            g_reg_date.setTextColor(view.getResources().getColor(R.color.colorPrimaryDark));
            g_row_layout.setBackgroundColor(view.getResources().getColor(R.color.colorBackground));
        }
        // 이미 선택되어있던 항목을 다시 선택하는경우, 선택항목에서 제외시기고, 제외되었음을 직관적으로 보여주기 위하여 배경과 글씨색을 원래대로 돌려준다.
        else {
            ((MainActivity)view.getContext()).get_selected_groups().add(getGroupIfExistsInGroup(view, id));
            g_name.setTextColor(view.getResources().getColor(R.color.colorBackground));
            g_ip.setTextColor(view.getResources().getColor(R.color.colorBackground));
            g_reg_date.setTextColor(view.getResources().getColor(R.color.colorBackground));
            g_row_layout.setBackgroundColor(view.getResources().getColor(R.color.colorPrimaryDark));
            Toast.makeText(view.getContext(), Long.toString(id), Toast.LENGTH_SHORT).show();
        }
        ((MainActivity)view.getContext()).invalidateOptionsMenu();
        return true;
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() { animationCollapse(); }

    // TODO 화살표가 갑자기 회전이 안된다... onClickListener의 우선순위 문제로 묻혀버린듯... 실제 사용에는 영향없으니 나중에 해결
    private void animateExpand(){
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animationCollapse(){
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    public boolean getSelectionMode(View view){
        return ((MainActivity)view.getContext()).selection_mode;
    }
    public void setSelectionMode(View view, boolean bool){
        ((MainActivity)view.getContext()).selection_mode=bool;
    }

    public Group getGroupIfExistsInSelectedGroup(View view, long id){
        for(Group group : ((MainActivity)view.getContext()).get_selected_groups()){
            if(group.getG_id()==id)
                return group;
        }
        return null;
    }
    public Group getGroupIfExistsInGroup(View view, long id){
        for(Group group : ((MainActivity)view.getContext()).get_group()){
            if(group.getG_id()==id)
                return group;
        }
        return null;
    }

}

