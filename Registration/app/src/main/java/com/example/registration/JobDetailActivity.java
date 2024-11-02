package com.example.registration;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class JobDetailActivity extends AppCompatActivity {

    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_job_detail);
//
//        // Retrieve the Job object from the intent
//        Job job = (Job) getIntent().getSerializableExtra("job");
//
//        // Find all TextViews in the layout and set their text
//        TextView jobNameTextView = findViewById(R.id.job_name);
//        TextView employerIdTextView = findViewById(R.id.employer_id);
//        TextView locationTextView = findViewById(R.id.location);
//        TextView durationTextView = findViewById(R.id.duration);
//        TextView salaryTextView = findViewById(R.id.salary);
//        TextView urgencyTextView = findViewById(R.id.urgency);
//
//        // Set job details in the UI
//        jobNameTextView.setText(job.getJobName());
//        employerIdTextView.setText(job.getEmployerID());
//        locationTextView.setText(job.getLocation());
//        durationTextView.setText(job.getDuration());
//        salaryTextView.setText(job.getSalary());
//        urgencyTextView.setText(job.getUrgency());
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        // Retrieve data from intent extras
        String jobName = getIntent().getStringExtra("jobName");
        String employerID = getIntent().getStringExtra("employerID");
        String location = getIntent().getStringExtra("location");
        String duration = getIntent().getStringExtra("duration");
        String salary = getIntent().getStringExtra("salary");
        String urgency = getIntent().getStringExtra("urgency");

        // Find all TextViews in the layout and set their text
        TextView jobNameTextView = findViewById(R.id.job_name);
        TextView employerIdTextView = findViewById(R.id.employer_id);
        TextView locationTextView = findViewById(R.id.location);
        TextView durationTextView = findViewById(R.id.duration);
        TextView salaryTextView = findViewById(R.id.salary);
        TextView urgencyTextView = findViewById(R.id.urgency);

        // Set text in the UI elements
        jobNameTextView.setText(jobName != null ? jobName : "N/A");
        employerIdTextView.setText(employerID != null ? employerID : "N/A");
        locationTextView.setText(location != null ? location : "N/A");
        durationTextView.setText(duration != null ? duration : "N/A");
        salaryTextView.setText(salary != null ? salary : "N/A");
        urgencyTextView.setText(urgency != null ? urgency : "N/A");

        // Debug log to confirm data was received
        Log.d("JobDetailActivity", "Loaded job details for: " + jobName);
    }
}
