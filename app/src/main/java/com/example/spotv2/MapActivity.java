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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isGhostMode = sharedPreferences.getBoolean("isGhostMode", true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        context = this;
        repeateAlarm();

        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = LocationManager.GPS_PROVIDER;
        getLocationPermission();

    }

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

        // Add a marker in your device location and move the camera
        LatLng devLoc = new LatLng(lat, lng);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
         //DB.insertUser("Thurya","123",false,context);
        if(DB.updateUserLocation("Thurya",24.686460, 46.840670)){
            Cursor cursor = DB.getUser("Thurya");
            int index;
            cursor.moveToFirst();
                index = cursor.getColumnIndexOrThrow("currentLat");
                System.out.println("index"+index);
                double lat = cursor.getDouble(index);
                index = cursor.getColumnIndexOrThrow("currentLng");
                System.out.println("index2"+index);
                double lng = cursor.getDouble(index);

            LatLng Thurya = new LatLng(lat, lng);
            float color = 210;
            mMap.addMarker(new MarkerOptions()
                    .position(Thurya).icon(BitmapDescriptorFactory.defaultMarker(color))
                    .title("Thurya is here "));
        }
        DB.insertUser("Fay","123",false,context);
        if(DB.updateUserLocation("Fay",24.687966, 46.840708)){
            Cursor cursor = DB.getUser("Fay");
            int index;
            cursor.moveToFirst();
            index = cursor.getColumnIndexOrThrow("currentLat");
            System.out.println("index"+index);
            double lat = cursor.getDouble(index);
            index = cursor.getColumnIndexOrThrow("currentLng");
            System.out.println("index2"+index);
            double lng = cursor.getDouble(index);

            LatLng Fay = new LatLng(lat, lng);
            float color5 = 180;
            mMap.addMarker(new MarkerOptions()
                    .position(Fay).icon(BitmapDescriptorFactory.defaultMarker(color5))
                    .title("Fay is here "));
        }
        DB.insertUser("Nouf","123",false,context);
        if(DB.updateUserLocation("Nouf",24.690178, 46.840063)){
            Cursor cursor = DB.getUser("Nouf");
            int index;
            cursor.moveToFirst();
            index = cursor.getColumnIndexOrThrow("currentLat");
            System.out.println("index"+index);
            double lat = cursor.getDouble(index);
            index = cursor.getColumnIndexOrThrow("currentLng");
            System.out.println("index2"+index);
            double lng = cursor.getDouble(index);

            LatLng Nouf = new LatLng(lat, lng);
            float color2 = 120;
            mMap.addMarker(new MarkerOptions()
                    .position(Nouf).icon(BitmapDescriptorFactory.defaultMarker(color2))
                    .title("Nouf is here "));
        }
        DB.insertUser("Latifah","123",false,context);
        if(DB.updateUserLocation("Latifah",24.690918, 46.841349)){
            Cursor cursor = DB.getUser("Latifah");
            int index;
            cursor.moveToFirst();
            index = cursor.getColumnIndexOrThrow("currentLat");
            System.out.println("index"+index);
            double lat = cursor.getDouble(index);
            index = cursor.getColumnIndexOrThrow("currentLng");
            System.out.println("index2"+index);
            double lng = cursor.getDouble(index);

            LatLng Latifah = new LatLng(lat, lng);
            float color3 = 260;
            mMap.addMarker(new MarkerOptions()
                    .position(Latifah).icon(BitmapDescriptorFactory.defaultMarker(color3))
                    .title("Latifah is here "));
        }
        DB.insertUser("Najd","123",false,context);
        if(DB.updateUserLocation("Najd",24.6908935, 46.8410352)){
            Cursor cursor = DB.getUser("Najd");
            int index;
            cursor.moveToFirst();
            index = cursor.getColumnIndexOrThrow("currentLat");
            System.out.println("index"+index);
            double lat = cursor.getDouble(index);
            index = cursor.getColumnIndexOrThrow("currentLng");
            System.out.println("index2"+index);
            double lng = cursor.getDouble(index);

            LatLng Najd = new LatLng(lat, lng);
            float color4 = 240;
            mMap.addMarker(new MarkerOptions()
                    .position(Najd).icon(BitmapDescriptorFactory.defaultMarker(color4))
                    .title("Najd is here "));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(devLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);


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

    private void repeateAlarm() {

        AlarmManager am=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, updateCurrentLocations.class);
        final PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 60, pi);
    }


}