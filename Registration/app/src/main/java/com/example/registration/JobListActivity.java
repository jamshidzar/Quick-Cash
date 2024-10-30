package com.example.registration;
import com.example.registration.Job;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

        jobAdapter = new JobAdapter(availableJobsList, this::onApplyJob);
        availableJobsRecyclerView.setAdapter(jobAdapter);
    }

    private void loadSampleJobs() {
        // Adding sample data to the availableJobsList
        availableJobsList.add(new Job("1", "Software Engineer", "Tech Company"));
        availableJobsList.add(new Job("2", "Product Manager", "Product Inc."));
        availableJobsList.add(new Job("3", "Data Analyst", "Analytics LLC"));
        availableJobsList.add(new Job("4", "Marketing Specialist", "Media Group"));
        availableJobsList.add(new Job("5", "Graphic Designer", "Creative Studio"));
        availableJobsList.add(new Job("6", "Sales Associate", "Retail Co."));
        availableJobsList.add(new Job("7", "Operations Manager", "Logistics Solutions"));
        availableJobsList.add(new Job("8", "IT Support Technician", "Tech Solutions"));
        availableJobsList.add(new Job("9", "Financial Analyst", "Finance Corp."));
        availableJobsList.add(new Job("10", "HR Coordinator", "Human Resources Inc."));
        availableJobsList.add(new Job("11", "Research Assistant", "University Lab"));
        availableJobsList.add(new Job("12", "Customer Service Representative", "Service Center"));
        availableJobsList.add(new Job("13", "Project Coordinator", "Construction Group"));

        // Initialize adapter with sample data and set to RecyclerView
        jobAdapter = new JobAdapter(availableJobsList, this::onApplyJob);
        availableJobsRecyclerView.setAdapter(jobAdapter);
    }
    private Set<String> appliedJobIds = new HashSet<>();

    private void onApplyJob(Job job) {
        if (appliedJobIds.contains(job.getId())) {
            // Job already applied for, show a message
            Toast.makeText(this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
        } else {
            // Job not applied for yet, proceed with application
            appliedJobIds.add(job.getId());  // Mark this job as applied
            Intent resultIntent = new Intent();
            resultIntent.putExtra("jobId", job.getId());
            resultIntent.putExtra("jobTitle", job.getJobTitle());
            resultIntent.putExtra("companyName", job.getCompany());
            setResult(RESULT_OK, resultIntent);
            finish();  // Close JobListActivity and return to Employee
        }
    }

}