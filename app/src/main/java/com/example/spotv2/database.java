package com.example.spotv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

//source code from :
//https://www.allcodingtutorials.com/post/insert-delete-update-and-view-data-in-sqlite-database-android-studio

public class database extends SQLiteOpenHelper {
    public database( Context context ) {
        super(context, "Spot.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(username TEXT primary key, password VARCHAR(8),ghostMood BOOLEAN, profileImg BLOB, currentLat DOUBLE, currentLng DOUBLE)");
        db.execSQL("create Table groups(groupId integer primary key autoincrement,groupName TEXT)");
        // FOREIGN KEY(username) REFERENCES users(username) on update cascade
        // FOREIGN KEY(username) REFERENCES users(username) on delete cascade on update cascade
        db.execSQL("create Table userGroups(username TEXT, groupId integer, " +
                "FOREIGN KEY(username) REFERENCES users(username) on delete cascade on update cascade, " +
                "FOREIGN KEY(groupId) REFERENCES groups(groupId) on delete cascade on update cascade, " +
                "PRIMARY KEY(username, groupId))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists users");
        db.execSQL("drop Table if exists groups");
        db.execSQL("drop Table if exists userGroups");

    }

    public Boolean updateUserLocation(String username, double currentLat, double currentLng)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentLat", currentLat);
        contentValues.put("currentLng", currentLng);
        Cursor cursor = DB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = DB.update("users", contentValues, "username =?", new String[]{username});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean insertUser(String username, String password , boolean ghostMood, Context context)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Bitmap profileImg = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.profile_img);
      byte[] blobImg = DbBitmapUtility.getBytes(profileImg);
        if(blobImg == null){
            Log.i("itisnulll","null");
        }
        contentValues.put("profileImg", blobImg);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("ghostMood", ghostMood);

        long result=DB.insert("users", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }


    public Boolean insertGroup(String groupName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupName", groupName);
        long result=DB.insert("groups", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

  
    public Boolean insertUserGroup(int groupId, String username)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupId", groupId);
        contentValues.put("username", username);

        long result=DB.insert("userGroups", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    //to test
    public  Boolean updateUserName(String username, String newUsername)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", newUsername);
        Cursor cursor = DB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = DB.update("users", contentValues, "username =?", new String[]{username});
            if (result == -1) {
                return false;
            } else {
                updateUserGroups(username, newUsername);
                return true;
            }
        } else {
            return false;
        }
    }

    public  Boolean updateUserImg(byte[] profileImg, String username)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("profileImg", profileImg);
        Cursor cursor = DB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = DB.update("users", contentValues, "username =?", new String[]{username});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    //to test
    public  Boolean updatePass(String username, String newPass)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPass);
        Cursor cursor = DB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = DB.update("users", contentValues, "username =?", new String[]{username});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }



    public Boolean deleteUser (String username) {

        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            long result = DB.delete("users", "username=?", new String[]{username});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public Boolean deleteGroup (int groupId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from groups where groupId = ?", new String []{groupId+""});
        if (cursor.getCount() > 0) {
            long result = DB.delete("groups", "groupId=?", new String[]{groupId+""});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public Boolean deleteUserGroup (String username, int groupId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from userGroups where username = ? AND groupId =?", new String[]{username, groupId+""});
        if (cursor.getCount() > 0) {
            long result = DB.delete("userGroups", "username=? AND groupId =?", new String[]{username, groupId+""});
            if (result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    //to test
    public Cursor getUser (String username) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String query = "SELECT * FROM users WHERE username = '"+username+"'";
        Cursor cursor = DB.rawQuery(query, null);
        return cursor;
    }
    //to test
    public Cursor getGroup (int groupId) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String query = "SELECT * FROM groups WHERE groupId = "+groupId;
        Cursor cursor = DB.rawQuery(query, null);
        return cursor;
    }


    public Cursor getUserGroups (String username) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String query = "SELECT * FROM groups LEFT JOIN userGroups ON userGroups.groupId = groups.groupId WHERE userGroups.username ='"+username+"'";
        Cursor cursor = DB.rawQuery(query, null);

        return cursor;
    }

    public Cursor getUsersInGroup (int groupId) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String query = "SELECT * FROM users LEFT JOIN userGroups ON userGroups.username = users.username WHERE userGroups.groupId = "+groupId;
        Cursor cursor = DB.rawQuery(query, null);

        return cursor;
    }

    public boolean createGroup(String groupName, String [] users){

        /* This method is responsible for create group, first it will insert a new row in groups table,
        then it will insert every user in the group int userGroup table with newly created group
        PS: we have userGroup table because the relationship between the users and groups is many to many
        */

        boolean isInserted = insertGroup(groupName);
        if(isInserted){
            //every user will inserted in userGroups table
            for(int i = 0; i<users.length; i++){
                int groupID = getLastGroupID();//to get the newly created group id
                insertUserGroup(groupID, users[i]);
            }

            return true;
        }else{

            return false;
        }

    }


    public int getLastGroupID(){

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String query = " SELECT * FROM groups WHERE groupId = (SELECT MAX(groupId) FROM groups)";
        Cursor cursor = DB.rawQuery(query, null);
        int groupID=0;

        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndexOrThrow("groupId");
            groupID = cursor.getInt(index);

        }
        return groupID;
    }

    public boolean login(String username, String password){
        Cursor cursor = getUser(username);
        if (cursor.moveToFirst() && cursor.getCount() >= 1){
            do {
                int index = cursor.getColumnIndexOrThrow("password");
                String actualPass = cursor.getString(index);
                if (password.equals(actualPass)) return true;
            } while(cursor.moveToNext());
        }
        return false;
    }

    public void updateUserGroups(String oldUser, String newUser){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", newUser);
        Cursor groups = getUserGroups(oldUser);
        int index;
        if (groups.moveToFirst() && groups.getCount() > 0){
            do {
                index = groups.getColumnIndexOrThrow("groupId");
                int groupId = groups.getInt(index);
                DB.update("userGroups", contentValues, "groupId =?", new String[]{""+groupId});
            } while (groups.moveToNext());
        }
    }
}