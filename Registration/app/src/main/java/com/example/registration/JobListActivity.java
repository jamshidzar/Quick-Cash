package com.example.registration;
import com.example.registration.Job;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


//
//public class JobListActivity extends AppCompatActivity {
//    private RecyclerView availableJobsRecyclerView;
//    private JobAdapter jobAdapter;
//    private List<Job> availableJobsList;
//    private FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_job_list);
//
//        db = FirebaseFirestore.getInstance();
//        availableJobsRecyclerView = findViewById(R.id.availableJobsRecyclerView);
//        availableJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        availableJobsList = new ArrayList<>();
//        loadAvailableJobs();
//    }
//
//    private void loadAvailableJobs() {
//        // Fetch available jobs from Firestore
//        db.collection("jobs").get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot doc : task.getResult()) {
//                            Job job = doc.toObject(Job.class);
//                            job.setId(doc.getId());
//                            availableJobsList.add(job);
//                        }
//                        jobAdapter = new JobAdapter(availableJobsList, this::onApplyJob);
//                        availableJobsRecyclerView.setAdapter(jobAdapter);
//                    } else {
//                        Log.e("Firestore", "Error getting jobs", task.getException());
//                    }
//                });
//    }
//
//    private void onApplyJob(Job job) {
//        // Logic for applying to a job (e.g., add to applied jobs)
//    }
//}


public class JobListActivity extends AppCompatActivity {
    private RecyclerView availableJobsRecyclerView;
    private JobAdapter jobAdapter;
    private List<Job> availableJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        availableJobsRecyclerView = findViewById(R.id.availableJobsRecyclerView);
        availableJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        availableJobsList = new ArrayList<>();

        // Load sample data
        loadSampleJobs();
    }

    private void loadSampleJobs() {
        // Adding sample data to the availableJobsList
        availableJobsList.add(new Job("1", "Software Engineer", "Tech Company"));
        availableJobsList.add(new Job("2", "Product Manager", "Product Inc."));
        availableJobsList.add(new Job("3", "Data Analyst", "Analytics LLC"));

        // Initialize adapter with sample data and set to RecyclerView
        jobAdapter = new JobAdapter(availableJobsList, this::onApplyJob);
        availableJobsRecyclerView.setAdapter(jobAdapter);
    }

    private void onApplyJob(Job job) {
        // Display a toast message for now
        Toast.makeText(this, "Applied to " + job.getJobTitle(), Toast.LENGTH_SHORT).show();

        // Placeholder for any additional application logic
        Log.d("Job Application", "Applying to job: " + job.getJobTitle());
    }

}
