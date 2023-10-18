package com.meass.travelagenceyuser;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapterr extends RecyclerView.Adapter<ChatAdapterr.myview> {
    public List<Contacts> data;
    FirebaseFirestore firebaseFirestore;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestoreg;
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    public ChatAdapterr(List<Contacts> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ChatAdapterr.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
        return new ChatAdapterr.myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapterr.myview holder, final int position) {
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        try {
            firebaseFirestore.collection("LastMessage")
                    .document(data.get(position).getUuid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    try {
                                        String immm="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";

                                        String image=data.get(position).getImage();
                                        if (image.equals(immm)) {
                                            Picasso.get().load(R.drawable.app_logo).into(holder.profile_image);
                                            holder.username.setText(data.get(position).getName());
                                            holder.userstatus.setText(task.getResult().getString("datte"));
                                        }
                                        else {
                                            Picasso.get().load(data.get(position).getImage()).into(holder.profile_image);
                                            holder.username.setText(data.get(position).getName());
                                            holder.userstatus.setText(task.getResult().getString("datte"));
                                        }

                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    });

        }catch (Exception e) {
        }
        holder.linearraa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Main_Chat.class);
                intent.putExtra("visit_user_id",data.get(position).getUuid());
                intent.putExtra("visit_user_name",data.get(position).getName());
                intent.putExtra("visit_image",data.get(position).getImage());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myview extends RecyclerView.ViewHolder {
        TextView cardname;
        ImageView cardimage;
        TextView cardprice,soldproducts;
        CircleImageView profile_image;
        TextView username,userstatus;
        ImageView users_online_status;
        View mView;
        LinearLayout linearraa;

        public myview(@NonNull View itemView) {
            super(itemView);
            mView =itemView;
            profile_image=itemView.findViewById(R.id.users_profile_image);
            username=itemView.findViewById(R.id.users_profile_name);
            userstatus=itemView.findViewById(R.id.users_status);
            users_online_status=itemView.findViewById(R.id.users_online_status);
            linearraa=itemView.findViewById(R.id.linearraa);


        }
    }
}