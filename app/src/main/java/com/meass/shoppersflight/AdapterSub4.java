package com.meass.travelagenceyuser;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.List;

public class AdapterSub4 extends RecyclerView.Adapter<AdapterSub4.myview> {
    public List<OrderDetails> data;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public AdapterSub4(List<OrderDetails> data) {
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


