package com.example.spotv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class updateCurrentLocations extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        database DB = new database(context);
        int groupId = intent.getIntExtra("groupID",0);

        double current_user_lat = sharedPreferences.getFloat("lat", 0);
        double current_user_lng = sharedPreferences.getFloat("lng", 0);
        Log.i("info inside broadcast", "Group ID: "+groupId+" lat: "+current_user_lat+" lng: "+current_user_lng);

        Cursor users = DB.getUsersInGroup(groupId);

        try{
            if (users.moveToFirst() && users.getCount() > 0) {
                do {
                    int index_username = users.getColumnIndexOrThrow("username");
                    int index_lat = users.getColumnIndexOrThrow("currentLat");
                    int index_lng = users.getColumnIndexOrThrow("currentLng");
                    String username = users.getString(index_username);
                    double lat = users.getDouble(index_lat);
                    double lng = users.getDouble(index_lng);

                    float[] results = new float[1];
                    Location.distanceBetween(current_user_lat, current_user_lng,
                            lat, lng, results);
                    Log.i("distance", ""+results[0]);

                    if(results[0] > 500){
                        longDistanceNotification(context, username);
                    }
                } while(users.moveToNext());
            }

        }finally{
            users.close();
        }


    }

    public void longDistanceNotification(Context context, String username){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "foo")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                .setContentTitle("Warning!")
                .setContentText(username + " is getting further than 500 meters!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());

    }
}