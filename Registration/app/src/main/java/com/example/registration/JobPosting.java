package com.example.registration;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the ability for employers to post jobs and stores them in the FireBase database.
 */
public class JobPosting extends AppCompatActivity {
    private static final Log log = LogFactory.getLog(JobPosting.class);
    private String email;
    private String userID;
    private String errorMessage;
    private EditText jobName;
    private EditText location;
    private EditText postalCode;
    private EditText duration;
    private EditText urgency;
    private EditText salary;
    private Button postButton;
    private FirebaseFirestore db;
    private SendNotification sendNotification;

    /**
     * This method is called when the activity is created. It initializes the database and all UI
     * elements.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_posting);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        userID = intent.getStringExtra("userID");
        jobName = findViewById(R.id.jobName);
        location = findViewById(R.id.location);
        postalCode = findViewById(R.id.postalCode);
        duration = findViewById(R.id.duration);
        urgency = findViewById(R.id.urgency);
        salary = findViewById(R.id.salary);
        postButton = findViewById(R.id.postButton);
        sendNotification = new SendNotification(getApplicationContext());
        postButton.setOnClickListener(
                v -> postJob()
        );
    }
    /**
     * This method takes all of the user input for a given job and, when the user clicks on the
     * button to post a job, it stores all of the details in the database, then takes the user
     * to their listings.
     */
    protected Task<Boolean> postJob(){
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        String jobNameText = jobName.getText().toString();
        String locationText = location.getText().toString();
        String postalCodeText = postalCode.getText().toString();
        String durationText = duration.getText().toString();
        String urgencyText = urgency.getText().toString();
        String salaryText = salary.getText().toString();
        Map<String, Object> jobPost = new HashMap<>();
        jobPost.put("jobName", jobNameText);
        jobPost.put("location", locationText);
        jobPost.put("postalCode", postalCodeText);
        jobPost.put("duration", durationText);
        jobPost.put("urgency", urgencyText);
        jobPost.put("salary", salaryText);
        jobPost.put("employerID", userID);
        if (jobNameText.isEmpty()) {
            errorMessage = "Job name cannot be blank";
            setStatusMessage(errorMessage);
            return Tasks.forResult(false);
        }

        else if (locationText.isEmpty()) {
            errorMessage = "Location cannot be blank";
            setStatusMessage(errorMessage);
            return Tasks.forResult(false);
        }
        else if(postalCodeText.isEmpty()){
            errorMessage = "Postal Code cannot be blank";
            setStatusMessage(errorMessage);
            return Tasks.forResult(false);
        }
        else if (durationText.isEmpty()) {
            errorMessage = "Duration cannot be blank";
            setStatusMessage(errorMessage); // Display error message
            return Tasks.forResult(false);
        }
        else if (urgencyText.isEmpty()) {
            errorMessage = "Urgency cannot be blank";
            setStatusMessage(errorMessage); // Display error message
            return Tasks.forResult(false);
        }
        else if (salaryText.isEmpty()) {
            errorMessage = "Salary cannot be blank";
            setStatusMessage(errorMessage); // Display error message
            return Tasks.forResult(false);
        }else{
            errorMessage = "Successful";
            setStatusMessage(errorMessage);
        }
        db.collection("job").add(jobPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(JobPosting.this, "Job Posting Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(JobPosting.this, JobList.class);
                        intent.putExtra("Email", email);
                        intent.putExtra("userID", userID);
                        taskCompletionSource.setResult(true);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setResult(false);
                        Toast.makeText(JobPosting.this, "Job Posting Failed", Toast.LENGTH_LONG).show();
                    }
                });

        taskCompletionSource.getTask().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    android.util.Log.d("sendNotification", "onComplete: send job notification was called");
                    sendNotification.sendJobNotification(getNewJob());
                }
            }
        });
        return taskCompletionSource.getTask();
    }
    private Job getNewJob(){
        Job newJob = new Job();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("job")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        for (DocumentSnapshot document : value) {
                            if (document.exists()) {
                                // Access document fields
                                String duration = document.getString("duration");
                                String employerID = document.getString("employerID");
                                String jobName = document.getString("jobName");
                                String location = document.getString("location");
                                String postalCode = document.getString("postalCode");
                                String salary =  document.getString("salary");
                                String urgency = document.getString("urgency");
                                //Job job = new Job(jobName, employerID, location, duration, salary, urgency, postalCode);
                                newJob.setLocation(location);
                                newJob.setId(document.getId());
                            }
                        }
                    }
                });
        return newJob;
    }
    private void setStatusMessage(String message) {
        TextView errorMSG = findViewById(R.id.errorMessage);
        errorMSG.setVisibility(View.VISIBLE);
        errorMSG.setText(message);
    }

}


