package com.example1.ankitrawat.rentley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private EditText userFullName,userName,userMob,userDOB,userGender;
    private Button updateAccountButton;
    protected CircleImageView updateImage;
    private DatabaseReference settingsUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    final static  int Gallary_pick=1;
    private ProgressDialog loadingBar;
    private StorageReference UserProfileImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        settingsUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Image");

        mToolBar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Update Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userFullName = (EditText)findViewById(R.id.settings_fullName);
        userName = (EditText)findViewById(R.id.settings_userName);
        userMob = (EditText)findViewById(R.id.settings_phone_number);
        userDOB= (EditText)findViewById(R.id.settings_DOB);
        userGender = (EditText)findViewById(R.id.settings_gender);
        updateImage = (CircleImageView)findViewById(R.id.settings_profile_image);
        updateAccountButton = (Button)findViewById(R.id.settings_update_button);

        settingsUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String myProfileImage= dataSnapshot.child("profile_image").getValue().toString();
                    String myFullName= dataSnapshot.child("FullName").getValue().toString();
                    String myUserName= dataSnapshot.child("Username").getValue().toString();
                    String myMob= dataSnapshot.child("Mobile").getValue().toString();
                    String myGender= dataSnapshot.child("Gender").getValue().toString();
                    String myDOB= dataSnapshot.child("DateOfBirth").getValue().toString();

                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(updateImage);

                    userFullName.setText(myFullName);
                    userName.setText(myUserName);
                    userMob.setText(myMob);
                    userDOB.setText(myDOB);
                    userGender.setText(myGender);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidiateAccountInfo();

            }
        });

        loadingBar = new ProgressDialog(this);
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent,Gallary_pick);

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallary_pick && resultCode == RESULT_OK && data!= null)
        {
            Uri ImageUri = data.getData();
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);

        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Profile Picture");
                loadingBar.setMessage("Please wait ,while we are updating your profile picture!");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();


                Uri resultUri = result.getUri();
                StorageReference filepath = UserProfileImageRef.child(currentUserID + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this, "Your profile picture has been saved !", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            settingsUserRef.child("profile_image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent setupIntent = new Intent(SettingsActivity.this,SettingsActivity.class);
                                        startActivity(setupIntent);

                                        Toast.makeText(SettingsActivity.this, "profile pic is stored to database", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                    }
                                    else
                                    {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SettingsActivity.this, "Error Occured" + message , Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }

                                }
                            });
                        }

                    }
                });
            }
            else {
                Toast.makeText(this, "Error Occured :Image can not be cropped, Please Try Again!", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void ValidiateAccountInfo()
    {
        String userfullname = userFullName.getText().toString();
        String username = userName.getText().toString();
        String usermob = userMob.getText().toString();
        String userdob = userDOB.getText().toString();
        String usergender = userGender.getText().toString();

        if(TextUtils.isEmpty(userfullname))
        {
            Toast.makeText(this, "Please write your full name..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "please write your username..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(usermob))
        {
            Toast.makeText(this, "please write your mobile number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userdob))
        {
            Toast.makeText(this, "please write your date of birth..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(usergender))
        {
            Toast.makeText(this, "please write your gender..", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Profile Picture");
            loadingBar.setMessage("Please wait ,while we are updating your profile picture!");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            UpdateAccountInformation(userfullname,username,usermob,userdob,usergender);
        }
    }

    private void UpdateAccountInformation(String userfullname, String username, String usermob, String userdob, String usergender)
    {
        HashMap userMap = new HashMap();
        userMap.put("FullName",userfullname);
        userMap.put("Username",username);
        userMap.put("Mobile",usermob);
        userMap.put("DateOfBirth",userdob);
        userMap.put("Gender",usergender);
        settingsUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    SendUserToHome();
                    Toast.makeText(SettingsActivity.this, "Account settings updated succesfully..", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else 
                {
                    Toast.makeText(SettingsActivity.this, "Error occured while updating account information...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
        });
    }
    private void SendUserToHome()
    {
        Intent homeIntent = new Intent(SettingsActivity.this,Home.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }

}
