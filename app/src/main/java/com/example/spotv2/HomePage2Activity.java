package com.example.spotv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomePage2Activity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    database db = new database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        Intent intent = new Intent(this, createGroupForm.class);


        db.insertUser("Nouf Ali","123123",false,this);
        String[] users = {"Nouf Ali"};
//        db.createGroup("friends",users);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
 /*       DrawerLayout drawer = findViewById(R.id.drawer_layout);
      //  NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);*/
        // NavigationUI.setupWithNavController(navigationView, navController);


        Cursor result = db.getUserGroups("Nouf Ali");
        int[] groups = new int[]{};
        String[] groupName = new String[]{};

        int i = 0;
        if (result.moveToFirst() && result.getCount() >= 1){
            groupName= new String[result.getCount()];
            groups= new int[result.getCount()];
            do {
                int index;
                index = result.getColumnIndexOrThrow("groupId");
                int id = result.getInt(index);
                groups[i]=id;

                index = result.getColumnIndexOrThrow("groupName");
                String Name = result.getString(index);
                groupName[i++]=Name;

            } while (result.moveToNext());
        }
        result.close();


        RecyclerView groupList = (RecyclerView)findViewById(R.id.groupList);
        GroupHomeAdapter adapter = new GroupHomeAdapter(this,groups,groupName);
        groupList.setAdapter(adapter);
        groupList.setLayoutManager(new LinearLayoutManager(this));

        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

  /*  @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_logout: {
                logout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout(){
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}