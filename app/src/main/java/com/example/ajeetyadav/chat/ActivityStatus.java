package com.example.ajeetyadav.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ActivityStatus extends AppCompatActivity {
  private Toolbar mToolBar;
    private Button setStatus;
    private TextInputLayout qoutes;
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mToolBar=(Toolbar)findViewById(R.id.main_status);
        pg=new ProgressDialog(this);
       // mToolBar=(Toolbar)findViewById(R.id.main_status);
       // setSupportActionBar(mToolBar);
       // getSupportActionBar().setTitle("Status");
        setStatus=(Button)findViewById(R.id.set);
        String status_value=getIntent().getStringExtra("prev");
        qoutes=(TextInputLayout)findViewById(R.id.write);
        qoutes.getEditText().setText(status_value);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();

        String uid=mCurrentUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        setStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg.setTitle("save changes");
                pg.setMessage("please wait...");
                pg.show();
                String status=qoutes.getEditText().getText().toString();
                mDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pg.dismiss();

                            Intent si=new Intent(ActivityStatus.this,SettingAccount.class);
                            startActivity(si);
                            finish();

                        }
                    }
                });
            }
        });

    }
}
