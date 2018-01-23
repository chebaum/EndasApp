package com.tistory.chebaum.endasapp;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by cheba on 2018-01-23.
 */

public class myExpandableGroup extends ExpandableGroup {
    private String ip;
    private String reg_date;

    public myExpandableGroup(String title, List items, String ip, String reg_date) {
        super(title, items);
        this.ip = ip;
        this.reg_date = reg_date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
}
