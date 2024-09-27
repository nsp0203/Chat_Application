package com.example.chatapp.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.model.User_Model;

public class AndroidUtils {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void pass_user_model(Intent i, User_Model user_model)
    {
        i.putExtra("username", user_model.getUsername());
        i.putExtra("phone", user_model.getPhn_no());
        i.putExtra("user_id", user_model.getUser_id());
    }

    public static User_Model get_User_Model(Intent i)
    {
        User_Model user_model = new User_Model();
        user_model.setUsername(i.getStringExtra("username"));
        user_model.setPhn_no(i.getStringExtra("phn_no"));
        user_model.setUser_id(i.getStringExtra("user_id"));
        return user_model;
    }

    public static void set_profile_pic(Context context, Uri image_uri, ImageView imageView)
    {
        Glide.with(context).load(image_uri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
