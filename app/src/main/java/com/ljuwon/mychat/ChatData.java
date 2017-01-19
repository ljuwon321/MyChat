package com.ljuwon.mychat;

/**
 * Created by 주원 on 2017-01-18.
 */
public class ChatData {
    private String userName;
    private String message;
    private String image_url;
    private String time;

    public ChatData() { }

    public ChatData(String userName, String message, String image_url, String time) {
        this.userName = userName;
        this.message = message;
        this.image_url = image_url;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
