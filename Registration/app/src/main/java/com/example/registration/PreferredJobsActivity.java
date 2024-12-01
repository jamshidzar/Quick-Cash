package com.example.registration;


import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PreferredJobsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView listViewFavorites;
    private List<String> favoritesList;
    private List<String> documentIds; // To store document IDs for deletion
    private PreferredJobsAdapter adapter;
    private String userId; // User ID from SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_jobs);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize the ListView
        listViewFavorites = findViewById(R.id.ViewPreferredJobs);
        favoritesList = new ArrayList<>();
        documentIds = new ArrayList<>();
        adapter = new PreferredJobsAdapter(this, favoritesList, documentIds);
        listViewFavorites.setAdapter(adapter);

        // Get the userId passed from the Employee Activity
        userId = getIntent().getStringExtra("userId");

        // Check if userId is null
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID not found. Unable to load favorites.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load favorite jobs from the database
        loadFavoriteJobs();
    }

    private void loadFavoriteJobs() {
        // Query the "favorites" collection for the user's saved jobs
        db.collection("favorites")
                .whereEqualTo("userId", userId) // Filter jobs for the current user
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            favoritesList.clear(); // Clear the list before adding new items
                            documentIds.clear(); // Clear the document IDs
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String jobName = document.getString("jobName");
                                String location = document.getString("location");
                                String salary = document.getString("salary");

                                String jobDetails = "Job Name: " + jobName +
                                        "\nLocation: " + location +
                                        "\nSalary: $" + salary;

                                favoritesList.add(jobDetails);
                                documentIds.add(document.getId()); // Save the document ID for deletion
                            }
                            adapter.notifyDataSetChanged(); // Refresh the ListView
                        } else {
                            Toast.makeText(this, "No favorite jobs found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error loading favorite jobs.", Toast.LENGTH_SHORT).show();
                        Log.e("PreferredJobsActivity", "Error retrieving documents: ", task.getException());
                    }
                });
    }
}
