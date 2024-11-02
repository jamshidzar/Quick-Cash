package com.example.registration;
import android.content.Intent;
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

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");

        jobName = findViewById(R.id.jobName);
        location = findViewById(R.id.location);
        searchButton = findViewById(R.id.applyButton);

        getUserID(id -> {
            if (id != null){
                userID = id;
            }
        });
        searchButton.setOnClickListener(v -> filterJobs());
    }

    protected void getUserID(JobFilter.FirestoreCallBack callback) {
        db.collection("user").whereEqualTo("Email", email).get()
                .addOnCompleteListener(task -> {
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
                });
    }

    public interface FirestoreCallBack {
        void onCallBack(String userID);
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


        Intent intent = new Intent(JobFilter.this, JobListActivity.class);
        intent.putExtra("jobName", jobNameText);
        intent.putExtra("location", locationText);
        startActivity(intent);
    }
}



