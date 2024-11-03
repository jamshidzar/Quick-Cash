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
    private Button jobApplyingButton;
    private FirebaseFirestore firestore;
    private String userId; // To store the user ID received from SharedPreferences
    private Button notificationButton;
    private static final int APPLY_JOB_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee);

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
        retrieveNotificationPreference().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Boolean isNotificationEnabled = task.getResult();
                if(isNotificationEnabled){
                    notificationButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Employee.this, notificationListActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    notificationButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), "Please Enable Notifications", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        notificationButton = findViewById(R.id.notificationButton);
        // Initialize "Job Listing" button and set it to start JobListActivity
        jobApplyingButton = findViewById(R.id.jobApplyingButton);
        jobApplyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EmployeeActivity", "Passing userId to JobListActivity: " + userId);
                // Pass userId to JobListActivity
                Intent intent = new Intent(Employee.this, JobListActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, APPLY_JOB_REQUEST); // Start JobListActivity for job listing
            }
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
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateNotificationPreference();
                    Toast.makeText(getApplicationContext(), "Job alerts enabled", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    // Method to update isNotificationEnabled to true
    public void updateNotificationPreference() {
        SharedPreferences sh = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = sh.getString("userId", "");
            Log.d("UserId", "User ID: " + userId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")  // Replace with your collection name
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get document reference
                                if(document.getId().equals(userId)){
                                    DocumentReference userRef = document.getReference();
                                    Log.d("UserRef", "User Document Reference: " + userRef.getPath());
                                    // Do something with userRef
                                    userRef.update("isNotificationEnabled", true);
                                    break;
                                }
                                else{
                                    Log.d("UserRef", "No matching document ID found for userId: " + userId);
                                }
                            }
                        } else {
                            Log.d("UserRef", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public Task<Boolean> retrieveNotificationPreference() {
        // Retrieve the userId from SharedPreferences
        SharedPreferences sh = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        String userId = sh.getString("userId", "");
        Log.d("UserId", "Retrieved User ID: " + userId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Boolean notification = false;
        db.collection("user")  // Replace with your collection name
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve the isNotificationEnabled field
                                Boolean isNotificationEnabled = document.getBoolean("isNotificationEnabled");
                                if (isNotificationEnabled != null) {
                                    taskCompletionSource.setResult(isNotificationEnabled);
                                    Log.d("NotificationStatus", "isNotificationEnabled: " + isNotificationEnabled);
                                }
                            } else {
                                taskCompletionSource.setResult(false);
                                taskCompletionSource.setResult(false);
                                Log.d("NotificationStatus", "No document found with ID: " + userId);
                            }
                        } else {
                            taskCompletionSource.setResult(false);
                            Log.d("NotificationStatus", "Error retrieving document: ", task.getException());
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }
}


