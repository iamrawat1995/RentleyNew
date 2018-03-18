package com.example1.ankitrawat.rentley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    EditText name,number,email,password,confirm;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=(EditText)findViewById(R.id.editText);
        number=(EditText)findViewById(R.id.editText2);
        email=(EditText)findViewById(R.id.editText3);
        password=(EditText)findViewById(R.id.editText6);
        confirm=(EditText)findViewById(R.id.editText7);
        register=(Button)findViewById(R.id.button4);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Register.this,Login.class);
                    startActivity(i);

            }
        });
    }
}
