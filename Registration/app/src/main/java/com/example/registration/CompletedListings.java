package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedListings extends AppCompatActivity {
    private FirebaseFirestore db;

    private RecyclerView completedListingsRecyclerView;
    private CompletedJobsAdapter completedJobsAdapter;
    private List<Job> completedJobsList;

    private String email;
    private String userID;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_listings);
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");

        getUserID(id -> {
            if (id != null){
                userID = id;
                Log.d("JobPosting", "User ID retrieved: " + userID);
            }
            else{
                Log.d("Firestore", "User not found.");
            }
        });

        completedListingsRecyclerView = findViewById(R.id.completedListingRecyclerView);
        completedListingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        completedJobsList = new ArrayList<>();
        loadCompletedJobs();

        completedJobsAdapter = new CompletedJobsAdapter(completedJobsList, this::onPayment);
        completedListingsRecyclerView.setAdapter(completedJobsAdapter);
    }

    private void loadCompletedJobs(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch applied jobs for the user from Firestore
            db.collection("jobSeekers").document(userId)
                    .collection("completed")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        completedJobsList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Job job = document.toObject(Job.class);
                            completedJobsList.add(job);
                        }
                        completedJobsAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load completed jobs.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }

    }

    private void onPayment(Job job){

            String jobId = job.getId();

            db.collection("jobSeekers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               Map<String, Object> allCompletedJobs = new HashMap<>();
                               for (QueryDocumentSnapshot document : task.getResult()) {
                                   String jobSeekerID = document.getId();
                                   db.collection("jobSeekers").document(jobSeekerID).collection("completedJobs")
                                           .whereEqualTo("employerID", userID)
                                           .get()
                                           .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<QuerySnapshot> completedTask) {
                                                   if (completedTask.isSuccessful()) {
                                                        for (QueryDocumentSnapshot completedDoc : completedTask.getResult()){
                                                            Map<String, Object> jobData = completedDoc.getData();

                                                        }
                                                   }
                                               }
                                           });
                               }
                           } else {
                               Log.d("Firestore error", "errror geetting documents", task.getException());
                           }
                       }




            // Reference to the applied job document
            DocumentReference appliedJobRef = db.collection("jobSeekers").document()
                    .collection("appliedJobs").document(jobId);

            // Reference to the completed job collection
            CollectionReference completedJobsRef = db.collection("jobSeekers").document(userID)
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
                                        completedJobsList.remove(job);
                                        completedJobsAdapter.notifyDataSetChanged();
                                        Toast.makeText(this, "Job marked as complete and moved to Completed Jobs.", Toast.LENGTH_SHORT).show();

                                        // Check if appliedJobsList is empty, and if so, navigate back to JobListActivity
                                        if (completedJobsList.isEmpty()) {
                                            Toast.makeText(this, "All jobs completed. Returning to Job List.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(this, Employer.class);
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

    protected void getUserID(JobPosting.FirestoreCallBack callback){

        db.collection("user").whereEqualTo("Email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String userID = documentSnapshot.getId();
                                callback.onCallBack(userID);
                            } else {
                                callback.onCallBack(null);
                            }
                        }
                    }
                });
    }


}

