package com.example.chatapp.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {
    public static String current_user_id()
    {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn()
    {
        if(current_user_id()!=null)
        {
            return true;
        }
        return false;
    }

    public static DocumentReference current_user_details()
    {
        return FirebaseFirestore.getInstance().collection("users").document(current_user_id());
    }

    public static CollectionReference User_Collection_ref()
    {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference get_chatroom_ref(String chat_room_id)
    {
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chat_room_id);
    }

    public static String get_chat_room_id(String user_id_1, String user_id_2)
    {
        if(user_id_1.hashCode()<user_id_2.hashCode())
        {
            return user_id_1+"_"+user_id_2;
        }
        else
        {
            return user_id_2+"_"+user_id_1;
        }
    }

    public static CollectionReference get_chat_room_msg_ref(String chat_room_id)
    {
        return get_chatroom_ref(chat_room_id).collection("chats");
    }

    public static CollectionReference all_chat_room_collection_ref()
    {
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference get_other_user(List<String> user_id)
    {
        if(user_id.get(0).equals(FirebaseUtil.current_user_id()))
        {
            return User_Collection_ref().document(user_id.get(1));
        }
        else
        {
            return User_Collection_ref().document(user_id.get(0));
        }
    }

    public static String Timestamp_to_string(Timestamp timestamp)
    {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout()
    {
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference get_current_profile_pic_storage()
    {
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.current_user_id());
    }

    public static StorageReference get_other_profile_pic_storage(String other_userid)
    {
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(other_userid);
    }
}
