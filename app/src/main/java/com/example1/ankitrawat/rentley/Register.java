package com.example1.ankitrawat.rentley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

   private EditText UserEmail,UserPassword,UserConfirm;
    private Button registerAccountButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

       //name=(EditText)findViewById(R.id.editText);
        //number=(EditText)findViewById(R.id.editText2);
        UserEmail=(EditText)findViewById(R.id.register_email);
        UserPassword=(EditText)findViewById(R.id.register_password);
        UserConfirm=(EditText)findViewById(R.id.confirm_password);
        registerAccountButton=(Button)findViewById(R.id.register_button);
        loadingBar = new ProgressDialog(this);




        registerAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                CreateNewAccount();
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

    private void SendUserToHome()
    {
        Intent homeIntent = new Intent(Register.this,Home.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
    private void CreateNewAccount()
    {
        String email = UserEmail.getText().toString();
        String password= UserPassword.getText().toString();
        String confirmPassword= UserConfirm.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your email !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, "Please confirm the password", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Password Does not match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("Please wait ,while we are creating your new account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                          if(task.isSuccessful())
                          {
                              SendUserToSetupActivity();
                              Toast.makeText(Register.this, "You have authenticated succesfully", Toast.LENGTH_SHORT).show();
                              loadingBar.dismiss();
                          }
                          else
                          {
                              String message = task.getException().getMessage();
                              Toast.makeText(Register.this, "Error occured" + message, Toast.LENGTH_SHORT).show();
                              loadingBar.dismiss();
                          }
                        }
                    });

        }
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(Register.this,SetupActivity.class);
        setupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);

    }
}
