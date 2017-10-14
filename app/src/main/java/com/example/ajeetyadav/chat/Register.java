package com.example.ajeetyadav.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private static final String TAG = "ac";
    private TextInputLayout UserName,mail, pass;
    private Button createAc;
    private TextView login;
    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    ProgressDialog mProgress;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        UserName =(TextInputLayout)findViewById(R.id.user);
        pass =(TextInputLayout)findViewById(R.id.password);
        mail =(TextInputLayout)findViewById(R.id.email);
        createAc =(Button)findViewById(R.id.create);
        mToolbar=(Toolbar)findViewById(R.id.register_toolbar);
        login=(TextView)findViewById(R.id.already);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgress=new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIn=new Intent(Register.this,LoginActivity.class);
                startActivity(newIn);
                finish();
            }
        });

        createAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=mail.getEditText().getText().toString().trim();
                String Name=UserName.getEditText().getText().toString().trim();
                Name = Name.substring(0,1).toUpperCase() + Name.substring(1).toLowerCase();
                String password=pass.getEditText().getText().toString().trim();
                if(!TextUtils.isEmpty(Email)|| !TextUtils.isEmpty(Name)|| !TextUtils.isEmpty(password)){
                     mProgress.setTitle("Registering User");
                    mProgress.setMessage("Please wait while we creating your account!");
                    mProgress.show();
                    mProgress.setCanceledOnTouchOutside(false);
                    register_user(Name,Email,password);
                }
                else{
                    Toast.makeText(Register.this, "Field cannot empty", Toast.LENGTH_SHORT).show();
                }



            }
        });



    }
    public void register_user(final String uName , String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser mUser=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=mUser.getUid();
                    mDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    HashMap<String,String> userMap=new HashMap<String, String>();
                    String deviceToken= FirebaseInstanceId.getInstance().getToken();
                    userMap.put("name",uName);
                    userMap.put("device_token",deviceToken);
                    userMap.put("status","Hi there using this app");
                    userMap.put("image","default");
                    userMap.put("thumb_image","default");
                     mDataBase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 mProgress.dismiss();
                                 Intent mIn=new Intent(Register.this,MainActivity.class);
                                 mIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 startActivity(mIn);
                                 finish();

                             }

                         }
                     });



                }
                else{
                    mProgress.hide();
                    Toast.makeText(Register.this, "You got some error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}
