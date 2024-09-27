package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Chat_Activity;
import com.example.chatapp.R;
import com.example.chatapp.model.Chat_msg_model;
import com.example.chatapp.utils.AndroidUtils;
import com.example.chatapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Chat_recy_adapter extends FirestoreRecyclerAdapter<Chat_msg_model, Chat_recy_adapter.Chat_Model_ViewHolder> {

    Context context;
    public Chat_recy_adapter(@NonNull FirestoreRecyclerOptions<Chat_msg_model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Chat_Model_ViewHolder holder, int position, @NonNull Chat_msg_model model) {
        if(model.getSender_id().equals(FirebaseUtil.current_user_id()))
        {
            holder.Left_chat.setVisibility(View.GONE);
            holder.Right_chat.setVisibility(View.VISIBLE);
            holder.Right_text.setText(model.getMsg());
        }
        else
        {
            holder.Right_chat.setVisibility(View.GONE);
            holder.Left_chat.setVisibility(View.VISIBLE);
            holder.Left_text.setText(model.getMsg());
        }
    }

    @NonNull
    @Override
    public Chat_Model_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_msg_recy_row,parent,false);
        return new Chat_Model_ViewHolder(view);
    }

    class Chat_Model_ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout Left_chat, Right_chat;
        TextView Left_text, Right_text;

        public Chat_Model_ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            Left_chat = itemView.findViewById(R.id.left_chat);
            Right_chat = itemView.findViewById(R.id.right_chat);
            Left_text = itemView.findViewById(R.id.left_text);
            Right_text = itemView.findViewById(R.id.right_text);


        }
    }
}
