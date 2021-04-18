package com.example.spotv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InviteFormActivity extends AppCompatActivity {
    Intent intent;
    int groupId;
    database DB;
    Button invite;
    EditText usernameInput;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_form);
        intent = getIntent();
        groupId = intent.getIntExtra("groupID", 0);
        DB = new database(this);
        usernameInput = findViewById(R.id.usernameInput);
        invite = findViewById(R.id.inviteBtn);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                if(invite(username)){
                    Toast.makeText(context, username+" was added successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    usernameInput.requestFocus();
                    usernameInput.setError("There is no such user or the user is already joined");
                }
            }
        });
    }
    boolean invite(String username){
        Cursor userData = DB.getUser(username);
        if (userData.getCount()>0){
            return DB.insertUserGroup(groupId, username);
        }
        return false;
    }
}