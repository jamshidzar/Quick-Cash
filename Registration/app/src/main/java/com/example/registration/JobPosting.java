package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the ability for employers to post jobs and stores them in the FireBase database.
 */
public class JobPosting extends AppCompatActivity {
    private String email;
    private String userID;

    private EditText jobName;
    private EditText location;
    private EditText duration;
    private EditText urgency;
    private EditText salary;
    private Button postButton;

    private FirebaseFirestore db;

    /**
     * This method is called when the activity is created. It initializes the database and all UI
     * elements.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_posting);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        userID = intent.getStringExtra("userID");

        jobName = findViewById(R.id.jobName);
        location = findViewById(R.id.location);
        duration = findViewById(R.id.duration);
        urgency = findViewById(R.id.urgency);
        salary = findViewById(R.id.salary);
        postButton = findViewById(R.id.postButton);

        postButton.setOnClickListener(v -> postJob());
    }

    /**
     * This method takes all of the user input for a given job and, when the user clicks on the
     * button to post a job, it stores all of the details in the database, then takes the user
     * to their listings.
     */
    protected void postJob(){
        // Get text input fields
        String jobNameText = jobName.getText().toString();
        String locationText = location.getText().toString();
        String durationText = duration.getText().toString();
        String urgencyText = urgency.getText().toString();
        String salaryText = salary.getText().toString();

        Map<String, Object> job = new HashMap<>();
        job.put("jobName", jobNameText);
        job.put("location", locationText);
        job.put("duration", durationText);
        job.put("urgency", urgencyText);
        job.put("salary", salaryText);
        job.put("employerID", userID);


        if (jobNameText.isEmpty()) {
            jobName.setError("Job name cannot be blank");
            return;
        }

        if (locationText.isEmpty()) {
            location.setError("Location cannot be blank");
            return;
        }

        if (durationText.isEmpty()) {
            duration.setError("Duration cannot be blank");
            return;
        }

        if (urgencyText.isEmpty()) {
            urgency.setError("Urgency cannot be blank");
            return;
        }

        if (salaryText.isEmpty()) {
            salary.setError("Salary cannot be blank");
            return;
        }


        db.collection("job").add(job)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(JobPosting.this, "Job Posting Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(JobPosting.this, JobList.class);
                        intent.putExtra("Email", email);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JobPosting.this, "Job Posting Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
