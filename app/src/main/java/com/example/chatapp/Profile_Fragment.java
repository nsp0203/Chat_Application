package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.AndroidUtils;
import com.example.chatapp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Profile_Fragment extends Fragment {

    ImageView Profile_pic;
    EditText User_name;
    EditText Phn_num;
    Button Update_btn;
    ProgressBar progressBar;
    TextView Logout_btn;
    User_Model current_user_model;
    ActivityResultLauncher<Intent> image_pic_launcher;
    Uri selected_img_uri;

    public Profile_Fragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image_pic_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null)
                        {
                            selected_img_uri = data.getData();
                            AndroidUtils.set_profile_pic(getContext(), selected_img_uri, Profile_pic);
                        }
                    }
                }
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        Profile_pic = view.findViewById(R.id.profile_img);
        User_name = view.findViewById(R.id.username);
        Phn_num = view.findViewById(R.id.phn);
        Update_btn = view.findViewById(R.id.update_btn);
        progressBar = view.findViewById(R.id.pg_4);
        Logout_btn = view.findViewById(R.id.logout);

        get_user_data();

        Update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_btn_click();
            }
        });

        Logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUtil.logout();
                            Intent intent = new Intent(getContext(), Splash_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        Profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(Profile_Fragment.this).cropSquare().compress(512).maxResultSize(512,512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                image_pic_launcher.launch(intent);
                                return null;
                            }
                        });
            }
        });
        return view;
    }

    void update_btn_click()
    {
        String new_username = User_name.getText().toString();
        if(new_username.isEmpty() || new_username.length()<5)
        {
            User_name.setError("Username must be at least 5 characters long..!!");
            return;
        }
        current_user_model.setUsername(new_username);
        set_in_progress(true);
        if(selected_img_uri!=null)
        {
            FirebaseUtil.get_current_profile_pic_storage().putFile(selected_img_uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            update_to_firestore();
                        }
                    });
        }
        else
        {
            update_to_firestore();
        }
        update_to_firestore();
    }

    void update_to_firestore()
    {
        FirebaseUtil.current_user_details().set(current_user_model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        set_in_progress(false);
                        if(task.isSuccessful())
                        {
                            AndroidUtils.showToast(getContext(),"Updated Successfully..!!");
                        }
                        else
                        {
                            AndroidUtils.showToast(getContext(),"Update Failed..!!");
                        }
                    }
                });
    }
    void get_user_data()
    {
        set_in_progress(true);

        FirebaseUtil.get_current_profile_pic_storage().getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful())
                                {
                                    Uri uri = task.getResult();
                                    AndroidUtils.set_profile_pic(getContext(), uri, Profile_pic);
                                }
                            }
                        });

        FirebaseUtil.current_user_details().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                set_in_progress(false);
                current_user_model = task.getResult().toObject(User_Model.class);
                User_name.setText(current_user_model.getUsername());
                Phn_num.setText(current_user_model.getPhn_no());

            }
        });
    }

    void set_in_progress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            Update_btn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            Update_btn.setVisibility(View.VISIBLE);
        }
    }
}