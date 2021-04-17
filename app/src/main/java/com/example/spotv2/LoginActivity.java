package com.example.spotv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// Ref: https://www.tutorialspoint.com/android/android_session_management.htm
public class LoginActivity extends AppCompatActivity {
    database DB;

    TextView toSignup;
    EditText usernameEdit, passwordEdit;
    Button loginBtn;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DB = new database(this);

        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.Password);

        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        loginBtn = findViewById(R.id.loginbtn);
        loginBtn.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            if (isValidUsername(username) && isValidPassword(password)){
                boolean isValidCredentials = DB.login(username, password);
                if (isValidCredentials){
                    Toast.makeText(this, "Welcome back "+username+"!", Toast.LENGTH_SHORT).show();
                    insertSharedPrefs(username, password);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "An error occurred, make sure that is the right username and password combination.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toSignup = findViewById(R.id.toSignup);
        toSignup.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
    protected boolean isValidUsername(String username){
        if (username.isEmpty()) {
            usernameEdit.requestFocus();
            usernameEdit.setError("Who are you?");
            return false;
        }
        return true;
    }

    protected boolean isValidPassword(String password){
        if (password.isEmpty()) {
            passwordEdit.requestFocus();
            passwordEdit.setError("What's your password?");
            return false;
        }
        return true;
    }

    protected void insertSharedPrefs(String username, String password){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("usernameKey", username);
        editor.putString("passwordKey", password);
        editor.commit();
    }
}