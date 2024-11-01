package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppliedJobsActivity extends AppCompatActivity {
    private RecyclerView appliedJobsRecyclerView;
    private AppliedJobsAdapter appliedJobsAdapter;
    private List<Job> appliedJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);

        appliedJobsRecyclerView = findViewById(R.id.appliedJobsRecyclerView);
        appliedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        appliedJobsList = new ArrayList<>();
        appliedJobsAdapter = new AppliedJobsAdapter(appliedJobsList, this::onCompleteJob);
        appliedJobsRecyclerView.setAdapter(appliedJobsAdapter);


        loadAppliedJobs();

        // Back to Job Listing Button
        Button backToJobListingButton = findViewById(R.id.backToJobListingButton);
        backToJobListingButton.setOnClickListener(v -> {
            // Navigate back to JobListActivity
            Intent intent = new Intent(AppliedJobsActivity.this, JobListActivity.class);
            startActivity(intent);
            finish(); // Close AppliedJobsActivity if you want to remove it from the stack
        });


    }



    private void loadAppliedJobs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch applied jobs for the user from Firestore
            db.collection("jobSeekers").document(userId)
                    .collection("appliedJobs")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        appliedJobsList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Job job = document.toObject(Job.class);
                            appliedJobsList.add(job);
                        }
                        appliedJobsAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load applied jobs.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }









    private void onCompleteJob(Job job) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String jobId = job.getId();

            // Reference to the applied job document
            DocumentReference appliedJobRef = db.collection("jobSeekers").document(userId)
                    .collection("appliedJobs").document(jobId);

            // Reference to the completed job collection
            CollectionReference completedJobsRef = db.collection("jobSeekers").document(userId)
                    .collection("completedJobs");

            // Fetch the job document, add it to completedJobs, and delete from appliedJobs
            appliedJobRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Copy the document data and add completion fields
                    Map<String, Object> jobData = documentSnapshot.getData();
                    if (jobData != null) {
                        jobData.put("status", "completed");
                        jobData.put("paymentStatus", "awaiting payment");

                        // Add the document to completedJobs
                        completedJobsRef.document(jobId).set(jobData)
                                .addOnSuccessListener(aVoid -> {
                                    // Remove the job from appliedJobs after successful addition to completedJobs
                                    appliedJobRef.delete().addOnSuccessListener(deleteVoid -> {
                                        // Update the UI
                                        appliedJobsList.remove(job);
                                        appliedJobsAdapter.notifyDataSetChanged();
                                        Toast.makeText(this, "Job marked as complete and moved to Completed Jobs.", Toast.LENGTH_SHORT).show();

                                        // Check if appliedJobsList is empty, and if so, navigate back to JobListActivity
                                        if (appliedJobsList.isEmpty()) {
                                            Toast.makeText(this, "All jobs completed. Returning to Job List.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(this, JobListActivity.class);
                                            startActivity(intent);
                                            finish(); // Close the current activity
                                        }
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to delete job from Applied Jobs.", Toast.LENGTH_SHORT).show();
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to add job to Completed Jobs.", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to fetch job details.", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }



}

