package com.example.ajeetyadav.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ajeet Yadav on 9/14/2017.
 */


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    Context ctx;
    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    public MessageAdapter(){}


    public MessageAdapter(List<Messages> mMessageList) {
       //this.ctx=ctx;
        this.mMessageList = mMessageList;
       // inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }



    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single ,parent, false);


        return new MessageViewHolder(v);

    }




    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText,messageText1;
        public CircleImageView profileImage;
        public TextView time,time1;
        public RelativeLayout mLayout,mLayout1;
        public FrameLayout fLayout,flayout1;

        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.txtMsgFrom);
            mLayout=(RelativeLayout)view.findViewById(R.id.flayout);
             time = (TextView) view.findViewById(R.id.txtTimeFrom);
            messageText1 = (TextView) view.findViewById(R.id.txtMsgFrom1);
            mLayout1=(RelativeLayout)view.findViewById(R.id.flayout1);
            time1= (TextView) view.findViewById(R.id.txtTimeFrom1);

        }
        public void loadMessages() {
        }


    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
      //  RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.llGrandParent.getLayoutParams();
  FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String mCurrentUser=mAuth.getCurrentUser().getUid().toString();
        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        long t=c.getTime();
        GetTimeAgo getTimeAgo = new GetTimeAgo();
        String lastSeenTime = getTimeAgo.getTimeAgo(t);
        String st=c.getMessage();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String time = dataSnapshot.child("thumb_image").getValue().toString();


              // viewHolder.displayName.setText(name);

               // Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.profile).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
if(mCurrentUser!=from_user){
    viewHolder.mLayout.setVisibility(View.VISIBLE);
    //viewHolder.mLatyout.setGravity(Gravity. LEFT);
    viewHolder.messageText.setText(st);
    viewHolder.time.setText(lastSeenTime);
    viewHolder.mLayout1.setVisibility(View.INVISIBLE);


}else{
        viewHolder.messageText.setText(st);
    viewHolder.mLayout1.setVisibility(View.VISIBLE);
    //viewHolder.mLatyout.setGravity(Gravity. LEFT);
    viewHolder.messageText1.setText(st);
    viewHolder.time1.setText(lastSeenTime);
    viewHolder.mLayout.setVisibility(View.INVISIBLE);
}

    }



    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



}