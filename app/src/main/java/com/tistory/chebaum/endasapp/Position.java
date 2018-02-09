package com.tistory.chebaum.endasapp;

/**
 * Created by cheba on 2018-02-01.
 * 선택된 항목을 기억하기 위한 클래스.
 */

public class Position {
    private int i; // 장비의 번호를 의미함
    private int j; // 채널 번호를 의미함

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
