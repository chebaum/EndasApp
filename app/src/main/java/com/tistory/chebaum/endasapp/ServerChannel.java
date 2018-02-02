package com.tistory.chebaum.endasapp;

/**
 * ***설명***
 * 장비에 연결해서 장비에 연결된 모든 채널들을 가져올때
 * 장비의 번호/이름/연결가능 여부를 간편히 저장하고 사용하기 위한 클래스 / RegisterGroupActivity에서 사용된다.
 *
 * ex) 0001:0001 Channel1  ->  number=2 / isActive=true / name=Channel1
 *
 * 사용자가 채널을 사용하기 위해 선택한 경우 isSelected=true
 */

public class ServerChannel {
    private int number;
    private boolean isActive;
    private String name;
    private boolean isSelected;

    public ServerChannel() {
        isSelected=false;
    }

    public ServerChannel(int number, boolean isActive, String name) {
        this.number = number;
        this.isActive = isActive;
        this.name = name;
        this.isSelected = false;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }
}
