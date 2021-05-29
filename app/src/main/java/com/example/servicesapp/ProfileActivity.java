package com.example.servicesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private double lat;
    private double lon;
    private String[] locations = {"Haram", "Faisal", "Dokki", "Agouza"
            ,"Bulaq ad Dakrur", "Imbabah", "Omrania", "Monib", "Kafr Tuhurmus"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                        , 101);

        Spinner locationSpinner = findViewById(R.id.spinner_location);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

    }

    public void saveProfile(View view) {
    }

    public void choosePP(View view) {
    }


    //TODO: Get user lat lon, while uploading profile data
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("trace", "Location: " + location.getLatitude()
                                    + ", " + location.getLongitude());
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        } else
                            Toast.makeText(ProfileActivity.this, "No Found Location"
                                    , Toast.LENGTH_SHORT).show();

                    }
                });
    }

}