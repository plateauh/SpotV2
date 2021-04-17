package com.example.spotv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class updateCurrentLocations extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        database DB = new database(context);
        int groupId = 0;
        double current_user_lat = 0.0;
        double current_user_lng = 0.0;
        Cursor users = DB.getUsersInGroup(groupId);

        try{
            if (users.getCount() > 0) {
                while(users.moveToNext()) {
                    int index_username = users.getColumnIndexOrThrow("username");
                    int index_lat = users.getColumnIndexOrThrow("currentLat");
                    int index_lng = users.getColumnIndexOrThrow("currentLng");
                    String username = users.getString(index_username);
                    double lat = users.getDouble(index_lat);
                    double lng = users.getDouble(index_lng);

                    float[] results = new float[1];
                    Location.distanceBetween(current_user_lat, current_user_lng,
                            lat, lng, results);

                    if(results[0] > 500){
                        longDistanceNotification(context, username);
                    }
                }
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