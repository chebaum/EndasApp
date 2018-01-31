package com.tistory.chebaum.endasapp;

/**
 * Created by cheba on 2018-01-31.
 */

public interface OnChannelCheckStateChangedListener {
    void updateChildrenCheckState(int firstChildFlattenedIndex, int numChildren);
}
