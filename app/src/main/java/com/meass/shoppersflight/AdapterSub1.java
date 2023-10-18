package com.meass.travelagenceyuser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSub1 extends RecyclerView.Adapter<AdapterSub1.myview> {
    public List<FollerModelAndList> data;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public AdapterSub1(List<FollerModelAndList> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.subbb,parent,false);
        return new myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, final int position) {
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        holder.customer_name.setText(data.get(position).getName());
        holder.customer_number.setText("Not Friend");
        String imm = "https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
        if (imm.equals(data.get(position).getPhoto())) {
        }
        else {
           try {
               Picasso.get().load(data.get(position).getPhoto()).into(holder.image);
           }catch (Exception e) {
               Picasso.get().load(data.get(position).getPhoto()).into(holder.image);
           }
        }
        holder.card_view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmation")
                        .setMessage("Are you want to accept his request?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //name, email, photo, mobile,username,userid,time
                        FollerModelAndList  follerModelAndList =new FollerModelAndList(data.get(position).getName(),
                                data.get(position).getEmail(),data.get(position).getPhoto(),data.get(position).getMobile(),
                                data.get(position).getUsername(),data.get(position).getUserid(),data.get(position).getTime());
                        firebaseFirestore.collection("FirendsList")
                                .document("MyList")
                                .collection(firebaseAuth.getCurrentUser().getEmail())
                                .document("List")
                                .collection("List")
                                .document(data.get(position).getEmail())
                                .set(follerModelAndList)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            firebaseFirestore.collection("FirendsList")
                                                    .document("Requesteds")
                                                    .collection(firebaseAuth.getCurrentUser().getEmail())
                                                    .document("List")
                                                    .collection("List")
                                                    .document(data.get(position).getEmail())
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(v.getContext(), "Done", Toast.LENGTH_SHORT).show();
                                                                v.getContext().startActivity(new Intent(v.getContext(),HomeActivity.class));
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                }).create();
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myview extends RecyclerView.ViewHolder{
        TextView customer_name,customer_number,customer_area,logout,customer_area3,customer_area8;
        CardView card_view8;
        ImageView image;
        public myview(@NonNull View itemView) {
            super(itemView);
            customer_name=itemView.findViewById(R.id.customer_name);
            customer_number=itemView.findViewById(R.id.customer_number);
            logout=itemView.findViewById(R.id.logout);
            card_view8=itemView.findViewById(R.id.card_view8);
            image=itemView.findViewById(R.id.image);
        }
    }
}


