package com.meass.travelagenceyuser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.BlogViewHolder>
        implements Filterable {

    private List<BlogPost> blog_list;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    private FloatingActionButton dialogDismiss;
    private Dialog mDialog;
    private RecyclerView mComments;
    private List<comments> mCommentList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private commentsAdapter mAdapter;
    private EditText commentText;
    private ImageButton commentSend;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

    private RequestManager glide;


    private List<BlogPost> blogListFiltered;

    private static final String TAG = "blogAdapter errors";

    public BlogRecyclerAdapter(Context context,List<BlogPost> blog_list, RequestManager glide){
        this.blog_list = blog_list;
        this.context = context;
        this.blogListFiltered = blog_list;
        this.glide = glide;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_blog_list_item,parent,false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogViewHolder holder, final int position) {

        BlogPost blogPost = blogListFiltered.get(position);

        final String blogPostId = blogPost.BlogPostId;

        String user_id = blogPost.getUser_id();
        currentUserId = firebaseAuth.getCurrentUser().getEmail();

        //holder.shimmerFrameLayout.stopShimmerAnimation();
        final String blogImageUrl = blogPost.getImage_url();
        final String blogThumbUrl = blogPost.getThumb_url();
        final String blogTitle = blogPost.getTitle();
        final String blogDesc = blogPost.getDesc();

        //holder.shimmerFrameLayout.startShimmerAnimation();
        firebaseFirestore.collection("All_UserID")
                .document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String uuid=task.getResult().getString("uuid");
                                firebaseFirestore.collection("Users")
                                        .document(uuid)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        String userImageUrl = task.getResult().getString("image").toString();
                                                        String userNameData = task.getResult().getString("name").toString();
                                                        if (userNameData.isEmpty()) {
                                                            userNameData = "No name";
                                                        }
                                                        String immm="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";
                                                       if (immm.equals(userImageUrl)) {
                                                           String ooo = "https://firebasestorage.googleapis.com/v0/b/iconinternationallimited.appspot.com/o/airplane.png?alt=media&token=f168a5a2-b7bd-4820-836e-80b99395c39e";
                                                           holder.username.setText(userNameData);
                                                          GlideLoadImage.loadSmallImage(context,holder.userImage,ooo,userImageUrl);
                                                       }
                                                       else {
                                                           holder.username.setText(userNameData);
                                                           GlideLoadImage.loadSmallImage(context,holder.userImage,userImageUrl,userImageUrl);
                                                       }

                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });


        topAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_bottom_animation);
        startAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_start_animation);
        endAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.splash_end_animation);
        holder.single_linear_layout.setAnimation(topAnimation);
        holder.blogImage.setVisibility(View.VISIBLE);
        GlideLoadImage.loadImage(context,holder.blogImage,blogThumbUrl,blogImageUrl);

        holder.title.setText(blogTitle);

        long millisecond = blogPost.getTimestamp();
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        String timeString = DateFormat.format("HH:mm", new Date(millisecond)).toString();
        holder.blogTime.setText(timeString);

        if (System.currentTimeMillis() - blogPost.getTimestamp() < 86400 * 1000) {
            String lastSeenTime = GetTimeAgo.getTimeAgo(millisecond, context);
            holder.date.setText(lastSeenTime);
        } else {
            holder.date.setText(dateString);
        }

        //Getting likes count
        CollectionReference likeCollectionRef = firebaseFirestore.collection("Posts")
                .document(blogPostId).collection("Likes");
        likeCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }
                if (documentSnapshots.isEmpty()) {
                    holder.blogLikeCount.setText(0 + " Likes");
                } else {
                    int likeCount = documentSnapshots.size();
                    holder.blogLikeCount.setText(likeCount + " Likes");
                }
            }
        });

        //Getting if post liked or not
      /*
        DocumentReference likeReference = firebaseFirestore.collection("Posts")
                .document(blogPostId)
                .collection("Likes").document(currentUserId);
        likeReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }
                if (documentSnapshot.exists()) {
                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like));
                } else {
                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_normal_like));
                }
            }
        });
       */
       // Toast.makeText(holder.itemView.getContext(), ""+currentUserId, Toast.LENGTH_SHORT).show();
        DocumentReference likeReference = firebaseFirestore.collection("Posts")
                .document(blogPostId)
                .collection("Likes").document(firebaseAuth.getCurrentUser().getUid());
        likeReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }
                if (documentSnapshot.exists()) {
                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like));
                } else {
                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_normal_like));
                }
            }
        });
        //Getting if post favourite or not
        DocumentReference favouriteReference = firebaseFirestore.collection("User2")
                .document(currentUserId).collection("Favourites")
                .document(blogPostId);
        favouriteReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }
                if (documentSnapshot.exists()) {
                    holder.favouriteBlog.setImageDrawable(context.getDrawable(R.drawable.favourites));
                } else {
                    holder.favouriteBlog.setImageDrawable(context.getDrawable(R.drawable.favourites_normal));
                }
            }
        });


        //Getting comments count
        CollectionReference commentRef = firebaseFirestore.collection("Posts")
                .document(blogPostId).collection("Comments");
        commentRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }
                if (documentSnapshots.isEmpty()) {
                    holder.blogCommentCount.setText(0 + " Comments");
                } else {
                    int commentCount = documentSnapshots.size();
                    holder.blogCommentCount.setText(commentCount + " Comments");
                }

            }
        });

        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_contact);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }


    private void loadMessages(String mBlogId){
        mCommentList.clear();
        mAdapter.notifyDataSetChanged();
        Log.i("chatActivityCount","Loading messages");
        Query loadMessageQuery = firebaseFirestore.collection("Posts").document(mBlogId)
                .collection("Comments").orderBy("timestamp", Query.Direction.DESCENDING);
        loadMessageQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    if (task.getResult().getDocuments().isEmpty()){
                        mDialog.dismiss();
                        Toast.makeText(context,"No comments for this blog",Toast.LENGTH_SHORT).show();
                    }

                    if (!task.getResult().getDocuments().isEmpty()) {

                        for (DocumentChange doc : task.getResult().getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                comments message = doc.getDocument().toObject(comments.class);

                                mCommentList.add(message);

                                mAdapter.notifyDataSetChanged();
                                mComments.scrollToPosition(0);
                            }
                        }
                    }
                }
            }
        });
    }

