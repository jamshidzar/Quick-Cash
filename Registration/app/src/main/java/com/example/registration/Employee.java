package com.example.registration;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Employee extends AppCompatActivity {
    private Button jobApplyingButton;
    private Button mapButton; // Add this line
    private String userId; // To store the user ID received from SharedPreferences

    private static final int APPLY_JOB_REQUEST = 1;


// Code review by Jamshid Zar:
// Overall, the onCreate method is well-structured, and the Firebase integration is solid.
// A few suggestions for improvement:
// - Consider adding null checks for the Intent to prevent potential crashes.
// - Be cautious about displaying sensitive information such as passwords and credit card details in the UI.
// - Ensure all user data fields are properly null-checked before using them to avoid null pointer exceptions.
// - The error message in the else block references "name" when it should reference "email" as that is being checked.
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

        Button mapButton = findViewById(R.id.MapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employee.this, CurrentLocation.class);
                startActivity(intent);
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

}


