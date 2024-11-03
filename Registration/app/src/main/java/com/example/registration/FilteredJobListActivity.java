package com.example.registration;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Filter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilteredJobListActivity extends AppCompatActivity implements JobAdapter.OnSaveToFavoritesListener, JobAdapter.OnApplyJobListener {
    private String email;
    private String userID;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private RecyclerView availableJobsRecyclerView;
    private JobAdapter jobAdapter;
    private List<Job> availableJobsList;

    private String jobName;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        Intent intent = getIntent();
        jobName = intent.getStringExtra("jobName");
        location = intent.getStringExtra("location");

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userID = sharedPref.getString("userId", null);
        email = sharedPref.getString("Email", null);

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Setup RecyclerView and Adapter
        availableJobsList = new ArrayList<>();
        jobAdapter = new JobAdapter(availableJobsList, this, this);
        availableJobsRecyclerView = findViewById(R.id.availableJobsRecyclerView);
        availableJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableJobsRecyclerView.setAdapter(jobAdapter);

        // Load jobs from Firestore
        loadJobsFromFirestore();

        findViewById(R.id.button3).setOnClickListener(v -> {
            Intent searchIntent = new Intent(FilteredJobListActivity.this, JobFilter.class);
            startActivity(searchIntent);
        });
    }

    private void loadJobsFromFirestore() {
        CollectionReference jobsRef = firestore.collection("job");

        jobsRef.whereEqualTo("location", location).whereEqualTo("jobName", jobName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                availableJobsList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Job job = document.toObject(Job.class);
                    job.setId(document.getId()); // Set the document ID

                    // Correct the field name to match Firestore's "PostalCode"
                    if (document.contains("postalCode")) {
                        job.setPostalCode(document.getString("postalCode"));
                    } else {
                        job.setPostalCode("N/A"); // Default value if postal code is missing
                    }

                    availableJobsList.add(job);
                }
                jobAdapter.notifyDataSetChanged();
            } else {
                Log.e("FirestoreError", "Error fetching jobs: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
            }
        });
    }




    @Override
    public void onApplyJob(Job job) {
        // Initialize Firestore and get the current user
        FirebaseFirestore db =   FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's applied jobs collection
            db.collection("jobSeekers").document(userId)
                    .collection("appliedJobs").document(job.getId()) // Use job ID as the document ID
                    .set(job)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(FilteredJobListActivity.this, "Job applied successfully!", Toast.LENGTH_SHORT).show();

                        // Navigate to AppliedJobsActivity
                        Intent intent = new Intent(FilteredJobListActivity.this, AppliedJobsActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(FilteredJobListActivity.this, "Failed to apply for job.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(FilteredJobListActivity.this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onSaveToFavorites(Job job) {
        // Use the userId to save the job in the favorites collection
        saveJobToFavorites(job);
    }

    public void saveJobToFavorites(Job job) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> favoriteData = new HashMap<>();

        // Add all job details to the favorite data
        favoriteData.put("jobId", job.getId());
        favoriteData.put("userId", userID);
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
