package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.chatapp.Adapter.Search_User_Recy_Adapter;
import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Search_Activity extends AppCompatActivity {

    EditText Search_input;
    ImageButton Search_btn;
    ImageButton Back_btn;
    RecyclerView recyclerView;

    Search_User_Recy_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Search_input = findViewById(R.id.search_input);
        Search_btn = findViewById(R.id.search_user);
        Back_btn = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_recy);

        Search_input.requestFocus();

        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        Search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Search_term = Search_input.getText().toString();
                if(Search_term.isEmpty() || Search_term.length()<5)
                {
                    Search_input.setError("Invalid Username");
                    return;
                }
                Set_up_recyclerview(Search_term);
            }
        });
    }

    void Set_up_recyclerview(String Search_term)
    {
        Query query = FirebaseUtil.User_Collection_ref()
                .whereGreaterThanOrEqualTo("username", Search_term);

        FirestoreRecyclerOptions<User_Model> options = new FirestoreRecyclerOptions.Builder<User_Model>()
                .setQuery(query, User_Model.class).build();

        adapter = new Search_User_Recy_Adapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null)
        {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter!=null)
        {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
        {
            adapter.startListening();
        }
    }
}