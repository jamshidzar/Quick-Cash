package com.example.registration;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class JobFilter extends AppCompatActivity {
    private String email;
    private String userID;

    private EditText jobName;
    private EditText location;

    private Button searchButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_filter);

        db = FirebaseSingleton.getInstance().getDb();
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userID = sharedPref.getString("userId", null);
        email = sharedPref.getString("Email", null);

        jobName = findViewById(R.id.jobName);
        location = findViewById(R.id.location);
        searchButton = findViewById(R.id.applyButton);

        searchButton.setOnClickListener(v -> filterJobs());
    }

    protected void filterJobs() {
        String jobNameText = jobName.getText().toString();
        String locationText = location.getText().toString();

        if (jobNameText.isEmpty()) {
            jobName.setError("Job name cannot be blank");
            return;
        }

        if (locationText.isEmpty()) {
            location.setError("Location cannot be blank");
            return;
        }


        Intent intent = new Intent(JobFilter.this, FilteredJobListActivity.class);
        intent.putExtra("jobName", jobNameText);
        intent.putExtra("location", locationText);
        startActivity(intent);
    }
}



