package com.tistory.chebaum.endasapp;

/**
 * Created by cheba on 2018-01-26.
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
