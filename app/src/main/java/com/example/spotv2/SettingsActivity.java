package com.example.spotv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;

public class SettingsActivity extends AppCompatActivity {
//
    TableRow changPassRow;
    TableRow changeUsernameRow;
    Switch ghostModeToggle;
    ImageView profileImg;
    int SELECT_PICTURE = 200;
    SharedPreferences preferences;
    database DB;
    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
       Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //settings options
        changPassRow = findViewById(R.id.changPassRow);
        changeUsernameRow = findViewById(R.id.changeUsernameRow);
        ghostModeToggle = findViewById(R.id.ghostModeToggle);
        profileImg = findViewById(R.id.profile_img);
        DB = new database(this);
        preferences = getSharedPreferences(
                "MyPrefs", Context.MODE_PRIVATE);
        loggedInUser = preferences.getString("usernameKey","");
        setUserImage();


        //options activities
        Intent changePassIntent = new Intent(this, ChangePasswordActivity.class);
        Intent changeUsernameIntent = new Intent(this, ChangeUsernameActivity.class);

        //start activities onclick
        changPassRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(changePassIntent);
            }
        });

        changeUsernameRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(changeUsernameIntent);
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageChooser();
            }
        });

        boolean ghostM = preferences.getBoolean("isGhostMode",false);

        ghostModeToggle.setChecked(ghostM);

        ghostModeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    preferences.edit().putBoolean("isGhostMode",true).apply();
                }else{
                    preferences.edit().putBoolean("isGhostMode",false).apply();

                }
                Log.i("isGoste",""+preferences.getBoolean("isGhostMode",false));

            }


        });



    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    profileImg.setImageURI(selectedImageUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        byte[] img = DbBitmapUtility.getBytes(bitmap);
                        DB.updateUserImg(img, loggedInUser);
                        Toast.makeText(this,"profile Image updated successful", Toast.LENGTH_SHORT ).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public void setUserImage(){

        Cursor cursor = DB.getUser(loggedInUser);
        while (cursor.moveToNext()){
            int index;
            index = cursor.getColumnIndexOrThrow("profileImg");
            byte[] blob = cursor.getBlob(index);
            Bitmap bm = BitmapFactory.decodeByteArray(blob, 0, blob.length);
            profileImg.setImageBitmap(bm);
        }
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