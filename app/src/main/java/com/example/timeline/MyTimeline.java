package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTimeline extends AppCompatActivity {

    LinearLayout layout2;
    ListView listView2;
    ArrayList<String> myComments = new ArrayList<>();
    ArrayList<String> myCommentsId = new ArrayList<>();
    TextView backButton;

    DBConnect db = new DBConnect(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_timeline);

        layout2 = findViewById(R.id.layout2);
        listView2 = findViewById(R.id.listView2);
        backButton = findViewById(R.id.button2);


        //receiving all the variables passed by intent
        Intent getAllIntent = getIntent();
        String getCurrentIntent = getAllIntent.getStringExtra("userId");

        //getting all the comments the current user has posted, using the id
        myComments = db.readMyComments(getCurrentIntent);
        //getting the id's of every comment
        myCommentsId = db.readMyCommentsId(getCurrentIntent);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myComments);
        //adding the comments to the listview
        listView2.setAdapter(adapter);

        //list view click listener
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //opening the editcomment.java activity
                Intent SendIntent = new Intent(MyTimeline.this, EditComment.class);
                //passing the comment id
                SendIntent.putExtra("commentId", myCommentsId.get(i));
                SendIntent.putExtra("userId", getCurrentIntent);
                startActivity(SendIntent);
            }
        });

        //click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //going back to the general timeline activity
                Intent SendIntent = new Intent(MyTimeline.this, GeneralTimeline.class);
                SendIntent.putExtra("userId", getCurrentIntent);
                startActivity(SendIntent);
            }
        });
    }
}