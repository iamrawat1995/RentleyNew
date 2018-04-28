package com.example1.ankitrawat.rentley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private Button LoginButton;
    private EditText userEmail,userPassword;
    private TextView needNewAccountLink;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        needNewAccountLink = (TextView)findViewById(R.id.register_account_link);
        userEmail = (EditText)findViewById(R.id.login_email);
        userPassword=(EditText)findViewById(R.id.login_password);
        LoginButton = (Button)findViewById(R.id.login_button);

        loadingBar = new ProgressDialog(this);

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sendUserToRegisterActivity();

            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AllowUserToLogin();

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            SendUserToHome();
        }
    }

    private void AllowUserToLogin()
    {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email" , Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter you password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please wait ,logging In your account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                SendUserToHome();
                                Toast.makeText(Login.this, "You are logged in succesfully !", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(Login.this, "error Occured "+ message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                        }
                    });
        }
    }

    private void SendUserToHome()
    {
        Intent homeIntent = new Intent(Login.this,Home.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }

    private void sendUserToRegisterActivity()
    {
        Intent registerIntent = new Intent(Login.this,Register.class);
        startActivity(registerIntent);
        finish();

    }
}
