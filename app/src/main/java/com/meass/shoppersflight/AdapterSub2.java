package com.meass.travelagenceyuser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSub2 extends RecyclerView.Adapter<AdapterSub2.myview> {
    public List<FollerModelAndList> data;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public AdapterSub2(List<FollerModelAndList> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mees,parent,false);
        return new myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, final int position) {
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        holder.customer_name.setText(data.get(position).getName());
        holder.customer_number.setText("Friend");
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
                firebaseFirestore.collection("DDD")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        String name=task.getResult().getString("name");
                                        if (name.equals("a")) {
                                            String messa[] = {"Send Message"};
                                            AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                            builder.setTitle("Select Options")
                                                    .setItems(messa, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (which==0) {
                                                                final KProgressHUD progressDialog=  KProgressHUD.create(v.getContext())
                                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                                        .setLabel("Please wait")
                                                                        .setCancellable(false)
                                                                        .setAnimationSpeed(2)
                                                                        .setDimAmount(0.5f)
                                                                        .show();
                                                                firebaseFirestore.collection("All_UserID")
                                                                        .document(data.get(position).getEmail())
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    if (task.getResult().exists()) {
                                                                                        String uuid=task.getResult().getString("uuid");
                                                                                        firebaseFirestore.collection("User2")
                                                                                                .document(data.get(position).getEmail())
                                                                                                .get()
                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            if (task.getResult().exists()) {
                                                                                                                try {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    String image=task.getResult().getString("image");
                                                                                                                    String name=task.getResult().getString("name");
                                                                                                                    Intent intent=new Intent(v.getContext(),Main_Chat.class);
                                                                                                                    intent.putExtra("visit_user_id",uuid);
                                                                                                                    intent.putExtra("visit_user_name",name);
                                                                                                                    intent.putExtra("visit_image",image);
                                                                                                                    v.getContext().startActivity(intent);
                                                                                                                }catch (Exception e) {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    e.printStackTrace();
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                            else if (which==11) {
                                                                Intent intent=new Intent(v.getContext(),PlacedOrderActivity.class);
                                                                intent.putExtra("usernmae",data.get(position).getEmail());
                                                                v.getContext().startActivity(intent);
                                                            }
                                                        }
                                                    }).create();
                                            builder.show();
                                        }
                                        else {
                                            String messa[] = {"Placed a order ","Send Message"};
                                            AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                            builder.setTitle("Select Options")
                                                    .setItems(messa, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (which==1) {
                                                                final KProgressHUD progressDialog=  KProgressHUD.create(v.getContext())
                                                                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                                        .setLabel("Please wait")
                                                                        .setCancellable(false)
                                                                        .setAnimationSpeed(2)
                                                                        .setDimAmount(0.5f)
                                                                        .show();
                                                                firebaseFirestore.collection("All_UserID")
                                                                        .document(data.get(position).getEmail())
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    if (task.getResult().exists()) {
                                                                                        String uuid=task.getResult().getString("uuid");
                                                                                        firebaseFirestore.collection("User2")
                                                                                                .document(data.get(position).getEmail())
                                                                                                .get()
                                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            if (task.getResult().exists()) {
                                                                                                                try {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    String image=task.getResult().getString("image");
                                                                                                                    String name=task.getResult().getString("name");
                                                                                                                    Intent intent=new Intent(v.getContext(),Main_Chat.class);
                                                                                                                    intent.putExtra("visit_user_id",uuid);
                                                                                                                    intent.putExtra("visit_user_name",name);
                                                                                                                    intent.putExtra("visit_image",image);
                                                                                                                    v.getContext().startActivity(intent);
                                                                                                                }catch (Exception e) {
                                                                                                                    progressDialog.dismiss();
                                                                                                                    e.printStackTrace();
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                            else if (which==0) {
                                                                String lidtp[]= {"Mobile","Laptop","Chocolet","Gadget"};
                                                                AlertDialog.Builder builder1=new AlertDialog.Builder(v.getContext());
                                                                builder1.setTitle("Select Items")
                                                                        .setItems(lidtp, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                if (which==0) {
                                                                                    Intent intent=new Intent(v.getContext(),PlacedOrderActivity.class);
                                                                                    intent.putExtra("usernmae",data.get(position).getEmail());
                                                                                    intent.putExtra("weight","Mobile");
                                                                                    v.getContext().startActivity(intent);
                                                                                }
                                                                                else  if (which==1) {
                                                                                    Intent intent=new Intent(v.getContext(),PlacedOrderActivity.class);
                                                                                    intent.putExtra("usernmae",data.get(position).getEmail());
                                                                                    intent.putExtra("weight","Laptop");
                                                                                    v.getContext().startActivity(intent);
                                                                                }
                                                                                else  if (which==2) {
                                                                                    Intent intent=new Intent(v.getContext(),PlacedOrderActivity.class);
                                                                                    intent.putExtra("usernmae",data.get(position).getEmail());
                                                                                    intent.putExtra("weight","Chocolet");
                                                                                    v.getContext().startActivity(intent);
                                                                                }
                                                                                else  if (which==3) {
                                                                                    Intent intent=new Intent(v.getContext(),PlacedOrderActivity.class);
                                                                                    intent.putExtra("usernmae",data.get(position).getEmail());
                                                                                    intent.putExtra("weight","Gadget");
                                                                                    v.getContext().startActivity(intent);
                                                                                }

                                                                            }
                                                                        }).create();
                                                                builder1.show();

                                                            }
                                                        }
                                                    }).create();
                                            builder.show();

                                        }

                                    }
                                }
                            }
                        });


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


