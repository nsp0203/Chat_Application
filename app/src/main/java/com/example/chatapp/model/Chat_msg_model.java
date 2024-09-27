package com.example.chatapp.model;


import com.google.firebase.Timestamp;

public class Chat_msg_model {

    private String msg;
    private String sender_id;
    private Timestamp timestamp;

    public Chat_msg_model() {
    }

    public Chat_msg_model(String msg, String sender_id, Timestamp timestamp) {
        this.msg = msg;
        this.sender_id = sender_id;
        this.timestamp = timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
