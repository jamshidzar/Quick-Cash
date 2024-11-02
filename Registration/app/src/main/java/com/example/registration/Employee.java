package com.example.registration;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Employee extends AppCompatActivity {
    private Button jobApplyingButton;
    private FirebaseFirestore firestore;
    private String userId; // To store the user ID received from SharedPreferences

    private static final int APPLY_JOB_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getString("userId", null);

        Log.d("EmployeeActivity", "Retrieved userId from SharedPreferences: " + userId);

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Redirecting to login.", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(Employee.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize "Job Listing" button and set it to start JobListActivity
        jobApplyingButton = findViewById(R.id.jobApplyingButton);
        jobApplyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EmployeeActivity", "Passing userId to JobListActivity: " + userId);
                // Pass userId to JobListActivity
                Intent intent = new Intent(Employee.this, JobListActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, APPLY_JOB_REQUEST); // Start JobListActivity for job listing
            }
        });
    }

    // Handle the result of job application
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APPLY_JOB_REQUEST && resultCode == RESULT_OK) {
            Intent intent = new Intent(Employee.this, AppliedJobsActivity.class);
            intent.putExtra("userId", userId); // Pass userId to AppliedJobsActivity
            startActivity(intent);
        }
    }

    public void enableJobAlerts() {
        CheckBox checkBox = findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateNotificationPreference();
                    Toast.makeText(getApplicationContext(), "Job alerts enabled", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    // Method to update "Enable Notifications" to true
    public void updateNotificationPreference() {
        // Reference to the specific user document by userId
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            final String userId = currentUser.getUid();
            Log.d("UserId", "User ID: " + userId);
            DocumentReference userDocRef = firestore.collection("user").document(userId);
            // Update the "Enable Notifications" field to true
            userDocRef.update("enable notification", true)
                    .addOnSuccessListener(aVoid -> {
                        // Successfully updated document
                        Log.d("FirestoreHelper", "Notification preference updated successfully for userId: " + userId);
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors
                        Log.e("FirestoreHelper", "Error updating notification preference for userId: " + userId, e);
                    });
        } else {
            // Handle user not logged in (e.g., redirect to login screen)
            Log.d("UserId", "No user is currently signed in.");
        }

    }
}


