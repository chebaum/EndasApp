package com.tistory.chebaum.endasapp.listeners;

/**
 * Created by cheba on 2018-01-31.
 */

public interface OnChannelCheckStateChangedListener {
    void updateChildrenCheckState(int firstChildFlattenedIndex, int numChildren);
}
