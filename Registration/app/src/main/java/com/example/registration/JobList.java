package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobList extends AppCompatActivity {
    private String email;
    private String userID;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> jobList;
    private List<String> jobIDs; // List to store job IDs
    private FirebaseFirestore db;
    Intent jobListView;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employer_job_list);

        jobListView = getIntent();
        email = jobListView.getStringExtra("Email");
        userID = jobListView.getStringExtra("userID");

        listView = findViewById(R.id.list_view);
        jobList = new ArrayList<>();
        jobIDs = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobList);
        listView.setAdapter(adapter);
        db = FirebaseSingleton.getInstance().getDb();

        // Set the click listener for ListView items
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the job ID for the clicked job
            String selectedJobID = jobIDs.get(position);
            String selectedJobName = jobList.get(position);

            // Navigate to the new activity with the job ID
            Intent intent = new Intent(JobList.this, EmployeeListActivity.class);
            intent.putExtra("jobID", selectedJobID);
            intent.putExtra("jobName", selectedJobName);
            intent.putExtra("Email", email);
            startActivity(intent);
        });

        // Load the jobs
        loadJobs();

        // Back button to return to the previous activity
        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> {
            Intent backIntent = new Intent(JobList.this, Employer.class);
            backIntent.putExtra("Email", email);
            startActivity(backIntent);
        });
    }

    private void loadJobs() {
        if (userID != null && !userID.isEmpty()) {
            db.collection("job").whereEqualTo("employerID", userID).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String jobID = document.getId(); // Get job ID
                                String jobName = document.getString("jobName");
                                String location = document.getString("location");
                                String postalCode = document.getString("postalCode");
                                String duration = document.getString("duration");
                                String urgency = document.getString("urgency");
                                String salary = document.getString("salary");

                                String jobDetails = "Job: " + jobName + "\nLocation: " + location + "\nPostal Code: " + postalCode
                                        + "\nDuration: " + duration + "\nUrgency: " + urgency + "\nSalary: " + salary;

                                Log.d("JobList", "Job retrieved: " + jobDetails);
                                jobList.add(jobDetails);
                                jobIDs.add(jobID); // Store job ID
                            }
                            runOnUiThread(() -> adapter.notifyDataSetChanged());
                        } else {
                            Toast.makeText(JobList.this, "Error loading jobs.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(JobList.this, "User ID not found.", Toast.LENGTH_SHORT).show();
        }
    }
}
