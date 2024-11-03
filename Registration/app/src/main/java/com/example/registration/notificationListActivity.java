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

    private RecyclerView notificationRecyclerView;
    private notificationAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);

        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        adapter = new notificationAdapter(getApplicationContext(), new ArrayList<>());
        notificationRecyclerView.setAdapter(adapter);

        fetchNotifications();
    }

    private void fetchNotifications() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Query for new and current jobs that the user has favorited
            db.collection("favorites")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            List<Job> notifications = new ArrayList<>();

                            for (DocumentSnapshot document : documents) {
                                String jobId = document.getString("jobId");
                                db.collection("jobs")
                                        .document(jobId)
                                        .get()
                                        .addOnCompleteListener(jobTask -> {
                                            if (jobTask.isSuccessful()) {
                                                Job job = jobTask.getResult().toObject(Job.class);
                                                /*if (job.getStatus().equals("new") || job.getStatus().equals("current")) {

                                                }*/
                                                notifications.add(job);
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "could not send notification", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
