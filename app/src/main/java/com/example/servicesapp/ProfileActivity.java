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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private double lat;
    private double lon;
    private String[] locations = {"Haram", "Faisal", "Dokki", "Agouza"
            , "Bulaq ad Dakrur", "Imbabah", "Omrania", "Monib", "Kafr Tuhurmus"};
    private Uri imageUri;
    private ImageView profileIV;
    private EditText nameET;
    private TextView emailTV;
    private EditText aboutET;
    private EditText phoneET;
    private EditText hourlyRateET;
    private ImageView saveIV;
    private ProgressBar progressBar;
    private User user;
    private FirebaseFirestore db;
    private String selectedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        profileIV = findViewById(R.id.iv_profile);
        nameET = findViewById(R.id.et_name);
        emailTV = findViewById(R.id.tv_email);
        aboutET = findViewById(R.id.et_about);
        phoneET = findViewById(R.id.et_phone);
        hourlyRateET = findViewById(R.id.et_hourly_rate);
        saveIV = findViewById(R.id.iv_save);
        progressBar = findViewById(R.id.pb);

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
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = locations[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db = FirebaseFirestore.getInstance();
        db
                .collection("users")
                .document(Finals.user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        setTitle(user.getUserType() + " Profile");
                        if (user.getName() != null)
                            nameET.setText(user.getName());
                        if (user.getAbout() != null)
                            aboutET.setText(user.getAbout());
                        if (user.getPhone() != null)
                            phoneET.setText(user.getPhone());
                        if (user.getHourlyRate() != null)
                            hourlyRateET.setText(user.getHourlyRate());
                        if (user.getLocation() != null)
                            locationSpinner.setSelection(adapter.getPosition(user.getLocation()));
                        if (user.getPicture() != null)
                            Picasso
                                    .get()
                                    .load(user.getPicture())
                                    .into(profileIV);
                        emailTV.setText(user.getEmail());
                    }
                });

    }

    public void choosePP(View view) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageUri = data.getData();
            profileIV.setImageURI(imageUri);
        }
    }


    public void saveProfile(View view) {
        progressBar.setVisibility(View.VISIBLE);
        saveIV.setVisibility(View.INVISIBLE);
        if (imageUri != null)
            uploadImage();
        else
            getLocation(imageUri); //Image Uri in this case is null
    }

    private void uploadImage() {
        //Accessing Cloud Storage bucket by creating an instance of FirebaseStorage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //Create a reference to upload, download, or delete a file

        Calendar now = Calendar.getInstance();
        int y = now.get(Calendar.YEAR);
        int m = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int d = now.get(Calendar.DAY_OF_MONTH);
        int h = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int s = now.get(Calendar.SECOND);
        String imageName = "image: " + d + '-' + m + '-' + y + ' ' + h + ':' + min + ':' + s;

        StorageReference storageRef = storage.getReference().child(imageName);
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    getLinkForUploadedImage(storageRef.getDownloadUrl());
                });
    }

    private void getLinkForUploadedImage(Task<Uri> task) {
        task.addOnSuccessListener(uri -> {
            getLocation(uri);
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation(Uri imageUri) {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("trace", "Location: " + location.getLatitude()
                                    + ", " + location.getLongitude());
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            uploadProfile(imageUri);
                        } else
                            Toast.makeText(ProfileActivity.this, "No Found Location"
                                    , Toast.LENGTH_SHORT).show();

                    }
                });
    }

    //TODO: Get user lat lon, while uploading profile data by calling getLocation Method
    //TODO: show user his email before uploading anything
    //TODO:upload employee type to firebase & his email & chosen place from spinner
    private void uploadProfile(Uri imageUri) {
        String name = nameET.getText().toString();
        String about = aboutET.getText().toString();
        String phone = phoneET.getText().toString();
        String hourlyRate = hourlyRateET.getText().toString();

        String imageLink;
        if (imageUri != null)
            imageLink = imageUri.toString();
        else
            imageLink = user.getPicture();

        user = new User(user.getEmail(), user.getUserType(), imageLink, name, about, phone,
                hourlyRate, selectedLocation, lat, lon);

        db
                .collection("users")
                .document(Finals.user)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        saveIV.setVisibility(View.VISIBLE);
                        Toast.makeText(ProfileActivity.this, "Profile Updated"
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }


}