package com.example.ajeetyadav.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    private Button dont;
   private TextInputLayout  mEmailView;
    private TextInputLayout  mPasswordView;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar=(Toolbar)findViewById(R.id.login_appbar);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        mProgress=new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set up the login form.
        mEmailView = (TextInputLayout ) findViewById(R.id.email);


        mPasswordView = (TextInputLayout) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.user_sign);
        dont = (Button) findViewById(R.id.newAc);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailView.getEditText().getText().toString().trim();
                String password = mPasswordView.getEditText().getText().toString().trim();
                if(!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){
                    mProgress.setTitle("Logging in");
                    mProgress.setMessage("Please wait while we checking credentials.");
                    mProgress.setCanceledOnTouchOutside(true);
                    mProgress.show();
                    loginUser(email, password);
                }
            }
        });
        dont.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIn=new Intent(LoginActivity.this,Register.class);
                startActivity(newIn);
                finish();
            }
        });

      
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String current_user_id=mAuth.getCurrentUser().getUid();
                            String deviceToken= FirebaseInstanceId.getInstance().getToken();
                            mDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgress.dismiss();
                                    //Toast.makeText(LoginActivity.this, "emailID or Password are ccvvvvcvbbvv", Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(LoginActivity.this,MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();

                                }
                            });

                            
                        }
                        else{
                            mProgress.hide();
                            Toast.makeText(LoginActivity.this, "emailID or Password are incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.home){
            finish();
        }
        return true;
    }


    }


