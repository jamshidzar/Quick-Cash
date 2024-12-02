package com.example.registration;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



public class Employee extends AppCompatActivity {
    private Button incomeHistoryButton;
    private Button jobApplyingButton;
    private Button mapButton; // Button to view map
    private Button btnViewAppliedJobs; // Button to view applied jobs
    private Button btnViewFavoriteJobs; // Button to view favorite jobs
    private Button btnViewPreferredJobs; // Button to view preferred jobs
    private Button viewMapButton;
    private Button viewProfile;
    private Button btnJobHistory;
    private FirebaseFirestore firestore;
    private  String userId ;// To store the user ID received from SharedPreferences
    private static final int APPLY_JOB_REQUEST = 1;

    // New button for viewing applied jobs
    private NotificationPreferenceManager notificationManager;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee);
        firestore = FirebaseFirestore.getInstance();
        availableJobsList = new ArrayList<>();
        notificationManager = new NotificationPreferenceManager(this);

        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPref.getString("userId", null);
        Log.d("EmployeeActivity", "Retrieved userId from SharedPreferences: " + userId);
        if (userId == null) {
            Toast.makeText(this, "User ID not found. Redirecting to login.", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(Employee.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        enableJobAlerts();
        notificationManager.retrieveNotificationPreference(userId).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Boolean isNotificationEnabled = task.getResult();
                if(isNotificationEnabled){
                    Toast.makeText(getApplicationContext(), "Notifications enabled, please select favorite jobs.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize "Job Listing" button and set it to start JobListActivity
        jobApplyingButton = findViewById(R.id.jobApplyingButton);
        mapButton = findViewById(R.id.ViewCurrentLocation);
        btnViewAppliedJobs = findViewById(R.id.btnViewAppliedJobs);
        btnViewPreferredJobs = findViewById(R.id.btnViewPreferredJobs); // New button for preferred jobs
        viewMapButton = findViewById(R.id.ViewMap);
        viewProfile = findViewById(R.id.Profile);
        btnJobHistory = findViewById(R.id.jobHistory);

        // Set listeners
        jobApplyingButton.setOnClickListener(v -> {
            Log.d("EmployeeActivity", "Passing userId to JobListActivity: " + userId);
            Intent intent = new Intent(Employee.this, JobListActivity.class);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, APPLY_JOB_REQUEST);
        });

        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(Employee.this, CurrentLocation.class);
            startActivity(intent);
        });

        btnViewAppliedJobs.setOnClickListener(v -> {
            Log.d("EmployeeActivity", "Opening Applied Jobs page for userId: " + userId);
            Intent intent = new Intent(Employee.this, AppliedJobsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        // New button for viewing preferred jobs
        btnViewPreferredJobs.setOnClickListener(v -> {
            if (userId == null || userId.isEmpty()) {
                Log.e("EmployeeActivity", "User ID is null or empty. Cannot open PreferredJobsActivity.");
                Toast.makeText(Employee.this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("EmployeeActivity", "Opening PreferredJobsActivity with userId: " + userId);

            // Navigate to PreferredJobsActivity and pass the userId
            Intent intent = new Intent(Employee.this, PreferredJobsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
        btnJobHistory.setOnClickListener(v -> {
                    Intent intent = new Intent(Employee.this, JobHistoryActivity.class);
                    intent.putExtra("userID", userId);
                    startActivity(intent);
                });

        // Enable job alerts
        enableJobAlerts();

        loadJobsFromFirestoreForMap();
        Button viewMapButton = findViewById(R.id.ViewMap);
        viewMapButton.setOnClickListener(v -> openMapView(availableJobsList));

        incomeHistoryButton = findViewById(R.id.incomeHistoryButton);
        incomeHistoryButton.setOnClickListener(v -> openIncomeHistory());

        viewProfile.setOnClickListener(v ->{
            Log.d("EmployeeActivity", "View profile with all the rating: " + userId);
            Intent intent = new Intent(Employee.this, ProfileActivity.class);
            intent.putExtra("userId", userId); // Pass userId to FavoriteJobsActivity
            startActivity(intent);
        });
    }



    // Handle the result of job application
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APPLY_JOB_REQUEST && resultCode == RESULT_OK) {
            Intent intent = new Intent(Employee.this, AppliedJobsActivity.class);
            intent.putExtra("userId", userId); // Pass userId to AppliedJobsActivity
            startActivity(intent);
        }
    }
    public void enableJobAlerts() {
        checkBox = findViewById(R.id.checkBox);
        SharedPreferences notiPref = getSharedPreferences("NotifyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = notiPref.edit();
        editor.putBoolean("notification", false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("UserID", "onCheckedChanged: passing userId to enable alerts "+ userId);
                if (isChecked) {
                    editor.putBoolean("notification", true);
                    notificationManager.updateNotificationPreference(userId);
                    Toast.makeText(getApplicationContext(), "Job alerts enabled", Toast.LENGTH_LONG).show();
                }
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
                    job.setId(document.getId()); // Setthe document ID

                    // Correct the field name to match Firestore's "PostalCode"
                    if (document.contains("postalCode")) {
                        job.setPostalCode(document.getString("postalCode"));
                    } else {
                        job.setPostalCode("N/A"); // Default value if postal code is missing
                    }
                    availableJobsList.add(job);
                }
//                jobAdapter.notifyDataSetChanged();
            } else {
                Log.e("FirestoreError", "Error fetching jobs: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
            }
        });
    }
    private void openMapView(List<Job> jobs) {
         Intent intent = new Intent(this, MapViewActivity.class);
         intent.putExtra("jobs", (ArrayList<Job>) jobs);
         startActivity(intent);
        }

    private void openIncomeHistory(){
        Intent intent = new Intent(Employee.this, IncomeHistoryActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }
}




