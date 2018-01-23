package com.tistory.chebaum.endasapp;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import static android.view.animation.Animation.RELATIVE_TO_SELF;


/**
 * Created by cheba on 2018-01-23.
 */

public class myGroupViewHolder extends GroupViewHolder {
    public ImageView arrow;
    public TextView g_name;
    public TextView g_ip;
    public TextView g_reg_date;

    public myGroupViewHolder(View itemView){
        super(itemView);
        g_name = (TextView)itemView.findViewById(R.id.row_g_name);
        g_ip=(TextView)itemView.findViewById(R.id.row_g_ip);
        g_reg_date=((TextView)itemView.findViewById(R.id.row_g_reg_date));
        arrow=(ImageView)itemView.findViewById(R.id.parent_list_item_expand_arrow);
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        super.collapse();
    }

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

}

