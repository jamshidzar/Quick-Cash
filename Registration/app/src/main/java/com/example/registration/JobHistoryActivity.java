package com.example.registration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobHistoryActivity extends AppCompatActivity {
    private static final String TAG = "JobHistoryActivity";

    private FirebaseFirestore firestore;
    private String userId;
    private RecyclerView recyclerView;
    private JobHistoryAdapter adapter;
    private List<JobHistory> jobHistoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_history);
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = userId = sharedPref.getString("userId", null);
        Log.d("JobHistoryActivity", "Received userId: " + userId);
        if (userId == null) {
            Toast.makeText(this, "User ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewJobHistory);
        if (recyclerView == null) {
            Log.e("JobHistoryActivity", "RecyclerView is null!");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobHistoryList = new ArrayList<>();
        adapter = new JobHistoryAdapter(jobHistoryList);
        recyclerView.setAdapter(adapter);
        loadJobHistory();
    }


    private void loadJobHistory() {
        firestore.collection("completedJobs")
                .whereEqualTo("userId", userId)
                .orderBy("completionDate", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.w("JobHistory", "Listen failed.", e);
                        return;
                    }

                    if (querySnapshot != null) {
                        Log.d("JobHistory", "Snapshot listener triggered. Query size: " + querySnapshot.size());
                        jobHistoryList.clear();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            String jobId = document.getId();
                            String jobName = document.getString("jobName");
                            String location = document.getString("location");
                            String postalCode = document.getString("postalCode");
                            String salary = document.getString("salary");
                            String duration = document.getString("duration");
                            String urgency = document.getString("urgency");

                            Timestamp completionTimestamp = document.getTimestamp("completionDate");
                            String completionDate = "";
                            if (completionTimestamp != null) {
                                completionDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .format(completionTimestamp.toDate());
                            }

                            jobHistoryList.add(new JobHistory(jobId, jobName, completionDate, location, postalCode, salary, duration, urgency));
                        }

                        Log.d("JobHistory", "Filtered jobHistoryList size: " + jobHistoryList.size());
                        adapter.notifyDataSetChanged();
                    }
                });

    }
}
