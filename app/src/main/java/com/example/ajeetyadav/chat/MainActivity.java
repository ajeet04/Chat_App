package com.example.ajeetyadav.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    private ViewPager viewP;
    private SectionsPagerAdapter spa;
    private TableLayout tabL;
    private DatabaseReference mUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mToolbar=(Toolbar)findViewById(R.id.main_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat");
        viewP=(ViewPager)findViewById(R.id.tabPager) ;
        if(mAuth.getCurrentUser()!=null) {

        }
        spa=new SectionsPagerAdapter(getSupportFragmentManager());
        viewP.setAdapter(spa);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
        tabLayout.addTab(tabLayout.newTab().setText("REQUESTS"));
        tabLayout.addTab(tabLayout.newTab().setText("CHATS"));
        tabLayout.addTab(tabLayout.newTab().setText("FRIENDS LIST"));
       tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewP.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            //mUserRef.child("lastsee").setValue(ServerValue.TIMESTAMP);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser==null){
            StartNewActivity();

        }
        else{
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

            mUserRef.child("online").setValue(true);
        }
    }

    private void StartNewActivity() {
        Intent i=new Intent(MainActivity.this,Register.class);
        startActivity(i);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.acs){
            Intent i =new Intent(MainActivity.this,SettingAccount.class);
            startActivity(i);

        }
        if(item.getItemId()==R.id.main_logout){

            FirebaseUser currentUser=mAuth.getCurrentUser();
            if(currentUser!=null) {
                mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            }
            FirebaseAuth.getInstance().signOut();
            StartNewActivity();

        }
        if(item.getItemId()==R.id.all){
            Intent i =new Intent(MainActivity.this,AllUserActivity.class);
            startActivity(i);

        }
        if(item.getItemId()==R.id.home){
            FirebaseUser currentUser=mAuth.getCurrentUser();
            if(currentUser!=null) {
                mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            }
            finish();
        }


        return true;
    }
}
