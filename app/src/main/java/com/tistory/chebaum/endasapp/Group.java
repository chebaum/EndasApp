package com.tistory.chebaum.endasapp;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * 장비(애매하여 Group 이라고 이름지었음) 의 속성값을 저장하는 클래스.
 */

public class Group extends MultiCheckExpandableGroup{
    private long g_id;
    private String g_title;
    private String g_url;
    private int g_web_port;
    private int g_video_port;
    private String g_login_id;
    private String g_login_pw;
    private List<Channel> g_channel_list; // 해당 Group(장비)에 속해있는 채널들을 담고있는 ListArray

    public Group(List<Channel> items, String g_title, String g_url, int g_web_port,
                 int g_video_port, String g_login_id, String g_login_pw) {
        super(g_title, items);
        g_channel_list=items;
        // -> 이 생성자는 장비객체를 처음 생성하여 DB에 insert할때 사용된다.
        // DB에서 id값은 auto-increment primary key로 설정되어있어서 따로 지정하지 않아줘도 되기때문에
        // 이 생성자에는 id값을 넣어주지 않는다.
        //this.g_id = g_id;
        this.g_title = g_title;
        this.g_url = g_url;
        this.g_web_port = g_web_port;
        this.g_video_port = g_video_port;
        this.g_login_id = g_login_id;
        this.g_login_pw = g_login_pw;
    }
    public Group(List<Channel> items, Long g_id, String g_title, String g_url, int g_web_port,
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
    }

    public long getG_id() {
        return g_id;
    }
    public void setG_id(long g_id) {
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
    public List<Channel> getG_channel_list() {
        return g_channel_list;
    }
    public void setG_channel_list(List<Channel> g_channel_list) {
        this.g_channel_list = g_channel_list;
    }
}
