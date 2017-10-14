package com.example.ajeetyadav.chat;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Freinds extends Fragment {
    private RecyclerView myFriendList;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    private View mView;


    public Freinds() {

    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView= inflater.inflate(R.layout.fragment_freinds, container, false);
        myFriendList=(RecyclerView)mView.findViewById(R.id.friendlist);
        mAuth=FirebaseAuth.getInstance();

        mCurrent_user_id=mAuth.getCurrentUser().getUid();
        mFriendDatabase= FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        myFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFriendDatabase.keepSynced(true);
        return mView;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<mFriends,FriendViewHolder> firebaseAdapter=new FirebaseRecyclerAdapter<mFriends, FriendViewHolder>(
                mFriends.class,R.layout.friend_list,FriendViewHolder.class,mFriendDatabase
        ) {
            @Override
            protected void populateViewHolder(final FriendViewHolder viewHolder, final mFriends friend, final int position) {

                final String user_list_id=getRef(position).getKey();
                mUserDatabase.child(user_list_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name=dataSnapshot.child("name").getValue().toString();
                        final String imageUrl=dataSnapshot.child("thumb_image").getValue().toString();
                        final String status=dataSnapshot.child("status").getValue().toString();
                        viewHolder.setStatus(status);

                        viewHolder.setName(name);
                       viewHolder.setImage(imageUrl,getContext());
                        if(dataSnapshot.hasChild("online")) {
                            String userOnline=dataSnapshot.child("online").getValue().toString();
                            viewHolder.setOnlineStatus(userOnline);
                        }
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               CharSequence options[]=new CharSequence[]{"Open Profile","Send Message"};
                                AlertDialog.Builder dailog=new AlertDialog.Builder(getContext());
                                dailog.setTitle("select options");
                                dailog.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i==0){
                                            Intent profileIn=new Intent(getContext(),UserProfile.class);
                                            profileIn.putExtra("user_id",user_list_id);
                                            startActivity(profileIn);
                                        }
                                        if(i==1){
                                            Intent chatIn=new Intent(getContext(),Chat1Activity.class);
                                            chatIn.putExtra("user_id",user_list_id);
                                            chatIn.putExtra("user_name",name);
                                            chatIn.putExtra("user_image",imageUrl);
                                            startActivity(chatIn);

                                        }

                                    }
                                }); dailog.show();

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        myFriendList.setAdapter(firebaseAdapter);
    }
   static class FriendViewHolder extends RecyclerView.ViewHolder {
         View mView;
        public FriendViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public  void setStatus(String date){
            TextView userName=(TextView)mView.findViewById(R.id.user_status1);
            userName.setText(date);

        }
        public  void setName(String name){
            TextView userName=(TextView)mView.findViewById(R.id.userName1);
            userName.setText(name);

        }
        public  void setImage(String image, Context ctcx){
            CircleImageView userName=(CircleImageView) mView.findViewById(R.id.user_single_image1);
            Picasso.with(ctcx).load(image).placeholder(R.drawable.profile).into(userName);

        }
       public  void setOnlineStatus(String st){
           TextView userName=(TextView)mView.findViewById(R.id.online);
           if(st.equals("true"))
           userName.setText("online");
           else
               userName.setVisibility(View.INVISIBLE);

       }


    }


    }


