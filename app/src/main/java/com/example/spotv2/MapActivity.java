package com.example.spotv2;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = "MapActivity";
    Context context;
    database DB;
    SharedPreferences sharedPreferences;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    int t = 5000; // milliseconds
    float distance = 1f; // meters
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    int groupID;

    private void initMap() {

        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }


    LocationManager locationmanager;
    Location userLocation;
    String provider;
    double lat;
    double lng;
    int groupID;
    boolean isGhostMode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
       isGhostMode= sharedPreferences.getBoolean("isGhostMode", false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        context = this;
        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID",0);
        Log.i("Map Class","GroupID: "+ groupID);


        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        getLocationPermission();


    }
    private Timer autoUpdate;


    public void onMapReady(GoogleMap googleMap) {
        DB = new database(context);
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        userLocation = locationmanager.getLastKnownLocation(provider);
        lat = userLocation.getLatitude();
        lng = userLocation.getLongitude();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("lat", (float) lat);
        editor.putFloat("lng", (float) lng);
        editor.commit();

        // Add a marker in your device location and move the camera
        LatLng devLoc = new LatLng(lat, lng);

        System.out.println("isGhostMode"+isGhostMode);
        if(!isGhostMode){
            System.out.println("isGhostMode"+isGhostMode);
        mMap.setMyLocationEnabled(true);}

        mMap.getUiSettings().setZoomControlsEnabled(true);

        String currentUser = sharedPreferences.getString("usernameKey", "");
        Cursor cursor = DB.getUsersInGroup(groupID);
            if (cursor.getCount() > 0) {
                double[] latArray = {24.688512, 24.688111, 24.687774, 24.688116};
                double[] longArray = {46.781582, 46.782028, 46.783301, 46.782032};
                int count = 0;
                while(cursor.moveToNext()) {
                    int index_username = cursor.getColumnIndexOrThrow("username");
                    String username = cursor.getString(index_username);
                    int lat1 = cursor.getColumnIndexOrThrow("currentLat");
                    double lat = cursor.getDouble(lat1);
                    int lng2 = cursor.getColumnIndexOrThrow("currentLng");
                    double lng = cursor.getDouble(lng2);
                    if(!currentUser.equals(username)){
                    if(DB.updateUserLocation(username,latArray[count], longArray[count++])){
                        autoUpdate = new Timer();
                        autoUpdate.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        LatLng user = new LatLng(lat, lng);
                                        float color = 210;
                                        mMap.addMarker(new MarkerOptions()
                                                .position(user).icon(BitmapDescriptorFactory.defaultMarker(color))
                                                .title(username+" is here "));
                                    }
                                });
                            }
                        }, 0, 4000); // updates each 4 secs
                    }
                }
                }
       }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(devLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        repeateAlarm(groupID);

    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                locationmanager.requestLocationUpdates(provider, 0, 0, myLocationListener);

                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    LocationListener myLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(TAG, "STOP MOVING!!!" + location.getLatitude());

            // save current user location
            DB = new database(context);
            double current_lat = location.getLatitude();
            double current_lng = location.getLongitude();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.i("plateau", lat+" "+lng);
            editor.putFloat("lat", (float) current_lat);
            editor.putFloat("lng", (float) current_lng);
            editor.commit();
            String username = sharedPreferences.getString("usernameKey", "");
            boolean isUpdated = DB.updateUserLocation(username, current_lat, current_lng);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

//    updateCurrentLocations.class

    private void repeateAlarm(int groupID) {

        AlarmManager am=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, updateCurrentLocations.class);
        i.putExtra("groupID",groupID);

        Log.i("repeat alarm", "GroupID: "+groupID);
      
        Log.i("info", "Group ID: "+groupID+" lat: "+lat+" lng: "+lng);
        final PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60, pi);
    }



}