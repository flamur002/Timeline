package com.example.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBConnect  extends SQLiteOpenHelper {

    private static String dbname = "Comments";
    private static int version = 1;

    private static String usersTable = "Users";
    private static String ID = "id";
    private static String name = "name";
    private static String surname = "surname";
    private static String username = "username";
    private static String password = "password";
    private static String dateRegistered = "date_registered";
    private static String dateUpdated = "date_updated";

    private static String commentsTable = "Comments";
    private static String commentsID = "id";
    private static String content = "content";
    private static String publisher = "publisher";
    private static String publishDate = "publish_date";

    public DBConnect(@Nullable Context context) {
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE "+usersTable+ " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+name+" TEXT, "+surname+" TEXT, "+username+" TEXT," +
                password+" TEXT, "+dateRegistered+" TEXT, "+dateUpdated+" TEXT)";

        sqLiteDatabase.execSQL(query);

        String query2 = "CREATE TABLE "+commentsTable+ " ("+commentsID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+content+" TEXT, "+publisher+" TEXT, "+publishDate+" TEXT)";
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+usersTable);
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+commentsTable);
        onCreate(sqLiteDatabase);
    }

    public boolean addUser(Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, user.getName());
        values.put(surname, user.getSurname());
        values.put(username, user.getUsername());
        values.put(password, user.getPassword());
        values.put(dateRegistered, getDate());
        values.put(dateUpdated, getDate());
        long result = db.insert(usersTable, null, values);
        if(result>0){
            return true;
        }
        else{
            return false;
        }

    }

    public boolean addComment(Comments comment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values2 = new ContentValues();
        values2.put(content, comment.getContent());
        values2.put(publisher, comment.getPublisher());
        values2.put(publishDate, getDate());
        long result = db.insert(commentsTable, null, values2);
        if(result>0){
            return true;
        }
        else{
            return false;
        }

    }

    public boolean updateUserDetails(Users user, String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, user.getName());
        values.put(surname, user.getSurname());
        values.put(username, user.getUsername());
        values.put(password, user.getPassword());
        values.put(dateUpdated, getDate());

        db.update(usersTable, values, " id =?",new String[]{user.getID()});
        return true;
    }

    public boolean updateComment(Comments comments){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(content, comments.getContent());

        db.update(commentsTable, values, " id =?",new String[]{String.valueOf(comments.getID())});
        return true;
    }

    public boolean deleteUser(String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(usersTable, " id =?", new String[]{userID});
        if(result>0){
            return true;
        }
        else{
            return false;
        }
    }

    public String getDate(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public boolean loginCheck(Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+usersTable+" WHERE "+username+" =? AND "+password+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{user.getUsername(),user.getPassword()});
        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean emailCheck(Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+usersTable+" WHERE "+username+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{user.getUsername()});
        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<String> readAllComments(){
        ArrayList<String> commentsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+commentsTable+" ORDER BY "+commentsID+" DESC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            String commentsContent = cursor.getString(cursor.getColumnIndexOrThrow(content));
            String commentPublisher = cursor.getString(cursor.getColumnIndexOrThrow(publisher));
            String commentDate = cursor.getString(cursor.getColumnIndexOrThrow(publishDate));

            commentsList.add(getName(Integer.valueOf(commentPublisher))+" ("+commentDate+"):\n"+commentsContent);
            cursor.moveToNext();
        }
        return commentsList;
    }

    public ArrayList<String> readMyComments(String publisherId){
        ArrayList<String> commentsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+commentsTable+" WHERE "+publisher+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{publisherId});
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            String commentsContent = cursor.getString(cursor.getColumnIndexOrThrow(content));
            String commentDate = cursor.getString(cursor.getColumnIndexOrThrow(publishDate));

            commentsList.add(commentsContent+"\n at: "+commentDate);
            cursor.moveToNext();
        }
        return commentsList;
    }

    public ArrayList<String> readMyCommentsId(String publisherId){
        ArrayList<String> commentsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+commentsTable+" WHERE "+publisher+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{publisherId});
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            String commentsId = cursor.getString(cursor.getColumnIndexOrThrow(commentsID));

            commentsList.add(commentsId);
            cursor.moveToNext();
        }
        return commentsList;
    }

    public int getId(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+usersTable+" WHERE "+username+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        cursor.moveToFirst();
        int currentId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
        return currentId;
    }

    public String getName(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+usersTable+" WHERE "+ID+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(name));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(surname));
        String fullname = firstName+" "+lastName;
        return fullname;
    }

    public ArrayList<String> getUserDetails (String id){
        ArrayList<String> userDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+usersTable+" WHERE "+ID+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{id});
        cursor.moveToFirst();
        userDetails.add(cursor.getString(cursor.getColumnIndexOrThrow(name)));
        userDetails.add(cursor.getString(cursor.getColumnIndexOrThrow(surname)));
        userDetails.add(cursor.getString(cursor.getColumnIndexOrThrow(username)));
        userDetails.add(cursor.getString(cursor.getColumnIndexOrThrow(password)));
        userDetails.add(cursor.getString(cursor.getColumnIndexOrThrow(dateUpdated)));
        return userDetails;
    }

    public String getComment(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+commentsTable+" WHERE "+commentsID+" =?";
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        String commContent = cursor.getString(cursor.getColumnIndexOrThrow(content));
        return commContent;
    }


    public boolean deleteComment(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(commentsTable, " id =?", new String[]{String.valueOf(id)});
        if(result>0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean deleteUserComments(String publisherId){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(commentsTable, " publisher =?", new String[]{publisherId});
        if(result>0){
            return true;
        }
        else{
            return false;
        }
    }

//    public ArrayList<String> readUsers(){
//        ArrayList<String> usersList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM "+usersTable;
//        Cursor cursor = db.rawQuery(query, null);
//        cursor.moveToFirst();
//        while (cursor.isAfterLast() == false){
////            String userName = cursor.getString(cursor.getColumnIndexOrThrow(name));
////            String userSurname = cursor.getString(cursor.getColumnIndexOrThrow(surname));
////            String userUsername = cursor.getString(cursor.getColumnIndexOrThrow(username));
////            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow(password));
////            String userDate = cursor.getString(cursor.getColumnIndexOrThrow(dateRegistered));
////            String userId = Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
//
//            usersList.add("userName");
//
////            usersList.add("Full Name: "+userName+" "+userSurname+"\n"
////            +"Log-In Details: "+userUsername+" "+userPassword+"\n"
////            +"ID: "+userId+"\n Member Since: "+userDate);
////            cursor.moveToNext();
//        }
//        return usersList;
//    }

    public ArrayList<String> readAllUsers(){
        ArrayList<String> commentsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+commentsTable+" ORDER BY "+commentsID+" DESC";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            String commentsContent = cursor.getString(cursor.getColumnIndexOrThrow(content));
            String commentPublisher = cursor.getString(cursor.getColumnIndexOrThrow(publisher));
            String commentDate = cursor.getString(cursor.getColumnIndexOrThrow(publishDate));

            commentsList.add(getName(Integer.valueOf(commentPublisher))+"\n \n"+commentsContent+"\n \n at "+commentDate+" \n");
            cursor.moveToNext();
        }
        return commentsList;
    }

    public ArrayList<String> readUsers(){
        ArrayList<String> allUsers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+usersTable;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            String userUserName = cursor.getString(cursor.getColumnIndexOrThrow(username));
            String userFirstName = cursor.getString(cursor.getColumnIndexOrThrow(name));
            String userLastName = cursor.getString(cursor.getColumnIndexOrThrow(surname));
            String userid = cursor.getString(cursor.getColumnIndexOrThrow(ID));
            String userDateRegistered = cursor.getString(cursor.getColumnIndexOrThrow(dateRegistered));

            allUsers.add("Full Name: "+ userFirstName+" "+userLastName+"\n"+"Username: "+userUserName+", UserID: "+userid+"\n"+"Member Since: "+userDateRegistered);

            cursor.moveToNext();
        }
        return allUsers;
    }

    public ArrayList<String> readUsersId(){
        ArrayList<String> allUsersId = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+usersTable;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            String usersid = cursor.getString(cursor.getColumnIndexOrThrow(ID));

            allUsersId.add(usersid);

            cursor.moveToNext();
        }
        return allUsersId;
    }




}