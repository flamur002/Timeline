package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText nameRgs, surnameRgs, usernameRgs, passwordRgs;
    Button register;
    DBConnect db = new DBConnect(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameRgs = findViewById(R.id.nameRgs);
        surnameRgs = findViewById(R.id.surnameRgs);
        usernameRgs = findViewById(R.id.usernameRgs);
        passwordRgs = findViewById(R.id.passwordRgs);
        register = findViewById(R.id.button);

        //click listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the values entered by the user
                String strName = nameRgs.getText().toString();
                String strSurname = surnameRgs.getText().toString();
                String strUsername = usernameRgs.getText().toString();
                String strPassword = passwordRgs.getText().toString();

                //checking if any of the values is empty
                if(strName.isEmpty() || strPassword.isEmpty() || strSurname.isEmpty() || strUsername.isEmpty()){
                    Toast.makeText(Register.this, "All field are required!", Toast.LENGTH_LONG).show();
                }
                else{
                    Users u2 = new Users(strUsername);
                    boolean exists = db.emailCheck(u2);
                    //checking if the username is already being used
                    if (exists || strUsername.equals("admin")){
                        Toast.makeText(Register.this, "Username taken!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //creating users object
                        Users u1 = new Users(strName, strSurname, strUsername, strPassword);
                        //calling database function to add a user
                        boolean result = db.addUser(u1);
                        if(result){
                            Toast.makeText(Register.this, "User registered!", Toast.LENGTH_LONG).show();
                            //going to the log in page
                            startActivity(new Intent(Register.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this, "User NOT registered!", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }
        });

    }
}