package com.example.registration;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_posting);

        db = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("Email");

        jobName = findViewById(R.id.jobName);
        location = findViewById(R.id.location);
        duration = findViewById(R.id.duration);
        urgency = findViewById(R.id.urgency);
        salary = findViewById(R.id.salary);
        postButton = findViewById(R.id.postButton);

        getUserID(id -> {
            if (id != null){
                userID = id;
            }
            else{
                Log.d("Firestore", "User not found.");
            }
        });

        postButton.setOnClickListener(v -> postJob());
    }

    protected void getUserID(FirestoreCallBack callback){

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

    public interface FirestoreCallBack{
        void onCallBack(String userID);
    }

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

        db.collection("user").add(job)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(JobPosting.this, "Job Posting Successful", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(JobPosting.this, "Job Posting Failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
