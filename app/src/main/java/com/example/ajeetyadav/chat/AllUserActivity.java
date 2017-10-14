package com.example.ajeetyadav.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ajeetyadav.chat.AllUserActivity.UserViewHolder.setName;


public class AllUserActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private RecyclerView rCv;
    private DatabaseReference mUserDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        mToolBar=(Toolbar)findViewById(R.id.user_appbar);
        rCv=(RecyclerView)findViewById(R.id.user_list);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("All User");
        String UserId=getIntent().getStringExtra("user_id");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserDataBase= FirebaseDatabase.getInstance().getReference().child("Users");
        rCv.setHasFixedSize(true);
        rCv.setLayoutManager(new LinearLayoutManager(this) );





    }

    @Override
    protected void onStart() {
        super.onStart();
        Query mQuery=mUserDataBase.orderByChild("name");
        FirebaseRecyclerAdapter<Users,UserViewHolder> fireBaseAdapter=new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,R.layout.user_single,UserViewHolder.class,mQuery
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users users, int position) {
                setName(users.getName());
               UserViewHolder.setUserImage(users.getThumb_image(),getApplicationContext());
                UserViewHolder.setStatus(users.getStatus());
                final String user_id=getRef(position).getKey();
                UserViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in=new Intent(AllUserActivity.this,UserProfile.class);
                        in.putExtra("user_id",user_id);
                        startActivity(in);


                    }
                });
            }

        };
        rCv.setAdapter(fireBaseAdapter);
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder{
      static View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
          public static void setName(String name){
              TextView userNameView=(TextView)mView.findViewById(R.id.userName);
              userNameView.setText(name);


        }
        public static void setStatus(String status){
            TextView userNameView=(TextView)mView.findViewById(R.id.user_status);
            userNameView.setText(status);


        }
        public static void setUserImage(String image, Context ctx){
            CircleImageView userNameView=(CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(image).placeholder(R.drawable.profile).into(userNameView);



        }


    }

}
