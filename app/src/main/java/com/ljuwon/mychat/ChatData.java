package com.ljuwon.mychat;

public class ChatData {
    public static final String MESSAGE = "message";
    public static final String IMAGE = "image";

    private String type;
    private String userName;
    private String message;
    private String image_url;
    private String time;

    public ChatData() {}

    public ChatData(String type, String userName, String message, String Image_url, String time) {
        this.type = type;
        this.userName = userName;
        this.message = message;
        this.image_url = Image_url;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
