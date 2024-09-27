package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.chatapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView Bottom_nav;
    ImageButton searchButton;

    Chats_Fragment chats_fragment;
    Profile_Fragment profile_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chats_fragment = new Chats_Fragment();
        profile_fragment = new Profile_Fragment();

        Bottom_nav = findViewById(R.id.bottom_nav);
        searchButton = findViewById(R.id.search_btn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Search_Activity.class);
                startActivity(i);
            }
        });
        Bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_chat)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chats_fragment).commit();
                }
                if(item.getItemId() == R.id.menu_profile)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profile_fragment).commit();
                }
                return true;
            }
        });
        Bottom_nav.setSelectedItemId(R.id.menu_chat);

        get_FCM_Token();
    }

    void get_FCM_Token()
    {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful())
                {
                    String token = task.getResult();
                    FirebaseUtil.current_user_details().update("FCM_token", token);
                }
            }
        });
    }
}