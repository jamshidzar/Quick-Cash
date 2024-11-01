package com.example.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
    private FirebaseFirestore db;
    Intent jobListView;
    Button back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        jobListView = getIntent();
        email = jobListView.getStringExtra("Email");
        listView = findViewById(R.id.list_view);
        jobList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobList);
        listView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();

        getUserID(id -> {
            if (id != null){
                userID = id;
                Log.d("JobList", "User ID retrieved: " + userID);
                loadJobs();
            }
            else{
                Log.d("Firestore", "User not found.");
            }
        });
        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(JobList.this , Employer.class);
                back.putExtra("Email" , email);
                startActivity(back);
            }
        });
    }

    private void loadJobs() {
        if (userID != null && !userID.isEmpty()) {
            db.collection("job").whereEqualTo("employerID", userID).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    String jobName = document.getString("jobName");
                                    String location = document.getString("location");
                                    String duration = document.getString("duration");
                                    String urgency = document.getString("urgency");
                                    String salary = document.getString("salary");

                                    String jobDetails = "Job: " + jobName + "\nLocation: " + location
                                            + "\nDuration: " + duration + "\nUrgency: " + urgency
                                            + "\nSalary: " + salary;

                                    Log.d("JobList", "Job retrieved: " + jobDetails);
                                    jobList.add(jobDetails);
                                }
                                runOnUiThread(() -> adapter.notifyDataSetChanged());
                            } else {
                                Toast.makeText(JobList.this, "Error loading jobs.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(JobList.this, "Not found", Toast.LENGTH_SHORT).show();
        }
    }
    protected void getUserID(JobPosting.FirestoreCallBack callback){

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
}
