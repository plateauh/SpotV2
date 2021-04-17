package com.example.spotv2;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeUsernameActivity extends AppCompatActivity {

    TextView username;
    EditText newUsername;
    Button cancelUserNameButton;
    Button changeUsernameButton;
    AlertDialog.Builder saveChangesDialog;
    AlertDialog.Builder cancelChangesDialog;
    database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.Username);
        newUsername = findViewById(R.id.newUsername);
        cancelUserNameButton = findViewById(R.id.cancelUserNameButton);
        changeUsernameButton = findViewById(R.id.changeUsernameButton);
        DB = DB.getInstance(this);

        username.setText("najd");
        DialogsBuilder();

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveChangesDialog.show();
            }
        });

        cancelUserNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelChangesDialog.show();

            }
        });

    }

    public void DialogsBuilder() {

        //savechanges dialog
        saveChangesDialog =  new AlertDialog.Builder(this)
                .setTitle("Save Changes")
                .setMessage("Are you sure you want to change your username?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String currentUsername = username.getText().toString();
                        String updatedUserName = newUsername.getText().toString();// the one entered by the user

                        boolean isDataUpdated = DB.updateUserName(currentUsername , updatedUserName);
                        if(isDataUpdated) {
                            Toast.makeText(ChangeUsernameActivity.this, "Username Updated", Toast.LENGTH_SHORT).show();
                            finish();                        }else {
                            Toast.makeText(ChangeUsernameActivity.this, "Oops, there is an error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null);


        //cancle dialog
        cancelChangesDialog =  new AlertDialog.Builder(this)
                .setTitle("Cancel Changes")
                .setMessage("Are you sure you want to Cancel your changes?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("no", null);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}