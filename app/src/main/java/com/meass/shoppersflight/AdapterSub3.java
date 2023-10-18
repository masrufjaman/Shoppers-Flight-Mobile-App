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
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdapterSub3 extends RecyclerView.Adapter<AdapterSub3.myview> {
    public List<OrderDetails> data;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public AdapterSub3(List<OrderDetails> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order,parent,false);
        return new myview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myview holder, final int position) {
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        String orderdetails= data.get(position).getName()+"\n" +
                "Parcel Item : "+data.get(position).getParcelitem()+"\n" +
                "Parcel Weight : "+data.get(position).getWeight()+" KG";
        holder.logout.setText(data.get(position).getStatus());
        holder.customer_name.setText(orderdetails);
        holder.customer_number.setText(data.get(position).getDate());

        holder.card_view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("DDD")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        try {
                                            String name=task.getResult().getString("name");
                                            if (name.equals("a")) {
                                                String list[]={"Order Details","Start Process","Order Complete List"};

                                                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                                builder.setTitle("Details")
                                                        .setItems(list, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (which==0) {
                                                                    String details = "Traveller Name : "+data.get(position).ordertakingname+"\n" +
                                                                            "Order Giver Name : "+data.get(position).getName()+"\n" +
                                                                            "Customer Number : "+data.get(position).getCus_number()+"\n" +
                                                                            "Order Name : "+data.get(position).getName()+"\n" +
                                                                            "Address : "+data.get(position).getAddress()+"\n" +
                                                                            "Contact Number : "+data.get(position).getContact()+"\n" +
                                                                            "Parcel Item : "+data.get(position).getParcelitem()+"\n" +
                                                                            "Parcel Weight : "+data.get(position).getWeight()+" KG"+"\n" +
                                                                            "Order Giving Date : "+data.get(position).getDate();
                                                                    AlertDialog.Builder builder2=new AlertDialog.Builder(v.getContext());
                                                                    builder2.setTitle("Details")
                                                                            .setMessage(details)
                                                                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                            }).setNegativeButton("yes", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.dismiss();

                                                                        }
                                                                    }).create();
                                                                    builder2.show();
                                                                }
                                                                else if(which==2) {
                                                                    v.getContext().startActivity(new Intent(v.getContext(),OrderCompleteActivity.class));
                                                                }
                                                                else {
                                                                    String list[]={"Order Confirmed","Out For Delivery","Order Shipped","Complete Order"};
                                                                    androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                                                                    builder.setTitle("Confirmation")
                                                                            .setItems(list, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    if (which==0) {
                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(""+data.get(position).getCus_email())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","ORDER_CONFIRMED")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {

                                                                                                            //   startActivity(new Intent(getApplicationContext(),Main_Dash.class));
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","ORDER_CONFIRMED")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Toasty.success(v.getContext(),"Done",Toasty.LENGTH_SHORT,true).show();
                                                                                                            //   startActivity(new Intent(getApplicationContext(),Main_Dash.class));
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                    else  if (which==1) {

                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(""+data.get(position).getCus_email())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","OUT_FOR_DELIVERY")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {

                                                                                                            //   startActivity(new Intent(getApplicationContext(),Main_Dash.class));
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","OUT_FOR_DELIVERY")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Toasty.success(v.getContext(),"Done",Toasty.LENGTH_SHORT,true).show();
                                                                                                            //   startActivity(new Intent(getApplicationContext(),Main_Dash.class));
                                                                                                        }
                                                                                                    }
                                                                                                });

                                                                                    }
                                                                                    else  if (which==2) {
                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(""+data.get(position).getCus_email())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","ORDER_SHIPPED")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {

                                                                                                            //   startActivity(new Intent(getApplicationContext(),Main_Dash.class));
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","ORDER_SHIPPED")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            Toasty.success(v.getContext(),"Done",Toasty.LENGTH_SHORT,true).show();
                                                                                                            //   startActivity(new Intent(getApplicationContext(),Main_Dash.class));
                                                                                                        }
                                                                                                    }
                                                                                                });



                                                                                    }
                                                                                    else  if (which==3) {
                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(""+data.get(position).getCus_email())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","ORDER_COMPLETE")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {

                                                                                                            //   startActivity(new Intent(getApplicationContext(),Main_Dash.class));
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                        firebaseFirestore.collection("Orderss")
                                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                .collection("List")
                                                                                                .document(""+data.get(position).getTime())
                                                                                                .update("status","ORDER_COMPLETE")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()) {
                                                                                                            firebaseFirestore.collection("Orderss")
                                                                                                                    .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                                                    .collection("List")
                                                                                                                    .document(""+data.get(position).getTime())
                                                                                                                    .delete()
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                Toasty.success(v.getContext(),"Done",Toasty.LENGTH_SHORT,true).show();
                                                                                                                                v.getContext().startActivity(new Intent(v.getContext(),HomeActivity.class));
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });

                                                                                                            //
                                                                                                        }
                                                                                                    }
                                                                                                });



                                                                                    }

                                                                                }
                                                                            }).create().show();
                                                                }
                                                            }
                                                        }).create();
                                                builder.show();
                                            }
                                            else {
                                               String details = "Traveller Name : "+data.get(position).ordertakingname+"\n" +
                                                       "Order Giver Name : "+data.get(position).getName()+"\n" +
                                                       "Traveller Number : "+data.get(position).getOrdertaking_number()+"\n" +
                                                       "Order Name : "+data.get(position).getName()+"\n" +
                                                       "Address : "+data.get(position).getAddress()+"\n" +
                                                       "Contact Number : "+data.get(position).getContact()+"\n" +
                                                       "Parcel Item : "+data.get(position).getParcelitem()+"\n" +
                                                       "Parcel Weight : "+data.get(position).getWeight()+" KG"+"\n" +
                                                       "Order Giving Date : "+data.get(position).getDate();
                                                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                                builder.setTitle("Details")
                                                        .setMessage(details)
                                                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        }).setNegativeButton("yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                }).create();
                                                builder.show();

                                                Toast.makeText(v.getContext(), "You are a simple user you can see the details only.", Toast.LENGTH_SHORT).show();
                                            }
                                        }catch (Exception e) {

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


