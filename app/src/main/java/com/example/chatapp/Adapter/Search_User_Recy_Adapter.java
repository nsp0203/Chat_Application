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
import com.example.chatapp.model.User_Model;
import com.example.chatapp.utils.AndroidUtils;
import com.example.chatapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Search_User_Recy_Adapter extends FirestoreRecyclerAdapter<User_Model, Search_User_Recy_Adapter.User_Model_ViewHolder> {

    Context context;
    public Search_User_Recy_Adapter(@NonNull FirestoreRecyclerOptions<User_Model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull User_Model_ViewHolder holder, int position, @NonNull User_Model model) {
        holder.Username_txt.setText(model.getUsername());
        holder.Phn_no.setText(model.getPhn_no());

        if(model.getUser_id().equals(FirebaseUtil.current_user_id()))
        {
            holder.Username_txt.setText(model.getUsername()+ " (Me)");
        }

        FirebaseUtil.get_other_profile_pic_storage(model.getUser_id()).getDownloadUrl()
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



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Chat_Activity.class);
                AndroidUtils.pass_user_model(i, model);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public User_Model_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recy_row,parent,false);
        return new User_Model_ViewHolder(view);
    }

    class User_Model_ViewHolder extends RecyclerView.ViewHolder{

        TextView Username_txt;
        TextView Phn_no;
        ImageView Profile_pic;

        public User_Model_ViewHolder(@NonNull View itemView) {
            super(itemView);

            Username_txt = itemView.findViewById(R.id.user_name);
            Phn_no = itemView.findViewById(R.id.phn_no);
            Profile_pic = itemView.findViewById(R.id.profile_pic);


        }
    }
}
