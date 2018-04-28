package com.example1.ankitrawat.rentley;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ANKIT RAWAT on 18-Mar-18.
 */

public class ProfileActivity extends Fragment {

    private TextView userFullName,userName,userMob,userDOB,userGender;
    private CircleImageView myProfileImage;
    private ImageView updateProfile;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_accounts, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        updateProfile = (ImageView) view.findViewById(R.id.update_the_settings);
        userFullName = (TextView) view.findViewById(R.id.my_profile_fullname);
        userName = (TextView) view.findViewById(R.id.my_profile_username);
        userMob = (TextView) view.findViewById(R.id.my_profile_phone);
        userDOB= (TextView) view.findViewById(R.id.my_profile_DOB);
        userGender = (TextView) view.findViewById(R.id.my_profile_gender);
        myProfileImage = (CircleImageView) view.findViewById(R.id.my_profile_pic);


        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {

                    String myProfilepic= dataSnapshot.child("profile_image").getValue().toString();
                    String myFullName= dataSnapshot.child("FullName").getValue().toString();
                    String myUserName= dataSnapshot.child("Username").getValue().toString();
                    String myMob= dataSnapshot.child("Mobile").getValue().toString();
                    String myGender= dataSnapshot.child("Gender").getValue().toString();
                    String myDOB= dataSnapshot.child("DateOfBirth").getValue().toString();

                    Picasso.get().load(myProfilepic).placeholder(R.drawable.profile).into(myProfileImage);

                    userFullName.setText("Name :"+myFullName);
                    userName.setText("UserName :"+myUserName);
                    userMob.setText("Mobile :"+myMob);
                    userDOB.setText("DOB :"+myDOB);
                    userGender.setText("Gender :"+myGender);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendUserToSettingsActivity();

            }
        });

        return view;
    }

    private void SendUserToSettingsActivity()
    {
        Intent settingsIntent = new Intent(ProfileActivity.this.getActivity(), SettingsActivity.class);
        startActivity(settingsIntent);
    }
}