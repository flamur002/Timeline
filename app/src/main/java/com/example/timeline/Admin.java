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
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayList<String> usersId = new ArrayList<>();
    ListView listView3;
    LinearLayout layout3;
    DBConnect db = new DBConnect(this);
    Button signOut, export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listView3 = findViewById(R.id.listView3);
        layout3 = findViewById(R.id.layout3);
        signOut = findViewById(R.id.signOutBtn);
        export = findViewById(R.id.exportBtn);

        //reading all registered users from the database
        users = db.readUsers();

        ////getting the id from all registered userd
        usersId = db.readUsersId();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listView3.setAdapter(adapter);


        //click listener for the list of users (delete user)
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //deleting the users comments first
                db.deleteUserComments(usersId.get(i));

                //deleting the users account
                db.deleteUser(usersId.get(i));

                //toast to notify the user
                Toast.makeText(Admin.this, "User deleted!", Toast.LENGTH_LONG).show();

                //refresh activity
                startActivity(new Intent(Admin.this, Admin.class));
            }
        });

        //click listener for sign out button
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //redirect the user to the log in activity
                startActivity(new Intent(Admin.this, MainActivity.class));
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FileOutputStream write = null;

                //writing on the file, so all the previous contents get removed
                try {
                    write = openFileOutput("UsersList.txt", MODE_PRIVATE);
                    write.write("All Users: \n \n \n".getBytes());
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }


                //appending all current user details to the UsersList.txt file
                for (int i = 0; i< users.size(); i++){
                    FileOutputStream write2 = null;
                    try {
                        String user = users.get(i)+"\n \n";
                        write2 = openFileOutput("UsersList.txt", MODE_APPEND);
                        write2.write(user.getBytes());
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
                Toast.makeText(Admin.this, "All user details exported to file 'UsersList.txt'", Toast.LENGTH_LONG).show();
            }
        });



    }
}