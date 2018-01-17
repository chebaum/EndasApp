package com.tistory.chebaum.endasapp;

/**
 * Created by cheba on 2018-01-11.
 */

public class Channel {
    private int c_id;
    private String c_title;
    private String c_url;
    private int c_web_port;
    private int c_video_port;
    private String c_login_id;
    private String c_login_pw;

    // 이거는 테스트용! 첫번째 생성자는 후에 지운다.
    public Channel(String c_title) {
        this.c_title = c_title;
    }

    public Channel(String c_title, String c_url) {
        this.c_title = c_title;
        this.c_url = c_url;
    }

    public Channel(String c_title, int c_web_port, int c_video_port, String c_login_id, String c_login_pw) {
        this.c_title = c_title;
        this.c_web_port = c_web_port;
        this.c_video_port = c_video_port;
        this.c_login_id = c_login_id;
        this.c_login_pw = c_login_pw;
    }

    // getters and setters!
    public int getC_id() {
        return c_id;
    }
    public void setC_id(int c_id) {
        this.c_id = c_id;
    }
    public String getC_title() {
        return c_title;
    }
    public void setC_title(String c_title) {
        this.c_title = c_title;
    }
    public String getC_url() {
        return c_url;
    }
    public void setC_url(String c_url) {
        this.c_url = c_url;
    }
    public int getC_web_port() {
        return c_web_port;
    }
    public void setC_web_port(int c_web_port) {
        this.c_web_port = c_web_port;
    }
    public int getC_video_port() {
        return c_video_port;
    }
    public void setC_video_port(int c_video_port) {
        this.c_video_port = c_video_port;
    }
    public String getC_login_id() {
        return c_login_id;
    }
    public void setC_login_id(String c_login_id) {
        this.c_login_id = c_login_id;
    }
    public String getC_login_pw() {
        return c_login_pw;
    }
    public void setC_login_pw(String c_login_pw) {
        this.c_login_pw = c_login_pw;
    }


}
