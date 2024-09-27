package com.example.chatapp.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class Chat_room_model {

    String chat_room_id;
    List<String> user_id;
    Timestamp last_msg_timestamp;
    String last_msg_sender_id;
    String last_msg;
    public Chat_room_model() {
    }

    public Chat_room_model(String chat_room_id, List<String> user_id, Timestamp last_msg_timestamp, String last_msg_sender_id) {
        this.chat_room_id = chat_room_id;
        this.user_id = user_id;
        this.last_msg_timestamp = last_msg_timestamp;
        this.last_msg_sender_id = last_msg_sender_id;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public List<String> getUser_id() {
        return user_id;
    }

    public void setUser_id(List<String> user_id) {
        this.user_id = user_id;
    }

    public Timestamp getLast_msg_timestamp() {
        return last_msg_timestamp;
    }

    public void setLast_msg_timestamp(Timestamp last_msg_timestamp) {
        this.last_msg_timestamp = last_msg_timestamp;
    }

    public String getLast_msg_sender_id() {
        return last_msg_sender_id;
    }

    public void setLast_msg_sender_id(String last_msg_sender_id) {
        this.last_msg_sender_id = last_msg_sender_id;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }
}
