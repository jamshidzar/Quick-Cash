package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class EmployeeListActivity extends AppCompatActivity {

    private String jobID;
    private String jobName;
    private ListView employeeListView;
    private Button backToListings;
    private FirebaseFirestore db;
    private ArrayAdapter<String> employeeAdapter;
    private List<String> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list);

        // Initialize views
        employeeListView = findViewById(R.id.employeeListView);
        backToListings = findViewById(R.id.btnBackToListings);

        // Get data from Intent
        Intent intent = getIntent();
        jobID = intent.getStringExtra("jobID");
        jobName = intent.getStringExtra("jobName");


        db = FirebaseSingleton.getInstance().getDb();
        employeeList = new ArrayList<>();
        employeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeList);
        employeeListView.setAdapter(employeeAdapter);
        loadPotentialEmployees();
        backToListings.setOnClickListener(v -> finish());
    }

    private void loadPotentialEmployees() {
        if (jobName != null && !jobName.isEmpty()) {
            db.collection("completedJobs")
                    // I do not know why it only works for jobId not JobName
                    .whereEqualTo("jobName", jobID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            Log.d("Debug", "Query Found Documents: " + count);
                            if (count > 0) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    String userID = document.getString("userId");
                                    Log.d("Debug", "User ID Found: " + userID);
                                    if (userID != null) {
                                        fetchEmployeeDetails(userID);
                                    }
                                }
                            } else {
                                Log.d("Debug", "No Matching Documents Found");
                            }
                        } else {
                            Log.e("Debug", "Query Error", task.getException());
                        }
                    });
        } else {
            Log.d("Debug", "Job Name is Empty or Null");
        }
    }




    private void fetchEmployeeDetails(String userID) {
        db.collection("user")
                .document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            String employeeName = document.getString("Name");
                            String email = document.getString("Email");

                            String employeeDetails = "Name: " + employeeName +
                                    "\nEmail: " + email;

                            Log.d("Debug", "Employee details: " + employeeDetails);

                            if (employeeName != null) {
                                employeeList.add(employeeDetails);
                                runOnUiThread(() -> employeeAdapter.notifyDataSetChanged());
                            }
                        } else {
                            Log.e("Debug", "Error fetching user details", task.getException());
                            Toast.makeText(EmployeeListActivity.this, "Error loading employee details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
