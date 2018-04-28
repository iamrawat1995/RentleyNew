package com.example1.ankitrawat.rentley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class SetupActivity extends AppCompatActivity {
    private EditText userName,fullName,mobNo,userDOB,userGender;
    private Button saveInfoButton;
    private CircleImageView profileImage;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    String currentUserID;
    final static  int Gallary_pick=1;
    private StorageReference UserProfileImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Image");

        userName = (EditText) findViewById(R.id.setup_username);
       fullName = (EditText) findViewById(R.id.setup_name);
        mobNo = (EditText) findViewById(R.id.setup_mobno);
        saveInfoButton = (Button)findViewById(R.id.save_setup);
        profileImage = (CircleImageView)findViewById(R.id.user_image);

        loadingBar = new ProgressDialog(this);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent,Gallary_pick);

            }
        });

        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                saveAccountSetupInfo();

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("profile_image"))
                    {
                        String image = dataSnapshot.child("profile_image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImage);

                    }
                    else
                    {
                        Toast.makeText(SetupActivity.this, "Please select profile image first", Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    @Override
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
                            Toast.makeText(SetupActivity.this, "Your profile picture has been saved !", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            databaseReference.child("profile_image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent setupIntent = new Intent(SetupActivity.this,SetupActivity.class);
                                        startActivity(setupIntent);

                                        Toast.makeText(SetupActivity.this, "profile pic is stored to database", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                    }
                                    else
                                    {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, "Error Occured" + message , Toast.LENGTH_SHORT).show();
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

    private void saveAccountSetupInfo()
    {
        String userInfoname = userName.getText().toString();
        String userFullName = fullName.getText().toString();
        String userMob = mobNo.getText().toString();

        if (TextUtils.isEmpty(userInfoname))
        {
            Toast.makeText(this, "Please provide username !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userFullName))
        {
            Toast.makeText(this, "Please provide Full Name !", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(userMob)){
            Toast.makeText(this, "Please provide Your mob number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Your Information");
            loadingBar.setMessage("Please wait ,while we are saving your information !");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("Username",userInfoname);
            userMap.put("FullName",userFullName);
            userMap.put("Mobile",userMob);
            userMap.put("Gender","None");
            userMap.put("DateOfBirth","DOB");
            databaseReference.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isComplete())
                    {
                        SendUserToMainHome();
                        Toast.makeText(SetupActivity.this, "Your account is created succesfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else {
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "Error Occured "+ message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }

                    }

                }
            });

        }


    }

    private void SendUserToMainHome()
    {
        Intent homeMainIntent = new Intent(SetupActivity.this,Home.class);
        homeMainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeMainIntent);
    }
}
