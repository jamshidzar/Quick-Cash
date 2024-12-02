package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class AppliedJobsActivity extends AppCompatActivity {
    private RecyclerView appliedJobsRecyclerView;
    AppliedJobsAdapter appliedJobsAdapter;
    List<Job> appliedJobsList;
    private String userId; // To store the userId for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_jobs);



        // Get userId passed from the previous activity (e.g., login or main activity)
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "User ID not found. Returning to login.", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity if userId is missing
            return;
        }

        appliedJobsRecyclerView = findViewById(R.id.appliedJobsRecyclerView);
        appliedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        appliedJobsList = new ArrayList<>();
        appliedJobsAdapter = new AppliedJobsAdapter(appliedJobsList, job -> onCompleteJob(job));
        appliedJobsRecyclerView.setAdapter(appliedJobsAdapter);

        loadAppliedJobs(userId);

        // Back to Job Listing Button
        Button backToJobListingButton = findViewById(R.id.backToJobListingButton);
        backToJobListingButton.setOnClickListener(v -> {
            // Navigate back to JobListActivity
            Intent intent = new Intent(AppliedJobsActivity.this, JobListActivity.class);
            intent.putExtra("userId", userId); // Pass userId back if needed
            startActivity(intent);
            finish(); // Close AppliedJobsActivity
        });
    }

    private void loadAppliedJobs(String userId) {
        FirebaseFirestore db = FirebaseSingleton.getInstance().getDb();
        db.collection("appliedJobs").whereEqualTo("userId", userId).get()
                .addOnSuccessListener(querySnapshot -> {
                    appliedJobsList.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Job job = new Job();

                        job.setId(document.getString("jobId"));
                        job.setJobName(document.getString("jobName"));
                        job.setEmployerID(document.getString("employerID"));
                        job.setLocation(document.getString("location"));
                        job.setDuration(document.getString("duration"));
                        job.setSalary(document.getString("salary"));
                        job.setUrgency(document.getString("urgency"));
                        job.setPostalCode(document.getString("postalCode"));

                        appliedJobsList.add(job);
                    }
                    appliedJobsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load applied jobs.", Toast.LENGTH_SHORT).show();
                });
    }





    void onCompleteJob(Job job) {
        FirebaseFirestore db = FirebaseSingleton.getInstance().getDb();
        String jobId = job.getId();

        // Retrieve the applied job directly from the appliedJobs collection
        db.collection("appliedJobs")
                .whereEqualTo("jobId", jobId)
                .whereEqualTo("userId", userId) // Ensure the applied job is specific to the user
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot appliedJobDoc = querySnapshot.getDocuments().get(0);
                        Map<String, Object> completedJobData = appliedJobDoc.getData();

                        if (completedJobData != null) {
                            // Update the status and add a completion date
                            completedJobData.put("status", "Completed");
                            completedJobData.put("completionDate", new Timestamp(new Date()));
                            completedJobData.put("paymentStatus", "Not Paid");
                            // Add to completedJobs collection
                            db.collection("completedJobs").add(completedJobData)
                                    .addOnSuccessListener(aVoid -> {
                                        // After successfully adding to completedJobs, delete from appliedJobs
                                        appliedJobDoc.getReference().delete()
                                                .addOnSuccessListener(deleteVoid -> {
                                                    // Update the UI by removing the job from the list
                                                    appliedJobsList.remove(job);
                                                    appliedJobsAdapter.notifyDataSetChanged();
                                                    Toast.makeText(this, "Job marked as complete and moved to Completed Jobs.", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Failed to delete job from Applied Jobs.", Toast.LENGTH_SHORT).show();
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to add job to Completed Jobs.", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "No matching job found in appliedJobs.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to retrieve job for completion.", Toast.LENGTH_SHORT).show();
                });
    }


}