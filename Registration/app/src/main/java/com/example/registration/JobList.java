package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> jobList;
    private FirebaseFirestore db;
    Intent jobListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        jobListView = getIntent();
        listView = findViewById(R.id.list_view);
        jobList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobList);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadJobs();
    }

    private void loadJobs() {
        db.collection("job").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String jobName = document.getString("jobName");
                        String location = document.getString("location");
                        String duration = document.getString("duration");
                        String urgency = document.getString("urgency");
                        String salary = document.getString("salary");

                        String jobDetails = "Job: " + jobName + "\nLocation: " + location + "\nDuration: " + duration
                                + "\nUrgency: " + urgency + "\nSalary: " + salary;
                        Log.d("JobList", "Job retrieved: " + jobDetails);

                        jobList.add(jobDetails);
                        Toast.makeText(JobList.this, "Added job: " + jobName, Toast.LENGTH_SHORT).show();

                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged());

                } else {
                    Toast.makeText(JobList.this, "Error getting jobs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
