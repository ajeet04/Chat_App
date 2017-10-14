package com.example.ajeetyadav.chat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private RecyclerView myChatList;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    private View mView;



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView= inflater.inflate(R.layout.fragment_chat, container, false);
        myChatList=(RecyclerView)mView.findViewById(R.id.chatList);
        mAuth= FirebaseAuth.getInstance();

        mCurrent_user_id=mAuth.getCurrentUser().getUid();
        mFriendDatabase= FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        myChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFriendDatabase.keepSynced(true);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query mQuery=mFriendDatabase.orderByChild("time");
        FirebaseRecyclerAdapter<Messages,FriendViewHolder> firebaseAdapter=new FirebaseRecyclerAdapter<Messages, FriendViewHolder>(
                Messages.class,R.layout.friend_list,FriendViewHolder.class,mQuery
        ) {
            @Override
            protected void populateViewHolder(final FriendViewHolder viewHolder, final Messages friend, final int position) {


                final String user_list_id=getRef(position).getKey();
                mFriendDatabase.child(user_list_id).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Messages message = dataSnapshot.getValue(Messages.class);
                        viewHolder.setDate(message.getMessage());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Messages message = dataSnapshot.getValue(Messages.class);
                        viewHolder.setDate(message.getMessage());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Messages message = dataSnapshot.getValue(Messages.class);
                        viewHolder.setDate(message.getMessage());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mUserDatabase.child(user_list_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name=dataSnapshot.child("name").getValue().toString();
                        final String imageUrl=dataSnapshot.child("thumb_image").getValue().toString();

                        viewHolder.setName(name);
                        viewHolder.setImage(imageUrl,getContext());
                        if(dataSnapshot.hasChild("online")) {
                            String userOnline=dataSnapshot.child("online").getValue().toString();
                            viewHolder.setOnlineStatus(userOnline);
                        }
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                            Intent chatIn=new Intent(getContext(),Chat1Activity.class);
                                            chatIn.putExtra("user_id",user_list_id);
                                            chatIn.putExtra("user_name",name);
                                            chatIn.putExtra("user_image",imageUrl);
                                            startActivity(chatIn);





                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        myChatList.setAdapter(firebaseAdapter);
    }
    static class FriendViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public FriendViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public  void setDate(String date){
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
