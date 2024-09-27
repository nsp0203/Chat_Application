package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.AndroidUtils;
import com.example.chatapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(FirebaseUtil.isLoggedIn() && getIntent().getExtras()!=null)
        {
            String user_id = getIntent().getExtras().getString("user_id");
            FirebaseUtil.User_Collection_ref().document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        User_Model model = task.getResult().toObject(User_Model.class);
                        Intent main_intent = new Intent(Splash_Activity.this, MainActivity.class);
                        main_intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(main_intent);

                        Intent i = new Intent(Splash_Activity.this, Chat_Activity.class);
                        AndroidUtils.pass_user_model(i, model);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Splash_Activity.this.startActivity(i);
                        finish();
                    }
                }
            });
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(FirebaseUtil.isLoggedIn())
                    {
                        Intent i = new Intent(Splash_Activity.this, MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(Splash_Activity.this, Login_phn_no.class);
                        startActivity(i);
                    }
                    finish();
                }
            },1000);
        }
    }
}