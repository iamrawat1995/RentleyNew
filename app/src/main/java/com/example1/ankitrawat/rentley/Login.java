package com.example1.ankitrawat.rentley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;


import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
    EditText email,password;
    Button login;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            //user already sign in
        }else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build())).build(), RC_SIGN_IN
            );
        }



        /*email=(EditText)findViewById(R.id.editText5);
        password=(EditText)findViewById(R.id.editText4);
        activity_login=(Button)findViewById(R.id.button3);
        activity_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fetched_email="ankit",fetched_password="rawat";

                if(email.getText().toString().trim().equals(fetched_email) && password.getText().toString().trim().equals(fetched_password))
                {
                    Intent i=new Intent(Login.this,Main_home.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(Login.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                //user logged in
                Log.d("AUTH",auth.getCurrentUser().getEmail());
            }
            else {
                //user not authenticated
                Log.d("AUTH","NOT AUTHENTICATED");
            }
        }

    }

}
