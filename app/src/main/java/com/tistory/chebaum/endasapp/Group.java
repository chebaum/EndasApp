package com.tistory.chebaum.endasapp;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by cheba on 2018-01-11.
 */

public class Group extends ExpandableGroup<Channel>{
    private int g_id;
    private String g_title;
    private String g_url;
    private int g_web_port;
    private int g_video_port;
    private String g_login_id;
    private String g_login_pw;

    //private List<Object> c_child_list;
    List<Channel> g_channel_list;

    public Group(List<Channel> items, int g_id, String g_title, String g_url, int g_web_port,
                 int g_video_port, String g_login_id, String g_login_pw) {
        super(g_title, items);
        g_channel_list=items;
        this.g_id = g_id;
        this.g_title = g_title;
        this.g_url = g_url;
        this.g_web_port = g_web_port;
        this.g_video_port = g_video_port;
        this.g_login_id = g_login_id;
        this.g_login_pw = g_login_pw;
        //this.c_child_list = c_child_list;
    }

    public Group(List<Channel> items, int g_id, String g_title, String g_url) {
        super(g_title, items);
        this.g_id = g_id;
        this.g_title = g_title;
        this.g_url = g_url;
    }

    public int getG_id() {
        return g_id;
    }

    public void setG_id(int g_id) {
        this.g_id = g_id;
    }

    public String getG_title() {
        return g_title;
    }

    public void setG_title(String g_title) {
        this.g_title = g_title;
    }

    public String getG_url() {
        return g_url;
    }

    public void setG_url(String g_url) {
        this.g_url = g_url;
    }

    public int getG_web_port() {
        return g_web_port;
    }

    public void setG_web_port(int g_web_port) {
        this.g_web_port = g_web_port;
    }

    public int getG_video_port() {
        return g_video_port;
    }

    public void setG_video_port(int g_video_port) {
        this.g_video_port = g_video_port;
    }

    public String getG_login_id() {
        return g_login_id;
    }

    public void setG_login_id(String g_login_id) {
        this.g_login_id = g_login_id;
    }

    public String getG_login_pw() {
        return g_login_pw;
    }

    public void setG_login_pw(String g_login_pw) {
        this.g_login_pw = g_login_pw;
    }
}
