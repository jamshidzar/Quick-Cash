package com.example.registration;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Employee extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button jobApplyingButton;
    private RecyclerView appliedJobsRecyclerView;
    private AppliedJobsAdapter appliedJobsAdapter;
    private List<Job> appliedJobsList;

    private static final int APPLY_JOB_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empolyee);

        db = FirebaseFirestore.getInstance();
        Intent welcome = getIntent();
        String email = welcome.getStringExtra("Email");

        // Initialize "Job Applying" button and set it to start JobListActivity for result
        jobApplyingButton = findViewById(R.id.jobApplyingButton);
        jobApplyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employee.this, JobListActivity.class);
                startActivityForResult(intent, APPLY_JOB_REQUEST); // Start JobListActivity for result
            }
        });

        // Initialize RecyclerView and adapter for applied jobs
        appliedJobsRecyclerView = findViewById(R.id.appliedJobsRecyclerView);
        appliedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        appliedJobsList = new ArrayList<>();
        appliedJobsAdapter = new AppliedJobsAdapter(appliedJobsList, this::onCompleteJob);
        appliedJobsRecyclerView.setAdapter(appliedJobsAdapter);

        // Existing code to fetch and display user information...
    }

    // Override onActivityResult to handle job application data from JobListActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APPLY_JOB_REQUEST && resultCode == RESULT_OK) {
            // Get job data from the intent
            String jobId = data.getStringExtra("jobId");
            String jobTitle = data.getStringExtra("jobTitle");
            String companyName = data.getStringExtra("companyName");

            // Create a new Job object with the received data
            Job appliedJob = new Job(jobId, jobTitle, companyName);

            // Add the job to the applied jobs list and refresh the adapter
            addAppliedJob(appliedJob);
        }
    }

    // Method to add an applied job to the list
    // Method to add an applied job to the list
    public void addAppliedJob(Job job) {
        // Check if the job is already in the applied jobs list
        for (Job appliedJob : appliedJobsList) {
            if (appliedJob.getId().equals(job.getId())) {
                Toast.makeText(this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
                return; // Exit the method without adding duplicate
            }
        }

        // If not a duplicate, add the job
        appliedJobsList.add(job);
        appliedJobsAdapter.notifyDataSetChanged();
    }


    // Placeholder for "Complete" button action
    private void onCompleteJob(Job job) {
        appliedJobsList.remove(job);
        appliedJobsAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Job marked as complete", Toast.LENGTH_SHORT).show();
    }
}

