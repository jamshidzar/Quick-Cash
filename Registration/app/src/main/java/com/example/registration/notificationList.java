package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class notificationList extends AppCompatActivity {
    private RecyclerView preferredJobsRecyclerView;
    private List<Job> preferredJobsList;
    private List<Job> availableJobsList;
    Employee employee;
    notificationAdapter notification;
    public notificationList(){
        preferredJobsList = new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);
        employee = new Employee();
        preferredJobsRecyclerView = findViewById(R.id.notificationRecyclerView);
        preferredJobsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAvailableJobs();
        loadPreferredJobs();
        notification = new notificationAdapter(availableJobsList, preferredJobsList, this::onApplyJob);
    }

    private void loadPreferredJobs() {
    }

    private void loadAvailableJobs() {
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
    public List<Job> getAvailableJobs(){
        return availableJobsList;
    }
    public List<Job> getPreferredJobs(){
        return preferredJobsList;
    }
}
