package com.meass.travelagenceyuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMainSection extends AppCompatActivity {

    SearchView name;
    RecyclerView rreeeed;
    LottieAnimationView empty_cart;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    ////
/*
private DatabaseReference contactsRef,userRef;
    private FirebaseAuth mauth;
    private String currentUserId;
    private RecyclerView myrecyclerview;
 */
    private RecyclerView recyclerView;
    private DatabaseReference chatRef, chatRef1, userRef;
    private FirebaseAuth mAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main_section);
        Toolbar toolbar = findViewById(R.id.tollbar);

        toolbar.setTitle("Chat List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10.0f);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        name = findViewById(R.id.name);
        recyclerView = findViewById(R.id.rreeeed);
        empty_cart = findViewById(R.id.empty_cart);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        mAuth = FirebaseAuth.getInstance();
       /*
        userID = "1pK3FquNHMVLgMZz3Hs0mufNnZD2";
        chatRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child("1pK3FquNHMVLgMZz3Hs0mufNnZD2").child("List");
        chatRef1 = FirebaseDatabase.getInstance().getReference().child("Contacts").child("1pK3FquNHMVLgMZz3Hs0mufNnZD2").child("List");
        userRef = FirebaseDatabase.getInstance().getReference().child("user");

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        */
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        getList = new ArrayList<>();
        getDataAdapter1 = new ChatAdapterr(getList);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference =   firebaseFirestore.collection("Contacts")
                .document(firebaseAuth.getCurrentUser().getUid())
        .collection("List").document();
        recyclerView = findViewById(R.id.rreeeed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatMainSection.this));
        recyclerView.setAdapter(getDataAdapter1);
        reciveData();


    }
    private void reciveData() {

        firebaseFirestore.collection("Contacts")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("List")
                .orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        Contacts get = ds.getDocument().toObject(Contacts.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });

    }
   // LottieAnimationView empty_cart;
    DocumentReference documentReference;
   // RecyclerView recyclerView;
    ChatAdapterr getDataAdapter1;
    List<Contacts> getList;
    String url;

    FirebaseUser firebaseUser;
    KProgressHUD progressHUD;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
    @Override
    public void onStart() {
        super.onStart();

        /*
        FirebaseRecyclerOptions<Contacts> options=new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatRef,Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts,ChatViewHolder> adapter=new FirebaseRecyclerAdapter<Contacts, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull Contacts model) {
                final String userid=model.getUuid();
                final String[] image = {"default_image"};
                userRef.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if(dataSnapshot.hasChild("picture"))
                            {
                                image[0] =dataSnapshot.child("picture").getValue().toString();
                                Picasso.get().load(image[0]).placeholder(R.drawable.profile_image).into(holder.profile_image);
                            }
                            final String name=dataSnapshot.child("name").getValue().toString();
                            final String status="Online";

                            holder.username.setText(name);
                            holder.userstatus.setText("Last seen: "+"\n"+"Online ");


                            if(dataSnapshot.child("userState").hasChild("state"))
                            {
                                String state=dataSnapshot.child("userState").child("state").getValue().toString();
                                String date=dataSnapshot.child("userState").child("date").getValue().toString();
                                String time=dataSnapshot.child("userState").child("time").getValue().toString();
                                if(state.equals("online"))
                                {
                                    holder.userstatus.setText("online");
                                }
                                else if(state.equals("offline"))
                                {
                                    holder.userstatus.setText("Last seen: "+"\n"+date+" "+time);
                                }
                            }
                            else
                            {
                                holder.userstatus.setText("offline");
                            }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent=new Intent(getApplicationContext(),Main_Chat.class);
                chatIntent.putExtra("visit_user_id",userid);
                chatIntent.putExtra("visit_user_name",name);
                chatIntent.putExtra("visit_image", image[0]);
                chatIntent.putExtra("chatstatus","online");
                startActivity(chatIntent);
            }
        });



    }
}

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
        }

@NonNull
@Override
public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.users_display_layout,parent,false);
        ChatViewHolder chatViewHolder=new ChatViewHolder(view);
        return chatViewHolder;
        }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
         */
    }
    /*
    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_image;
        TextView username,userstatus;
        ImageView users_online_status;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image=itemView.findViewById(R.id.users_profile_image);
            username=itemView.findViewById(R.id.users_profile_name);
            userstatus=itemView.findViewById(R.id.users_status);
            users_online_status=itemView.findViewById(R.id.users_online_status);
        }
    }
     */
}

