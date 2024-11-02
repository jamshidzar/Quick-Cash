package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedListings extends AppCompatActivity {
    private FirebaseFirestore db;

    private RecyclerView completedListingsRecyclerView;
    private CompletedJobsAdapter completedJobsAdapter;
    private List<CompletedListing> completedJobsList;

    private String email;
    private String userID;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_listings);
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        userID = intent.getStringExtra("userID");
        db = FirebaseFirestore.getInstance();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> goToEmployerPage());

        completedJobsList = new ArrayList<>();
        completedJobsAdapter = new CompletedJobsAdapter(completedJobsList, this::onPayment);
        completedListingsRecyclerView = findViewById(R.id.completedListingRecyclerView);
        completedListingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        completedListingsRecyclerView.setAdapter(completedJobsAdapter);

        loadCompletedJobs();
    }

    private void loadCompletedJobs(){
        // Reference to the jobSeekers collection


        db.collectionGroup("jobSeekers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> jobSeekersIDs = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                jobSeekersIDs.add(document.getId());
            }
        }).addOnFailureListener(e -> {
            Log.e("FireStore", "Error getting ids");
        });
//        jobSeekersRef.document("Eu8s8XbK4ZYVnsLUmmr5SOrr7w22").get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    Log.d("Firestore", "Document exists: " + documentSnapshot.exists());
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("Firestore", "Error getting document", e);
//                });

    }

    private void onPayment(CompletedListing completedListing){

            String listingId = completedListing.getId();

            db.collection("jobSeekers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                               if (task.isSuccessful()) {
                                   Map<String, Object> allCompletedJobs = new HashMap<>();
                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                       String jobSeekerID = document.getId();
                                       db.collection("jobSeekers").document(jobSeekerID).collection("completedJobs").document(listingId)
                                               .get()
                                               .addOnSuccessListener(documentSnapshot -> {
                                                   if (documentSnapshot.exists()){
                                                       Map<String, Object> jobData = documentSnapshot.getData();
                                                       if (jobData.containsValue(userID)){
                                                           db.collection("jobSeekers").document(jobSeekerID).collection("completedJobs").document(listingId)
                                                                   .delete()
                                                                   .addOnSuccessListener(deleteFile -> {
                                                                       completedJobsList.remove(completedListing);
                                                                       completedJobsAdapter.notifyDataSetChanged();
                                                                       Toast.makeText(CompletedListings.this, "Payment made", Toast.LENGTH_SHORT).show();
                                                                   }).addOnFailureListener(e -> {
                                                                       Toast.makeText(CompletedListings.this, "Failed", Toast.LENGTH_SHORT).show();
                                                                   });
                                                       }
                                                   }
                                               });

                                   }
                               } else {
                                   Log.d("Firestore error", "Error getting documents", task.getException());
                               }
                           }
                       });
    }

    protected void goToEmployerPage(){
        Intent intent = new Intent(CompletedListings.this, Employer.class);
        intent.putExtra("Email", email);
        startActivity(intent);
    }
}

