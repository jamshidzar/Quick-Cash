package com.example.registration;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FavoriteJobsActivity extends AppCompatActivity {

    private EditText jobTitleInput, jobNameInput, salaryInput, locationInput;
    private Button confirmButton;
    private FirebaseFirestore firestore;
    private String userId = "sampleUserId"; // Replace this with the actual logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_jobs);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        jobTitleInput = findViewById(R.id.jobTitleInput);
        jobNameInput = findViewById(R.id.jobNameInput);
        salaryInput = findViewById(R.id.salaryInput);
        locationInput = findViewById(R.id.locationInput);
        confirmButton = findViewById(R.id.confirmButton);

        // Handle confirm button click
        confirmButton.setOnClickListener(v -> {
            String jobTitle = jobTitleInput.getText().toString().trim();
            String jobName = jobNameInput.getText().toString().trim();
            String salary = salaryInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();

            // Validate inputs
            if (jobTitle.isEmpty() || jobName.isEmpty() || salary.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save the job to Firestore
                savePreferredJobToFirestore(jobTitle, jobName, salary, location);
            }
        });
    }

    private void savePreferredJobToFirestore(String jobTitle, String jobName, String salary, String location) {
        // Create a new preferred job entry
        Map<String, Object> preferredJob = new HashMap<>();
        preferredJob.put("userId", userId);
        preferredJob.put("jobTitle", jobTitle);
        preferredJob.put("jobName", jobName);
        preferredJob.put("salary", salary);
        preferredJob.put("location", location);

        // Save to the "preferredJobs" collection
        firestore.collection("preferredJobs")
                .add(preferredJob)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Preferred job saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save preferred job: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
