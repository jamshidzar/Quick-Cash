package com.example.registration;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class IncomeHistoryActivity extends AppCompatActivity {
    private TextView totalIncomeText;
    private String userID;
    private RecyclerView recyclerView;
    private IncomeHistoryAdapter adapter;
    private ArrayList<JobIncomeHistory> jobIncomeHistories;
    private Button incomeHistoryBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_history);
        db = FirebaseSingleton.getInstance().getDb();
        //Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.completedListingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobIncomeHistories = new ArrayList<>();
        adapter = new IncomeHistoryAdapter(jobIncomeHistories);
        recyclerView.setAdapter(adapter);
        userID = getIntent().getStringExtra("userID");
        totalIncomeText = findViewById(R.id.totalIncomeTextView);
        incomeHistoryBtn = findViewById(R.id.incomeHistoryGraphBtn);

        findViewById(R.id.backBtn).setOnClickListener(v -> goToEmployee());
        incomeHistoryBtn.setOnClickListener(v -> goToEarningsGraph());

        if (userID == null){
            Log.e("Error", "userID is null");
            return;
        }

        setTotalIncome();
        loadCompletedJobs();
    }

    private void setTotalIncome(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        String money = "$";
                        String totalIncome = money.concat(documentSnapshot.getString("Total Income"));
                        totalIncomeText.setText(totalIncome);
                        Log.d("FirestoreData", "Field value: " + totalIncome);
                    } else {
                        Log.d("FirestoreData", "No such document!");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error getting document", e);
                });
    }

    private void loadCompletedJobs() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("completedJobs")
                .whereEqualTo("userId", userID)
                .whereEqualTo("paymentStatus", "Paid")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    jobIncomeHistories.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String jobName = document.getString("jobName");
                        String location = document.getString("location");
                        String salaryStr = document.getString("salary"); //taking a string from db, need to turn this into int
                        String durationStr = document.getString("duration");
                        Timestamp timestampType = document.getTimestamp("completionDate");
                        String timestamp = timestampType.toDate().toString();

                        try {
                            int salary = (salaryStr != null) ? Integer.parseInt(salaryStr) : 0;
                            int duration = (durationStr != null) ? Integer.parseInt(durationStr) : 0;

                            int totalEarned = salary * duration;
                            // Check for null values
                            // Check for null jobName and duration before adding to the list
                            if (jobName != null && location != null) {
                                jobIncomeHistories.add(new JobIncomeHistory(jobName, location, totalEarned, timestamp));
                            }
                        } catch (NumberFormatException e) {
                            // Handle invalid salary conversion gracefully
                            Toast.makeText(this, "Invalid salary format in database.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                })
                .addOnFailureListener(e -> {
                    // Display an error message if the query fails
                    Log.e("FirestoreError", "Error fetching completed jobs", e);
                    Toast.makeText(this, "Failed to load completed jobs: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void goToEarningsGraph(){
        Intent intent = new Intent(IncomeHistoryActivity.this, IncomeHistoryChartActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("jobs", jobIncomeHistories);
        startActivity(intent);
    }

    private void goToEmployee(){
        Intent intent = new Intent(IncomeHistoryActivity.this, Employee.class);
        startActivity(intent);
    }
}

