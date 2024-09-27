package com.example.chatapp.model;

import com.google.firebase.Timestamp;

public class User_Model {

    private String Phn_no;
    private String username;
    private Timestamp timestamp;
    private String user_id;
    private String FCM_token;

    public User_Model() {
    }

    public User_Model(String phn_no, String username, Timestamp timestamp, String user_id) {
        Phn_no = phn_no;
        this.username = username;
        this.timestamp = timestamp;
        this.user_id = user_id;
    }

    public String getPhn_no() {
        return Phn_no;
    }

    public void setPhn_no(String phn_no) {
        Phn_no = phn_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFCM_token() {
        return FCM_token;
    }

    public void setFCM_token(String FCM_token) {
        this.FCM_token = FCM_token;
    }
}
