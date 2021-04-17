package com.example.spotv2;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class createGroupForm extends AppCompatActivity {

    database db = new database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_form);
        setView();
    }
    protected void setView(){
        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Create Group");
    }


    public void createGroup(){
        EditText editText=findViewById(R.id.NameGroupInput);
        String GroupName=editText.getText().toString();

        String currentUser= "Nouf Ali";
        String[] members = {currentUser};
        boolean iscreated = db.createGroup(GroupName,members);
        if(iscreated){
            //go to home page
        }else{
            Context context = getApplicationContext();
            CharSequence text = GroupName+" is created!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }
}