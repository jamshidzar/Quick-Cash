package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class notificationListActivity extends AppCompatActivity {
    private TextView jobTitle;
    private TextView company;
    private Button apply;
    protected List<Job> jobs;
    protected  List<Job> preferredJobs;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);
        init();
        jobs = new ArrayList<>();
        preferredJobs = new ArrayList<>();
        getJobPosting();
        getPreferredJobs();
    }
    private void init(){
        jobTitle = findViewById(R.id.jobTitleNotification);
        company = findViewById(R.id.companyNameNotification);
        apply = findViewById(R.id.applyButtonNotification);
    }
    protected void getJobPosting() {
        firestore.collection("job").get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                jobs.clear();  // Clear the list to prevent duplicates
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Job job = document.toObject(Job.class);
                    job.setId(document.getId()); // Optionally set document ID in the Job object
                    jobs.add(job); // Add the Job object to the list
                }
            } else {
                Log.e("FirestoreError", "Error fetching jobs: " +
                        (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
            }
        });
    }
    protected void getPreferredJobs(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            firestore.collection("jobSeekers").document(userId)
                    .collection("favourites")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        preferredJobs.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Job job = document.toObject(Job.class);
                            preferredJobs.add(job);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load applied jobs.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }
    /*protected void notifyUser(List<Job> jobs,List<Job> preferredJobs){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        for(int i = 0; i < jobs.size(); i++){
            for(int j = 0; j < preferredJobs.size(); j++){
                if(jobs.get(i).getLocation().matches())
            }
        }
    }*/
}
