package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class Login_username extends AppCompatActivity {

    EditText username_input;
    Button let_me_in;
    ProgressBar progressBar;
    String Phn_no;
    User_Model userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        username_input = findViewById(R.id.username);
        let_me_in = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.pg_3);

        Phn_no = getIntent().getExtras().getString("phone");
        getUsername();

        let_me_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsername();
            }
        });
    }

    void setUsername()
    {

        String username = username_input.getText().toString();
        if(username.isEmpty() || username.length()<5)
        {
            username_input.setError("Username must be at least 5 characters long..!!");
            return;
        }
        set_in_progress(true);
        if(userModel!=null)
        {
            userModel.setUsername(username);
        }
        else
        {
            userModel = new User_Model(Phn_no, username, Timestamp.now(), FirebaseUtil.current_user_id());
        }

        FirebaseUtil.current_user_details().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                set_in_progress(false);
                if(task.isSuccessful())
                {
                    Intent i = new Intent(Login_username.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
    }

    void getUsername()
    {
        set_in_progress(true);
        FirebaseUtil.current_user_details().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                set_in_progress(false);
                if(task.isSuccessful())
                {
                    userModel = task.getResult().toObject(User_Model.class);
                    if(userModel!=null)
                    {
                        username_input.setText(userModel.getUsername());
                    }
                }
            }
        });
    }
    void set_in_progress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            let_me_in.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            let_me_in.setVisibility(View.VISIBLE);
        }
    }
}