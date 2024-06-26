package com.example.wifichat.model;

public class User {


    private String userId;
    private String userName;
    private int picUrl;
    private String isOnline;

    public User() {
    }

    public User(String userId, String userName, int picUrl, String isOnline) {
        this.userId = userId;
        this.userName = userName;
        this.picUrl = picUrl;
        this.isOnline = isOnline;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(int picUrl) {
        this.picUrl = picUrl;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
}
