package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Chat_Activity;
import com.example.chatapp.R;
import com.example.chatapp.model.Chat_room_model;
import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.AndroidUtils;
import com.example.chatapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class Recent_chat_recy_adapter extends FirestoreRecyclerAdapter<Chat_room_model, Recent_chat_recy_adapter.Chat_room_model_ViewHolder> {

    Context context;
    public Recent_chat_recy_adapter(@NonNull FirestoreRecyclerOptions<Chat_room_model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Chat_room_model_ViewHolder holder, int position, @NonNull Chat_room_model model) {
        FirebaseUtil.get_other_user(model.getUser_id())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            boolean Last_msg_sent_by_me = model.getLast_msg_sender_id().equals(FirebaseUtil.current_user_id());

                            User_Model other_user_model = task.getResult().toObject(User_Model.class);

                            FirebaseUtil.get_other_profile_pic_storage(other_user_model.getUser_id()).getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> t) {
                                            if(t.isSuccessful())
                                            {
                                                Uri uri = t.getResult();
                                                AndroidUtils.set_profile_pic(context, uri, holder.Profile_pic);
                                            }
                                        }
                                    });

                            holder.Username_txt.setText(other_user_model.getUsername());
                            if(Last_msg_sent_by_me)
                            {
                                holder.Last_msg_txt.setText("You : " + model.getLast_msg());
                            }
                            else
                            {
                                holder.Last_msg_txt.setText(model.getLast_msg());
                            }
                            holder.Last_msg_time.setText(FirebaseUtil.Timestamp_to_string(model.getLast_msg_timestamp()));

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(context, Chat_Activity.class);
                                    AndroidUtils.pass_user_model(i, other_user_model);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                }
                            });
                        }
                    }
                });
    }

    @NonNull
    @Override
    public Chat_room_model_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recy_row,parent,false);
        return new Chat_room_model_ViewHolder(view);
    }

    class Chat_room_model_ViewHolder extends RecyclerView.ViewHolder{

        TextView Username_txt;
        TextView Last_msg_txt;
        TextView Last_msg_time;
        ImageView Profile_pic;

        public Chat_room_model_ViewHolder(@NonNull View itemView) {
            super(itemView);

            Username_txt = itemView.findViewById(R.id.user_name);
            Last_msg_txt = itemView.findViewById(R.id.last_msg_txt);
            Profile_pic = itemView.findViewById(R.id.profile_pic);
            Last_msg_time = itemView.findViewById(R.id.last_msg_time_txt);


        }
    }
}
