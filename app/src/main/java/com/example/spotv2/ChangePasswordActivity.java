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
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPass;
    EditText newPass1;
    EditText newPass2;
    Button cancelPassButton;
    Button changePassButton;
    AlertDialog.Builder saveChangesDialog;
    AlertDialog.Builder cancelChangesDialog;
    database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.setTitle("change password");

        currentPass = findViewById(R.id.currentPass);
        newPass1 = findViewById(R.id.newPass1);
        newPass2 = findViewById(R.id.newPass2);
        cancelPassButton = findViewById(R.id.cancelPassButton);
        changePassButton = findViewById(R.id.changePassButton);
        DB = new database(this);

        dialogsBuilder();



        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateFields() && validateNewPassword()){

                    saveChangesDialog.show();
                }
            }
        });

        cancelPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelChangesDialog.show();
            }
        });

    }




    private boolean validateFields(){


        if( currentPass.getText().toString().length()==0){

            currentPass.requestFocus();
            currentPass.setError("this field can not be empty");
            return false;
        }
        if(newPass1.getText().toString().length()==0){

            newPass1.requestFocus();
            newPass1.setError("this field can not be empty");
            return false;
        }


        if(newPass2.getText().toString().length()==0){

            newPass2.requestFocus();
            newPass2.setError("this field can not be empty");
            return false;
        }

        if(!newPass1.getText().toString().equals(newPass2.getText().toString())){

            newPass2.requestFocus();
            newPass2.setError("the two given passwords do not match");
            return false;

        }


        return true;
    }
    private boolean validateNewPassword (){
        if (newPass2.getText().toString().length() < 8 || newPass2.getText().toString().length() >15 ){

            newPass2.requestFocus();
            newPass2.setError("Password should be between 8 and 15 character");
            return false;
        }

        if (!newPass2.getText().toString().matches(".*\\d.*")){

            newPass2.requestFocus();
            newPass2.setError("password should have at least one digit");
            return false;

        }

        if (!newPass2.getText().toString().matches(".*[A-Z].*")) {

            newPass2.requestFocus();
            newPass2.setError("password should have at least one capital letter");
            return false;

        }

        if (!newPass2.getText().toString().matches(".*[a-z].*")) {

            newPass2.requestFocus();
            newPass2.setError("password should have at least one small letter");
            return false;

        }
        if (!newPass2.getText().toString().matches(".*[!@#$%^&*+=?-].*")) {

            newPass2.requestFocus();
            newPass2.setError("password should have at least one capital special character");
            return false;

        }

        return true;
    }

    public void dialogsBuilder(){

        //savechanges dialog
        saveChangesDialog = new AlertDialog.Builder(this)
                .setTitle("Save Changes")
                .setMessage("Are you sure you want to change your username?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String username = "latifahAlomar4"; //get from db
                        String newPass = newPass2.getText().toString();// the one entered by the user

                        boolean isDataUpdated = DB.updatePass( username , newPass);
                        if (isDataUpdated) {
                            Toast.makeText(ChangePasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Oops, there is an error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null);


        //cancle dialog
        cancelChangesDialog = new AlertDialog.Builder(this)
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