package com.example.spotv2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {



    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    GoogleMap mMap;
    LocationManager locationmanager;
    Location userLocation;
    String provider;
    double lat;
    double lng;

   //Latifah
    boolean isGhostMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        getLocationPermission();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    public void onMapReady(GoogleMap googleMap) {

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

        //Latifah
        mMap.setMyLocationEnabled(!isGhostMode);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng Thurya = new LatLng(24.686460, 46.840670);
        float color= 210;
        mMap.addMarker(new MarkerOptions()
                .position(Thurya).icon(BitmapDescriptorFactory.defaultMarker(color))
                .title("Thurya is here "));
        LatLng Fay = new LatLng(24.687966, 46.840708);
        float color5= 180;
        mMap.addMarker(new MarkerOptions()
                .position(Fay).icon(BitmapDescriptorFactory.defaultMarker(color5))
                .title("Fay is here "));

        LatLng Nouf = new LatLng(24.690178, 46.840063);
        float color2= 120;
        mMap.addMarker(new MarkerOptions()
                .position(Nouf).icon(BitmapDescriptorFactory.defaultMarker(color2))
                .title("Nouf is here "));

        LatLng Latifah = new LatLng(24.690918, 46.841349);
        float color3= 260;
        mMap.addMarker(new MarkerOptions()
                .position(Latifah).icon(BitmapDescriptorFactory.defaultMarker(color3))
                .title("Latifah is here "));

        LatLng Najd = new LatLng(24.6908935, 46.8410352);
        float color4= 240;
        mMap.addMarker(new MarkerOptions()
                .position(Najd).icon(BitmapDescriptorFactory.defaultMarker(color4))
                .title("Najd is here "));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(devLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}