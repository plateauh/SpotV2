package com.example.spotv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
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
//        setView();
        Button create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup(v);
            }
        });
    }
    protected void setView(){
        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Create Group");

    }


    public void createGroup(View v){
        EditText editText=findViewById(R.id.NameGroupInput);
        String GroupName=editText.getText().toString();

        String currentUser= "Nouf Ali";
        String[] members = {currentUser};
        boolean iscreated = db.createGroup(GroupName,members);
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;
        if(iscreated){
            //go to home page

            CharSequence text = GroupName+" is created!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent i = new Intent(this,HomePage2Activity.class);
            this.startActivity(i);
        }else{
            CharSequence text = "try again!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }
}