package com.tistory.chebaum.endasapp;

/**
 * Created by cheba on 2018-01-18.
 */

public class ChildChannel {
    private int child_c_num;
    private String child_c_title;
    private int child_parent_id; // id number of the parent DB
    // also other attrs...


    public ChildChannel(int child_c_num, String child_c_title, int child_parent_id) {
        this.child_c_num = child_c_num;
        this.child_c_title = child_c_title;
        this.child_parent_id = child_parent_id;
    }

    public int getChild_c_num() { return child_c_num; }
    public void setChild_c_num(int child_c_num) {
        this.child_c_num = child_c_num;
    }
    public String getChild_c_title() {
        return child_c_title;
    }
    public void setChild_c_title(String child_c_title) {
        this.child_c_title = child_c_title;
    }
    public int getChild_parent_id() { return child_parent_id; }
    public void setChild_parent_id(int child_parent_id) { this.child_parent_id = child_parent_id; }
}
