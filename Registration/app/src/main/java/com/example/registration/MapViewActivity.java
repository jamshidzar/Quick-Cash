package com.example.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        List<Job> jobList = (List<Job>) getIntent().getSerializableExtra("jobs");
        if (jobList != null && !jobList.isEmpty()) {
            Geocoder geocoder = new Geocoder(this);
            LatLng firstJobLocation = null; // Variable to hold the first valid location
            boolean firstLocationSet = false; // Flag to check if the first location is set

            for (Job job : jobList) {
                try {
                    // Check if the postal code is not null or empty
                    String postalCode = job.getPostalCode();
                    if (postalCode != null && !postalCode.isEmpty()) {
                        // Try to get the address from the postal code
                        List<Address> addresses = geocoder.getFromLocationName(postalCode, 1);

                        // Check if we got a valid address list
                        if (addresses != null && !addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            LatLng jobLocation = new LatLng(address.getLatitude(), address.getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(jobLocation).title(job.getJobName()));

                            // Set the first valid location for centering the camera
                            if (!firstLocationSet) {
                                firstJobLocation = jobLocation;
                                firstLocationSet = true;
                            }
                            // Mark that at least one valid location was found
                        } else {
                            Log.e("MapViewActivity", "No address found for postal code: " + postalCode);
                        }
                    } else {
                        Log.e("MapViewActivity", "Postal code is null or empty for job: " + job.getJobName());
                    }
                } catch (IOException e) {
                    Log.e("MapViewActivity", "Geocoding failed for postal code: " + job.getPostalCode(), e);
                } catch (Exception e) {
                    Log.e("MapViewActivity", "Unexpected error occurred while adding marker: ", e);
                }
            }

            // Center the camera on the first job's location if it was set
            if (firstJobLocation != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstJobLocation, 15)); // Adjust zoom level as needed
            } else {
                Toast.makeText(this, "No valid locations found for the jobs.", Toast.LENGTH_SHORT).show();
            }

            // Handle marker clicks to open the job details
            googleMap.setOnMarkerClickListener(marker -> {
                Log.d("MapViewActivity", "Marker clicked: " + marker.getTitle());
                for (Job job : jobList) {
                    if (Objects.equals(marker.getTitle(), job.getJobName())) { // Ensure job name uniquely identifies the job
                        Log.d("MapViewActivity", "Opening JobDetailActivity for job: " + job.getJobName());
                        openJobDetailActivity(job); // Open job detail when marker is clicked
                        break;
                    }
                }
                return true; // Indicate that the event is handled
            });
        } else {
            Toast.makeText(this, "No jobs available to display on the map.", Toast.LENGTH_SHORT).show();
        }
    }

        public void openJobDetailActivity(Job job) {
        Intent intent = new Intent(this, JobDetailActivity.class);
        intent.putExtra("jobName", job.getJobName());
        intent.putExtra("employerID", job.getEmployerID());
        intent.putExtra("location", job.getLocation());
        intent.putExtra("duration", job.getDuration());
        intent.putExtra("salary", job.getSalary());
        intent.putExtra("urgency", job.getUrgency());
        startActivity(intent);
    }
}


