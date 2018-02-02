package com.tistory.chebaum.endasapp.listeners;

import android.view.View;

import com.tistory.chebaum.endasapp.myCheckedExpandableGroup;

/**
 * Created by cheba on 2018-01-31.
 */

public interface OnCheckChannelClickListener {
    void onCheckChildClick(View v, boolean checked, myCheckedExpandableGroup group, int childIndex);
}
