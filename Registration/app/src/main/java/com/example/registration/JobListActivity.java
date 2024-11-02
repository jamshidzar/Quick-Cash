package com.example.registration;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobListActivity extends AppCompatActivity implements JobAdapter.OnSaveToFavoritesListener, JobAdapter.OnApplyJobListener {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private RecyclerView availableJobsRecyclerView;
    private JobAdapter jobAdapter;
    private List<Job> availableJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Setup RecyclerView and Adapter
        availableJobsList = new ArrayList<>();
        jobAdapter = new JobAdapter(availableJobsList, this, this);
        availableJobsRecyclerView = findViewById(R.id.availableJobsRecyclerView);
        availableJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableJobsRecyclerView.setAdapter(jobAdapter);

        // Retrieve search criteria from Intent
        Intent intent = getIntent();
        String jobName = intent.getStringExtra("jobName");
        String location = intent.getStringExtra("location");

        // Load jobs from Firestore with the search criteria
        loadJobsFromFirestore(jobName, location);

        findViewById(R.id.button3).setOnClickListener(v -> {
            Intent searchIntent = new Intent(JobListActivity.this, JobFilter.class);
            startActivity(searchIntent);
        });
    }

    private void loadJobsFromFirestore(String jobName, String location) {
        CollectionReference jobsRef = firestore.collection("job");

        // Apply filters only if jobName and location are not null
        if (jobName != null && location != null) {
            jobsRef.whereEqualTo("jobName", jobName)
                    .whereEqualTo("location", location)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            availableJobsList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Job job = document.toObject(Job.class);
                                job.setId(document.getId());
                                availableJobsList.add(job);
                            }
                            jobAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("FirestoreError", "Error fetching jobs: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                        }
                    });
        } else {
            // If no filters, load all jobs
            jobsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    availableJobsList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Job job = document.toObject(Job.class);
                        job.setId(document.getId());
                        availableJobsList.add(job);
                    }
                    jobAdapter.notifyDataSetChanged();
                } else {
                    Log.e("FirestoreError", "Error fetching jobs: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                }
            });
        }
    }






    @Override
    public void onApplyJob(Job job) {
        // Initialize Firestore and get the current user
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's applied jobs collection
            db.collection("jobSeekers").document(userId)
                    .collection("appliedJobs").document(job.getId()) // Use job ID as the document ID
                    .set(job)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(JobListActivity.this, "Job applied successfully!", Toast.LENGTH_SHORT).show();

                        // Navigate to AppliedJobsActivity
                        Intent intent = new Intent(JobListActivity.this, AppliedJobsActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(JobListActivity.this, "Failed to apply for job.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(JobListActivity.this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onSaveToFavorites(Job job) {
        if (auth.getCurrentUser() == null) {
            // If no user is signed in, sign in anonymously
            auth.signInAnonymously().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // After signing in anonymously, proceed to save the job
                    saveJobToFavorites(job);
                } else {
                    // Handle sign-in failure
                    Log.e("JobListActivity", "Anonymous sign-in failed", task.getException());
                    Toast.makeText(this, "Failed to sign in. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If a user is already signed in, proceed to save the job
            saveJobToFavorites(job);
        }
    }

    // Separate method to save the job to Firestore
    private void saveJobToFavorites(Job job) {
        String userId = auth.getCurrentUser().getUid();
        CollectionReference favoritesRef = firestore.collection("jobSeekers").document(userId).collection("favorites");

        favoritesRef.document(job.getId()).set(job)
                .addOnSuccessListener(aVoid -> {
                    Log.d("JobListActivity", "Job successfully added to favorites in Firestore");
                    Toast.makeText(this, "Job added to favorites!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("JobListActivity", "Failed to add job to favorites: " + e.getMessage());
                    Toast.makeText(this, "Failed to add job to favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
