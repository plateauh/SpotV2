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

public class SignUpActivity extends AppCompatActivity {

    database DB;

    TextView toLogin;
    EditText usernameEdit, emailEdit, passwordEdit;
    Button signupBtn;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedpreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        DB = new database(this);



        usernameEdit = findViewById(R.id.username);
        emailEdit = findViewById(R.id.EmailAddress);
        passwordEdit = findViewById(R.id.Password);

        signupBtn = findViewById(R.id.signupbtn);
        signupBtn.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString();
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            if (isValidUsername(username) && isValidEmail(email) && isValidPassword(password)){
                boolean isRegistered = DB.insertUser(username, password, false, this);
                if (isRegistered){
                    insertSharedPrefs(username, password);
                    Toast.makeText(this, "Congratulations! You're now a member of Spot!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomePage2Activity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Oops, there is an error. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toLogin = findViewById(R.id.toLogin);
        toLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }
    protected boolean isValidUsername(String username){
        if (username.isEmpty()) {
            usernameEdit.requestFocus();
            usernameEdit.setError("Come on! You have to have a user name at least!");
            return false;
        }
        return true;
    }
    protected boolean isValidEmail(String email){
        if (email.isEmpty()) {
            emailEdit.requestFocus();
            emailEdit.setError("We swear we're not going to sell your email to a third-party!");
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            emailEdit.requestFocus();
            emailEdit.setError("Are sure this is a valid email?");
            return false;
        }
        return true;
    }

    protected boolean isValidPassword(String password){
        if (password.isEmpty()) {
            passwordEdit.requestFocus();
            passwordEdit.setError("Are you serious? a password is a must!");
            return false;
        }

        if (password.length() < 8) {
            passwordEdit.requestFocus();
            passwordEdit.setError("Your password is too short, try to make a longer one");
            return false;
        }

        if (password.length() > 15) {
            passwordEdit.requestFocus();
            passwordEdit.setError("We can't handle such a long password! make a shorter one");
            return false;
        }

        if (!password.matches(".*\\d.*") || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*")
                || !password.matches(".*[!@#$%^&*+=?-].*")) {
            passwordEdit.requestFocus();
            passwordEdit.setError("Your password must contain an uppercase, lowercase, digit and a special character");
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