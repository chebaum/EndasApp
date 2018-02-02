package com.tistory.chebaum.endasapp.listeners;

import android.view.View;

/**
 * Created by cheba on 2018-01-31.
 */

public interface OnChannelCheckChangedListener {
    /**
     * @param checked The current checked state of the view
     * @param flatPos The flat position (raw index) of the the child within the RecyclerView
     */
    void onChildCheckChanged(View view, boolean checked, int flatPos);

}
