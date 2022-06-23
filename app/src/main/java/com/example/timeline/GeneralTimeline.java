package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class GeneralTimeline extends AppCompatActivity {

    EditText commentEdt;
    Button publish, accountBtn, myTimelineBtn, logOut;
    DBConnect db = new DBConnect(this);
    ArrayList<String>  comments = new ArrayList<>();
    LinearLayout layout1;
    ListView listView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_timeline);

        //preventing the automatic pop-up of the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //getting the id of the current logged in user
        Intent getAllIntent = getIntent();
        String getCurrentIntent = getAllIntent.getStringExtra("userId");

        accountBtn = findViewById(R.id.accountBtn);
        myTimelineBtn = findViewById(R.id.myTimelineBtn);
        commentEdt = findViewById(R.id.commentEdt);
        publish = findViewById(R.id.publish);
        layout1 = findViewById(R.id.layout1);
        listView1 = findViewById(R.id.listView1);
        logOut = findViewById(R.id.logOutBtn);

        //calling database function to read all comments
        comments = db.readAllComments();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
        //adding all the comments to the listview
        listView1.setAdapter(adapter);

        //click listener for account button
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //going to the profile.java activity & passing the user id
                Intent SendIntent = new Intent(GeneralTimeline.this, Profile.class);
                SendIntent.putExtra("userId", getCurrentIntent);
                startActivity(SendIntent);
            }
        });

        //click listener for my timeline button
        myTimelineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //going to the mytimeline.java activity & passing the user id
                Intent SendIntent = new Intent(GeneralTimeline.this, MyTimeline.class);
                SendIntent.putExtra("userId", getCurrentIntent);
                startActivity(SendIntent);
            }
        });

        //click listener for the publish button
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentEdt.getText().toString().isEmpty()){ //getting the text of the edittext and checking if its empty
                    //creating a comment object
                    Comments c = new Comments(commentEdt.getText().toString(), getCurrentIntent);

                    //calling the add comment function from the database
                    boolean result = db.addComment(c);

                    if (result){
                        Toast.makeText(GeneralTimeline.this, "Comment uploaded!", Toast.LENGTH_LONG).show();
                        //refreshing the activity
                        Intent SendIntent = new Intent(GeneralTimeline.this, GeneralTimeline.class);
                        SendIntent.putExtra("userId", getCurrentIntent);
                        startActivity(SendIntent);
                    }
                    else{
                        Toast.makeText(GeneralTimeline.this, "Comment NOT uploaded!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //click listener for sign out button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //going to the log in page
                startActivity(new Intent(GeneralTimeline.this, MainActivity.class));
            }
        });
    }
}