package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    TextView lastUpdated;
    EditText updName, updSurname,updUsername, updPassword;
    Button updateBtn, deleteBtn;
    DBConnect db = new DBConnect(this);
    ArrayList<String> details = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting the variables passed by intent
        Intent getAllIntent = getIntent();
        String getCurrentIntent = getAllIntent.getStringExtra("userId");

        lastUpdated = findViewById(R.id.lastUpdated);
        updName = findViewById(R.id.updName);
        updSurname = findViewById(R.id.updSurname);
        updUsername = findViewById(R.id.updUsername);
        updPassword = findViewById(R.id.updPassword);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        //fetching the current user details from the database
        details = db.getUserDetails(getCurrentIntent);

        //setting the text fields to the matching data
        updName.setText(details.get(0));
        updSurname.setText(details.get(1));
        updUsername.setText(details.get(2));
        updPassword.setText(details.get(3));
        lastUpdated.setText("Last updated on "+details.get(4));

        //click listener for update button
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the values entered by the user
                String strName = updName.getText().toString();
                String strSurname = updSurname.getText().toString();
                String strUsername = updUsername.getText().toString();
                String strPassword = updPassword.getText().toString();

                //checking if any of the values is empty
                if(strName.isEmpty() || strPassword.isEmpty() || strSurname.isEmpty() || strUsername.isEmpty()){
                    Toast.makeText(Profile.this, "All field are required!", Toast.LENGTH_LONG).show();
                }
                else{
                    Users user = new Users(strUsername);
                    boolean exists = db.emailCheck(user);
                    //checking if the username is already taken by another account
                    if (exists && !details.get(2).equals(strUsername) || strUsername.equals("admin")){
                        Toast.makeText(Profile.this, "Username taken!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //creating users object
                        Users u1 = new Users(strName, strSurname, strUsername, strPassword, getCurrentIntent);
                        //calling database function to update user details
                        boolean result = db.updateUserDetails(u1, getCurrentIntent);
                        if(result){
                            Toast.makeText(Profile.this, "User Updated!", Toast.LENGTH_LONG).show();

                            //going back to the general timeline activity
                            Intent SendIntent = new Intent(Profile.this, GeneralTimeline.class);
                            SendIntent.putExtra("userId", getCurrentIntent);
                            startActivity(SendIntent);
                        }
                        else{
                            Toast.makeText(Profile.this, "User NOT Updated!", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }
        });

        //click listener for delete button
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deleting user comments first
                db.deleteUserComments(getCurrentIntent);
                //deleting user account
                db.deleteUser(getCurrentIntent);
                //going to the log in activity
                Intent SendIntent = new Intent(Profile.this, MainActivity.class);
                startActivity(SendIntent);
                Toast.makeText(Profile.this, "Account deleted!", Toast.LENGTH_LONG).show();
            }
        });
    }
}