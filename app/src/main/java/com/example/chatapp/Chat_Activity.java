package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chatapp.Adapter.Chat_recy_adapter;
import com.example.chatapp.Adapter.Search_User_Recy_Adapter;
import com.example.chatapp.model.Chat_msg_model;
import com.example.chatapp.model.Chat_room_model;
import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.AndroidUtils;
import com.example.chatapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;

import java.util.Arrays;

public class Chat_Activity extends AppCompatActivity {

    User_Model user_model;
    EditText Msg;
    ImageButton Send_btn;
    ImageButton Back_btn;
    TextView Chat_username;
    RecyclerView recyclerView;
    String chat_room_id;
    Chat_room_model chat_room_model;
    Chat_recy_adapter adapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Get User_Model
        user_model = AndroidUtils.get_User_Model(getIntent());

        chat_room_id = FirebaseUtil.get_chat_room_id(FirebaseUtil.current_user_id(),user_model.getUser_id());

        Msg = findViewById(R.id.msg);
        Send_btn = findViewById(R.id.send_btn);
        Back_btn = findViewById(R.id.back_btn);
        Chat_username = findViewById(R.id.chat_username);
        imageView = findViewById(R.id.profile_pic);

        recyclerView = findViewById(R.id.chat_recy);

        FirebaseUtil.get_other_profile_pic_storage(user_model.getUser_id()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> t) {
                        if(t.isSuccessful())
                        {
                            Uri uri = t.getResult();
                            AndroidUtils.set_profile_pic(Chat_Activity.this, uri, imageView);
                        }
                    }
                });

        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        Chat_username.setText(user_model.getUsername());

        Send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = Msg.getText().toString().trim();
                if(msg.isEmpty())
                {
                    return;
                }
                send_msg(msg);
            }
        });

        get_or_create_chat_room_model();

        setup_chat_recy();
    }

    void setup_chat_recy()
    {
        Query query = FirebaseUtil.get_chat_room_msg_ref(chat_room_id)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Chat_msg_model> options = new FirestoreRecyclerOptions.Builder<Chat_msg_model>()
                .setQuery(query, Chat_msg_model.class).build();

        adapter = new Chat_recy_adapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }
    void send_msg(String msg)
    {
        chat_room_model.setLast_msg_timestamp(Timestamp.now());
        chat_room_model.setLast_msg_sender_id(FirebaseUtil.current_user_id());
        chat_room_model.setLast_msg(msg);
        FirebaseUtil.get_chatroom_ref(chat_room_id).set(chat_room_model);


        Chat_msg_model chat_msg_model = new Chat_msg_model(msg, FirebaseUtil.current_user_id(),Timestamp.now());
        FirebaseUtil.get_chat_room_msg_ref(chat_room_id).add(chat_msg_model)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful())
                        {
                            Msg.setText("");
                        }
                    }
                });
    }

    void get_or_create_chat_room_model()
    {
        FirebaseUtil.get_chatroom_ref(chat_room_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    chat_room_model = task.getResult().toObject(Chat_room_model.class);
                    if(chat_room_model==null)
                    {
                        chat_room_model = new Chat_room_model(
                                chat_room_id,
                                Arrays.asList(FirebaseUtil.current_user_id(), user_model.getUser_id()),
                                Timestamp.now(),
                                ""
                        );

                        FirebaseUtil.get_chatroom_ref(chat_room_id).set(chat_room_model);
                    }
                }
            }
        });
    }
}