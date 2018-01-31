package com.tistory.chebaum.endasapp;

import android.view.View;

/**
 * Created by cheba on 2018-01-31.
 */

public interface OnCheckChannelClickListener {
    void onCheckChildClick(View v, boolean checked, myCheckedExpandableGroup group, int childIndex);
}
