package com.example.ajeetyadav.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ShowPicture extends AppCompatActivity {
private Toolbar sToolbar;
    private ImageView picture;
    private ProgressDialog pg;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    String mCurrentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        sToolbar = (Toolbar) findViewById(R.id.pic_bar);
        setSupportActionBar(sToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Chat");

        String Current_user= getIntent().getStringExtra("user_id");
        pg=new ProgressDialog(this);
        pg.setCanceledOnTouchOutside(false);
        Animations();
        pg.setTitle("please wait");
        pg.setMessage("Loading...");
        pg.show();
        picture=(ImageView)findViewById(R.id.picShow);
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mRootRef.child(Current_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                final String Image=dataSnapshot.child("image").getValue().toString();
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + name + "</font>"));
                if(!Image.equals("default"))
                    Picasso.with(ShowPicture.this).load(Image).networkPolicy(NetworkPolicy.OFFLINE).
                            placeholder(R.drawable.profile).into(picture, new Callback() {
                        @Override
                        public void onSuccess() {
                            pg.dismiss();

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ShowPicture.this).load(Image).placeholder(R.drawable.profile).into(picture);
                            pg.dismiss();

                        }
                    });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pg.dismiss();

            }
        });



    }

    private void Animations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
        getWindow().setEnterTransition(fade);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.home){
           finish();

        }
         return super.onOptionsItemSelected(item);
    }

}
