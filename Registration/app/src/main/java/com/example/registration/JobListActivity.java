package com.example.registration;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
    public class JobListActivity extends AppCompatActivity implements JobAdapter.OnSaveToFavoritesListener, JobAdapter.OnApplyJobListener {
        private FirebaseFirestore firestore;
        private RecyclerView availableJobsRecyclerView;
        JobAdapter jobAdapter;
        List<Job> availableJobsList;
        private String userId; // To store the user ID passed from login

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_job_list);

            // Get userId from the intent (passed from login)

            userId = getIntent().getStringExtra("userId");
            Log.d("JobListActivity", "Received userId from Employee: " + userId);
            if (userId == null) {
                Toast.makeText(this, "User ID not found. Returning to login.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Initialize Firestore instance
            firestore = FirebaseFirestore.getInstance();

            // Setup RecyclerView and Adapter
            availableJobsList = new ArrayList<>();
            //jobAdapter = new JobAdapter(availableJobsList, this, this);
            availableJobsRecyclerView = findViewById(R.id.availableJobsRecyclerView);
            availableJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            availableJobsRecyclerView.setAdapter(jobAdapter);

//            // Load jobs from Firestore
//            loadJobsFromFirestore();

            findViewById(R.id.button3).setOnClickListener(v -> {
                Intent searchIntent = new Intent(JobListActivity.this, JobFilter.class);
                startActivity(searchIntent);
            });


        }






        public void onApplyJob(Job job) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> applicationData = new HashMap<>();
            Log.d("JobApplication", "Applying for job with ID: " + job.getId());

            // Add all job information to the application data
            applicationData.put("jobId", job.getId());
            applicationData.put("userId", userId);
            applicationData.put("appliedDate", new Timestamp(new Date()));
            applicationData.put("status", "Applied");

            // Additional job details
            applicationData.put("jobName", job.getJobName());
            applicationData.put("employerID", job.getEmployerID());
            applicationData.put("location", job.getLocation());
            applicationData.put("duration", job.getDuration());
            applicationData.put("salary", job.getSalary());
            applicationData.put("urgency", job.getUrgency());
            applicationData.put("postalCode", job.getPostalCode());

            db.collection("appliedJobs").add(applicationData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Job applied successfully!", Toast.LENGTH_SHORT).show();

                        // Navigate to AppliedJobsActivity after successful application
                        Intent intent = new Intent(this, AppliedJobsActivity.class);
                        intent.putExtra("userId", userId); // Pass userId to AppliedJobsActivity if needed
                        startActivity(intent);
                        finish(); // Optionally finish this activity to prevent returning to it
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to apply for job.", Toast.LENGTH_SHORT).show();
                    });
        }

        @Override
        public void onSaveToFavorites(Job job) {
            // Use the userId to save the job in the favorites collection
            saveJobToFavorites(job);
        }

        // Separate method to save the job to Firestore


        public void saveJobToFavorites(Job job) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> favoriteData = new HashMap<>();

            // Add all job details to the favorite data
            favoriteData.put("jobId", job.getId());
            favoriteData.put("userId", userId);
            favoriteData.put("jobName", job.getJobName());
            favoriteData.put("employerID", job.getEmployerID());
            favoriteData.put("location", job.getLocation());
            favoriteData.put("duration", job.getDuration());
            favoriteData.put("salary", job.getSalary());
            favoriteData.put("urgency", job.getUrgency());
            favoriteData.put("postalCode", job.getPostalCode());

            db.collection("favorites").add(favoriteData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Job added to favorites!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add job to favorites.", Toast.LENGTH_SHORT).show();
                    });
        }
    }


