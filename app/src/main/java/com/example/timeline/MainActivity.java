package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button signInBtn, registerBtn;
    DBConnect db = new DBConnect(this);
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        signInBtn = findViewById(R.id.signInBtn);
        registerBtn = findViewById(R.id.registerBtn);
        layout = findViewById(R.id.layout);

        //initializing the admin log in credentials
        String adminUser = "admin";
        String adminPass = "secret";

        //click listener for register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goes to the register.java activity
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user= edtUsername.getText().toString();
                String pass= edtPassword.getText().toString();

                //validation: checking if any of the inputs is empty
                if (user.isEmpty() || pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "All fields are required!", Toast.LENGTH_LONG).show();
                }
                else{
                    //creating users object
                    Users u1 = new Users(user,pass);
                    boolean result = db.loginCheck(u1);
                    //checking if the data matches the admin log in details
                    if (user.equals(adminUser) && pass.equals(adminPass)){
                        //opening the admin.java activity
                        Intent SendIntent = new Intent(MainActivity.this, Admin.class);
                        startActivity(SendIntent);
                    }
                    //checking if the data matches any data in the database
                    else if (result){
                        //opening the general timeline activity
                        Intent SendIntent = new Intent(MainActivity.this, GeneralTimeline.class);
                        SendIntent.putExtra("userId", Integer.toString(db.getId(user)));
                        startActivity(SendIntent);

                    }
                    else{
                        //showing error message to the user
                        Toast.makeText(MainActivity.this, "Wrong credentials!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}