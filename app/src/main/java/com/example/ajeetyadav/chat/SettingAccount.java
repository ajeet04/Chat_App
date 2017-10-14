package com.example.ajeetyadav.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingAccount extends AppCompatActivity {
    private TextView userName,userStatus;
    private Button changeStatus;
    private CircleImageView changeProfile;
    private DatabaseReference mUserDb;
    private FirebaseUser mCurrentUser;
    private static int GALLERY_PICK=1;
    final int PIC_CROP = 2;;
    private List<Movie> sList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private StorageReference mImageStorage;
    private ProgressDialog pg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);;
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();

       // userName=(TextView)findViewById(R.id.nameD);
       // userStatus=(TextView)findViewById(R.id.status1);
        mImageStorage=FirebaseStorage.getInstance().getReference();
        changeProfile=(CircleImageView)findViewById(R.id.image);
       // changeStatus=(Button)findViewById(R.id.statusB);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.setpic);
        mImageStorage= FirebaseStorage.getInstance().getReference();
        final String current_uid=mCurrentUser.getUid();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.slist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        prepareMovieData();
       // ArrayList mylist = prepareData();
        MoviesAdapter adapter = new MoviesAdapter(sList);
        recyclerView.setAdapter(adapter);


        mUserDb= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDb.keepSynced(true);
        mUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                final String image=dataSnapshot.child("image").getValue().toString();
                String image_thumb=dataSnapshot.child("thumb_image").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();

                //userName.setText(name);
                //userStatus.setText(status);
                if(!image.equals("default"))
                Picasso.with(SettingAccount.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).
                        placeholder(R.drawable.profile).into(changeProfile, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(SettingAccount.this).load(image).placeholder(R.drawable.profile).into(changeProfile);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      /*  changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prev_status=userStatus.getText().toString();
                Intent statusIntent=new Intent(SettingAccount.this,ActivityStatus.class);
                statusIntent.putExtra("prev",prev_status);
                startActivity(statusIntent);
                finish();

            }
        });*/
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Animations();
                Intent showPic=new Intent(SettingAccount.this,ShowPicture.class);
                showPic.putExtra("user_id",current_uid);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(showPic);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery=new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,GALLERY_PICK);

            }
        });

    }



    private void Animations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setAspectRatio(1,1).setMinCropWindowSize(500,500)
                    .start(this);



        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                pg=new ProgressDialog(this);
                pg.setTitle("Image uploading.");
                pg.setMessage("Please wait..");
                pg.setCanceledOnTouchOutside(false);
                pg.show();

                Uri resultUri = result.getUri();
                File thumb_file=new File(resultUri.getPath());

                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(75).compressToBitmap(thumb_file);
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    final byte [] thumb_byte=baos.toByteArray();


                    String userId=mCurrentUser.getUid();
                    StorageReference filePath=mImageStorage.child("profile_images").child(userId+".jpg");

                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                @SuppressWarnings("VisibleForTests") final String downloadUrl=task.getResult().getDownloadUrl().toString();
                                String userId=mCurrentUser.getUid();
                                StorageReference thumb=mImageStorage.child("profile_images").child("thumbs").child(userId+".jpg");
                                UploadTask uplaod=thumb.putBytes(thumb_byte);
                                uplaod.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                        @SuppressWarnings("VisibleForTests")  String thumb_downloadUrl=thumb_task.getResult().getDownloadUrl().toString();
                                        Map update=new HashMap<String, String>();
                                        update.put("image",downloadUrl);
                                        update.put("thumb_image",thumb_downloadUrl);
                                        if(thumb_task.isSuccessful()){
                                            mUserDb.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        pg.dismiss();
                                                        Toast.makeText(SettingAccount.this, "SuccessFully uploaded", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(SettingAccount.this, "Not Working thumbnail", Toast.LENGTH_LONG).show();
                                                        pg.dismiss();
                                                    }
                                                }
                                            });

                                        }
                                        else{
                                            Toast.makeText(SettingAccount.this, "Not Working thumbnail out", Toast.LENGTH_LONG).show();
                                            pg.dismiss();
                                        }

                                    }
                                });


                            }
                            else{
                                Toast.makeText(SettingAccount.this, "Not Working", Toast.LENGTH_LONG).show();
                                pg.dismiss();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                pg.dismiss();
            }
        }




        }
    private void prepareMovieData() {
        Movie movie = new Movie("Account",R.drawable.ac);
        sList.add(movie);

        movie = new Movie("Status", R.drawable.back);
       sList.add(movie);

        movie = new Movie("Notification", android.R.drawable.ic_notification_clear_all);
        sList.add(movie);

        movie = new Movie("Data Usage",  R.drawable.im);
        sList.add(movie);

        movie = new Movie("Contact", android.R.drawable.checkbox_off_background);
        sList.add(movie);

        movie = new Movie("Help",android.R.drawable.ic_menu_help);
        sList.add(movie);


       //adapter.notifyDataSetChanged();
    }




}





    