//    private void loadMoreMessages(String mBlogId){
//        Query nextQuery = firebaseFirestore.collection("Posts").document(mBlogId)
//                .collection("Comments").orderBy("timestamp", Query.Direction.DESCENDING).startAfter(lastVisible).limit(15);
//        nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.w("CHAT_ACTIVITY", "listen:error", e);
//                    return;
//                }
//                if (!documentSnapshots.isEmpty()) {
//                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
//                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//                        if (doc.getType() == DocumentChange.Type.ADDED) {
//                            comments message = doc.getDocument().toObject(comments.class);
//                            mCommentList.add(message);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//            }
//        });
//    }


    @Override
    public int getItemCount() {
        Log.i("BLOG",String.valueOf(blog_list.size()));
        return blogListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        //    private List<BlogPost> blogListFiltered;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.i("SearchInfo","Called");
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    blogListFiltered = blog_list;
                } else {
                    List<BlogPost> filteredList = new ArrayList<>();
                    for (BlogPost row : blog_list) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                            Log.i("SearchInfo","Yes added");
                        }
                    }
                    blogListFiltered = filteredList;
                    Log.i("SearchInfo",blogListFiltered.size()+" is the filter size");
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = blogListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                blogListFiltered = (List<BlogPost>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }


    public class BlogViewHolder extends RecyclerView.ViewHolder{

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;
        private TextView blogCommentCount;
        public View mView;
        private TextView title,username,date;
        private ImageView blogImage;
        private CircleImageView userImage;
        //private ShimmerLayout shimmerFrameLayout;
        private TextView blogTime;
        private ImageView commentBtn;
        private ConstraintLayout blogListConstraintLayout;
        private ConstraintLayout userConstraintLayout;
        private ImageView favouriteBlog;
        CardView single_linear_layout;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //shimmerFrameLayout = itemView.findViewById(R.id.shimmer_view_container);

            mView = itemView;
            single_linear_layout=itemView.findViewById(R.id.single_linear_layout);
            blogTime = itemView.findViewById(R.id.blog_time);
            title = itemView.findViewById(R.id.blog_title);
            username = itemView.findViewById(R.id.blog_user_name);
            blogImage = itemView.findViewById(R.id.blog_image);
            userImage = itemView.findViewById(R.id.blog_user_image);
            date = itemView.findViewById(R.id.blog_date);
            blogLikeBtn = itemView.findViewById(R.id.blog_like_btn);
            blogLikeCount = itemView.findViewById(R.id.blog_like_count);
            commentBtn = itemView.findViewById(R.id.blog_comment_btn);
            blogListConstraintLayout = itemView.findViewById(R.id.blogListConstraintLayout);
            userConstraintLayout = itemView.findViewById(R.id.constraintLayout2);
            blogCommentCount = itemView.findViewById(R.id.blog_comment_count);
            favouriteBlog = itemView.findViewById(R.id.favouriteBlog);

            ///////////////////////////// Comment btn Onclick///////////////////////////////////////////////////////////////
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        final String blogPostId = blogListFiltered.get(position).BlogPostId;
                        mComments = mDialog.findViewById(R.id.commentRecyclerView);
                        dialogDismiss = mDialog.findViewById(R.id.dialogClose);
                        mAdapter = new commentsAdapter(mCommentList);
                        mLinearLayout = new LinearLayoutManager(context);
                        mComments.setHasFixedSize(true);
                        mComments.setLayoutManager(mLinearLayout);
                        mComments.setAdapter(mAdapter);

                        dialogDismiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mDialog.dismiss();
                            }
                        });

                        loadMessages(blogPostId);
                        mDialog.show();
                    }
                }
            });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            //Adding post to favourites////////////////////////////////////////////////////////
            favouriteBlog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        final String blogPostId = blogListFiltered.get(position).BlogPostId;
                        final Animation likeAnim = AnimationUtils.loadAnimation(context,R.anim.like_animation);
                        favouriteBlog.startAnimation(likeAnim);
                        firebaseFirestore.collection("User2/" + currentUserId + "/Favourites").document(blogPostId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().exists()) {
                                        Map<String, Object> favouriteMap = new HashMap<>();
                                        favouriteMap.put("timestamp", System.currentTimeMillis());
                                        firebaseFirestore.collection("User2/" + currentUserId + "/Favourites").document(blogPostId).set(favouriteMap);

                                    } else {
                                        firebaseFirestore.collection("User2/" + currentUserId + "/Favourites").document(blogPostId).delete();
                                    }
                                } else {
                                    Log.i("LikeError", task.getException().getMessage());
                                    Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });


            //////////Liking a post///////////////////
            blogLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        final String blogPostId = blogListFiltered.get(position).BlogPostId;
                        final Animation likeAnim = AnimationUtils.loadAnimation(context,R.anim.like_animation);
                        blogLikeBtn.startAnimation(likeAnim);
                        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().exists()) {

                                        Map<String, Object> likesMap = new HashMap<>();
                                        likesMap.put("timestamp", System.currentTimeMillis());
                                        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(firebaseAuth.getCurrentUser().getUid()).set(likesMap);

                                    } else {
                                        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(firebaseAuth.getCurrentUser().getUid()).delete();
                                    }
                                } else {
                                    Log.i("LikeError", task.getException().getMessage());
                                    Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

            ////////////////////////Opening details activity////////////////////////////////
            blogListConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        BlogPost blogPost = blogListFiltered.get(position);
                        final String blogPostId = blogListFiltered.get(position).BlogPostId;
                        Intent detailIntent = new Intent(context, DetailActivity.class);
                        detailIntent.putExtra("blog_id", blogPostId);
                        detailIntent.putExtra("user_id",blogPost.getUser_id());
                        detailIntent.putExtra("thumb",blogPost.getThumb_url());
                        context.startActivity(detailIntent);
                    }
                }
            });

            ///////////////////////opening user profile //////////////////////
            userConstraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        BlogPost blogPost = blogListFiltered.get(position);
                        Intent intent = new Intent(context, ProfileActivity2.class);
                        intent.putExtra("user_id",blogPost.getUser_id());
                        context.startActivity(intent);
                    }
                }
            });


        }



    }
    private Animation topAnimation, bottomAnimation, startAnimation, endAnimation;
    private SharedPreferences onBoardingPreference;

}
