package com.example.registration;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class Employee extends AppCompatActivity {
    private Button incomeHistoryButton, jobApplyingButton, mapButton, btnViewAppliedJobs, btnViewPreferredJobs, viewMapButton, viewProfile, btnJobHistory;
    private CheckBox checkBox;
    private FirebaseFirestore firestore;
    private String userId;
    private NotificationPreferenceManager notificationManager;
    private List<Job> availableJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee);

        firestore = FirebaseFirestore.getInstance();
        notificationManager = new NotificationPreferenceManager(this);
        availableJobsList = new ArrayList<>();

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getString("userId", null);

        if (userId == null) {
            Toast.makeText(this, "User ID not found. Redirecting to login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Employee.this, LoginActivity.class));
            finish();
            return;
        }

        initializeUIComponents();
        enableJobAlerts();
        setButtonListeners();
        loadJobsFromFirestoreForMap();
    }

    private void initializeUIComponents() {
        jobApplyingButton = findViewById(R.id.jobApplyingButton);
        mapButton = findViewById(R.id.ViewCurrentLocation);
        btnViewAppliedJobs = findViewById(R.id.btnViewAppliedJobs);
        btnViewPreferredJobs = findViewById(R.id.btnViewPreferredJobs);
        viewMapButton = findViewById(R.id.ViewMap);
        viewProfile = findViewById(R.id.Profile);
        btnJobHistory = findViewById(R.id.jobHistory);
        incomeHistoryButton = findViewById(R.id.incomeHistoryButton);
        checkBox = findViewById(R.id.checkBox);
    }

    private void setButtonListeners() {
        jobApplyingButton.setOnClickListener(v -> {
            Intent intent = new Intent(Employee.this, JobListActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        mapButton.setOnClickListener(v -> startActivity(new Intent(Employee.this, CurrentLocation.class)));

        btnViewAppliedJobs.setOnClickListener(v -> {
            Intent intent = new Intent(Employee.this, AppliedJobsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnViewPreferredJobs.setOnClickListener(v -> {
            Intent intent = new Intent(Employee.this, PreferredJobsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnJobHistory.setOnClickListener(v -> {
            Intent intent = new Intent(Employee.this, JobHistoryActivity.class);
            intent.putExtra("userID", userId);
            startActivity(intent);
        });

        viewMapButton.setOnClickListener(v -> openMapView(availableJobsList));

        incomeHistoryButton.setOnClickListener(v -> openIncomeHistory());

        viewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Employee.this, ProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private void openIncomeHistory() {
        Intent intent = new Intent(Employee.this, IncomeHistoryActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }

    public void enableJobAlerts() {
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                notificationManager.updateNotificationPreference(userId);
                Toast.makeText(this, "Job alerts enabled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadJobsFromFirestoreForMap() {
        CollectionReference jobsRef = firestore.collection("job");
        jobsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                availableJobsList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Job job = document.toObject(Job.class);
                    job.setId(document.getId());
                    job.setPostalCode(document.getString("postalCode"));
                    availableJobsList.add(job);
                }
            } else {
                Log.e("FirestoreError", "Error fetching jobs");
            }
        });
    }

    private void openMapView(List<Job> jobs) {
        Intent intent = new Intent(this, MapViewActivity.class);
        intent.putExtra("jobs", (ArrayList<Job>) jobs);
    }// Ensure Job implements Serializable or Parcelable    startActivity(intent);}

}






