package com.example1.ankitrawat.rentley;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddTenantActivity extends AppCompatActivity {


    private EditText tenantName,tenantMobile,tenantAdress;
    private Button saveTenantButton;
    private DatabaseReference tenantUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private ProgressDialog loadingBar;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tenant);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
       tenantUserRef = FirebaseDatabase.getInstance().getReference().child("Tenants").child(currentUserID).push();


        mToolBar = (Toolbar) findViewById(R.id.tenant_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Add Tenant");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tenantName = (EditText)findViewById(R.id.tenant_fullName);
        tenantMobile = (EditText)findViewById(R.id.tenant_mobile);
        tenantAdress = (EditText)findViewById(R.id.tenant_address);
        saveTenantButton = (Button)findViewById(R.id.tenant_info_submit);

        loadingBar = new ProgressDialog(this);

        saveTenantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                saveTenantSetupInfo();

            }
        });




    }

    private void saveTenantSetupInfo()
    {
        String tenantname = tenantName.getText().toString();
        String tenantmobile = tenantMobile.getText().toString();
        String tenantadress= tenantAdress.getText().toString();

        if (TextUtils.isEmpty(tenantname))
        {
            Toast.makeText(this, "Please provide tenant Full Name !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(tenantmobile))
        {
            Toast.makeText(this, "Please provide tenant mobile number !", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(tenantadress)){
            Toast.makeText(this, "Please provide tenant adress", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Your Information");
            loadingBar.setMessage("Please wait ,while we are saving your information !");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("TenantName",tenantname);
            userMap.put("TenantMobile",tenantmobile);
            userMap.put("TenantAdress",tenantadress);
            tenantUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isComplete())
                    {
                        SendUserToMainHome();
                        Toast.makeText(AddTenantActivity.this, "Your account is created succesfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else {
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(AddTenantActivity.this, "Error Occured "+ message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }

                    }

                }
            });

        }


    }

    private void SendUserToMainHome()
    {
        Intent homeMainIntent = new Intent(AddTenantActivity.this,Home.class);
        homeMainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeMainIntent);
    }
}
