package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditComment extends AppCompatActivity {

    EditText edcComment;
    Button edcUpdate, edcDelete;
    DBConnect db = new DBConnect(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);

        edcComment = findViewById(R.id.edcComment);
        edcUpdate = findViewById(R.id.edcUpdate);
        edcDelete = findViewById(R.id.edcDelete);

        //receiving variables passed thorough intent
        Intent getAllIntent = getIntent();
        String getCurrentIntent = getAllIntent.getStringExtra("commentId");
        String usersId = getAllIntent.getStringExtra("userId");

        //finding the comment using the id passed by intent
        String content = db.getComment(Integer.valueOf(getCurrentIntent));
        edcComment.setText(content);


        //click listener for delete button
        edcDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete the comment using its id
                db.deleteComment(Integer.valueOf(getCurrentIntent));
                //going to the mytimeline activity
                Intent SendIntent = new Intent(EditComment.this, MyTimeline.class);
                Toast.makeText(EditComment.this, "Comment deleted!", Toast.LENGTH_LONG).show();
                //passing the variables as an intent
                SendIntent.putExtra("commentId", getCurrentIntent);
                SendIntent.putExtra("userId", usersId);
                startActivity(SendIntent);
            }
        });

        //click listener for update button
        edcUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newComment = edcComment.getText().toString();
                if(!newComment.isEmpty()) { //checking if the text entered is empty
                    //creating a comment object
                    Comments updatedComment = new Comments(newComment, Integer.valueOf(getCurrentIntent));
                    //calling database function to update comment
                    boolean result = db.updateComment(updatedComment);
                    if (result){
                        Toast.makeText(EditComment.this, "Comment updated!", Toast.LENGTH_LONG).show();
                        //going to the mytimeline activity
                        Intent SendIntent = new Intent(EditComment.this, MyTimeline.class);
                        //passing the variables as an intent
                        SendIntent.putExtra("commentId", getCurrentIntent);
                        SendIntent.putExtra("userId", usersId);
                        startActivity(SendIntent);
                    }
                    else{
                        Toast.makeText(EditComment.this, "Comment could not be updated!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    //giving an error message if the new comment is empty/blank
                    Toast.makeText(EditComment.this, "New comment cannot be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}