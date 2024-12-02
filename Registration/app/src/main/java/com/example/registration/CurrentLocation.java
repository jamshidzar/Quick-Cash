package com.example.registration;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;


public class CurrentLocation extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker marker;
    private FirebaseFirestore db;

    private EditText locationInput;
    private Button searchButton;

    // Flag to track if manual search is being performed
    private boolean isManualSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize EditText and Button for manual location entry
        locationInput = findViewById(R.id.locationInput);
        searchButton = findViewById(R.id.searchButton);

        // Set up button click listener to search for a location manually
        searchButton.setOnClickListener(v -> {
            String location = locationInput.getText().toString();
            if (!location.isEmpty()) {
                isManualSearch = true;  // Set flag to indicate manual search
                performManualLocationSearch(location);
            } else {
                Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize the map fragment and set up location manager
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return; // Exit if permissions are not granted
        }

        // Set up location listener for automatic location updates
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (!isManualSearch) {  // Only update location if not in manual search mode
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    updateLocationOnMap(latitude, longitude);
                    saveLastLocation(latitude, longitude);
                }
            }
        };
        // Request location updates from both network and GPS providers
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    // Method to update the map with the given latitude and longitude
    private void updateLocationOnMap(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getLocality() + ": " + addresses.get(0).getCountryName();

            LatLng latLng = new LatLng(latitude, longitude);
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(address));
            mMap.setMaxZoomPreference(15);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle manual location search
    private void performManualLocationSearch(String location) {
        try {
            List<Address> addressList = new Geocoder(this).getFromLocationName(location, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                mMap.setMaxZoomPreference(15);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                // Set flag to indicate that a manual search has been performed
                isManualSearch = true;
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error fetching location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // You can optionally set the default map position here if needed
    }
    protected void saveLastLocation(double latitude, double longitude){
        //create a new field in the db
        SharedPreferences sh = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = sh.getString("userId", "");
        Log.d("UserId", "User ID: " + userId);
        db = FirebaseFirestore.getInstance();
        db.collection("user")  // Replace with your collection name
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get document reference
                                if(document.getId().equals(userId)){
                                    DocumentReference userRef = document.getReference();
                                    Log.d("UserRef", "User Document Reference: " + userRef.getPath());
                                    // Do something with userRef
                                    userRef.update("latitude", latitude);
                                    Log.d("UserRef", "onComplete: latitude updated");
                                    userRef.update("longitude", longitude);
                                    Log.d("UserRef", "onComplete: longitude updated");
                                    break;
                                }
                                else{
                                    Log.d("UserRef", "No matching document ID found for userId: " + userId);
                                }
                            }
                        } else {
                            Log.d("UserRef", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
