package com.example.chatapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.Adapter.Recent_chat_recy_adapter;
import com.example.chatapp.Adapter.Search_User_Recy_Adapter;
import com.example.chatapp.model.Chat_room_model;
import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Chats_Fragment extends Fragment {

    RecyclerView recyclerView;
    Recent_chat_recy_adapter adapter;

    public Chats_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats_, container, false);
        recyclerView = view.findViewById(R.id.recy);
        Set_up_recyclerview();
        return view;
    }

    void Set_up_recyclerview()
    {
        Query query = FirebaseUtil.all_chat_room_collection_ref()
                .whereArrayContains("user_id", FirebaseUtil.current_user_id())
                .orderBy("last_msg_timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Chat_room_model> options = new FirestoreRecyclerOptions.Builder<Chat_room_model>()
                .setQuery(query, Chat_room_model.class).build();

        adapter = new Recent_chat_recy_adapter(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
        {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
        {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
        {
            adapter.notifyDataSetChanged();
        }
    }
}