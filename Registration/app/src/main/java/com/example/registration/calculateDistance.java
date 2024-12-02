package com.example.registration;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class calculateDistance {
    private double latitude;
    private double longitude;
    private Context context;
    private String userCity;
    private List<Job> jobsList;
    private static String TAG = "calculateDistance";

    public calculateDistance(double latitude, double longitude, Context context) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    public calculateDistance(double latitude, double longitude, String userCity) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userCity = userCity;
    }

    public calculateDistance() {
    }

    protected void setUserCity(){
        //use latitude and longitude to store city name in which user is located
        // Initialize Geocoder
        Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 100);
            if (addresses != null && !addresses.isEmpty()) {
                for(Address address : addresses){
                    userCity = address.getLocality();
                    Log.d(TAG, "setUserCity: user is in "+ userCity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserCity() {
        return userCity;
    }

    public List<Job> jobList(){
        Log.d(TAG, "jobList: job list called");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("job")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocuments) {
                        for (DocumentSnapshot document : queryDocuments) {
                            String duration = document.getString("duration");
                            String employerID = document.getString("employerID");
                            String jobName = document.getString("jobName");
                            String location = document.getString("location");
                            String postalCode = document.getString("postalCode");
                            String salary =  document.getString("salary");
                            String urgency = document.getString("urgency");
                            Job currentJob = new Job(jobName, employerID, location, duration, salary, urgency, postalCode);
                            jobsList.add(currentJob);
                        }
                        if(!jobsList.isEmpty()){
                            Log.d(TAG, "onSuccess: jobs list is not empty");
                        } else {
                            Log.d(TAG, "onSuccess: jobs list is empty");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.d(TAG, "onFailure: could not access the document");
                    }
                });
        return this.jobsList;
    }
    public boolean compareLocations(){
        for(Job job : jobsList){
            if(job.getLocation().equalsIgnoreCase(userCity)){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}
