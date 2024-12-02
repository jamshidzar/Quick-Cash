package com.example.registration;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RatingAdapter ratingAdapter; // Custom adapter
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize Firestore
        db = FirebaseSingleton.getInstance().getDb();

        // UI elements
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the user ID passed from the previous activity
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userID = sharedPref.getString("userId", null);
        //String userID = getIntent().getStringExtra("userId");

        if (userID != null && !userID.isEmpty()) {
            fetchAndDisplayRatings(userID);
        } else {
            Toast.makeText(this, "User ID not provided.", Toast.LENGTH_SHORT).show();
        }
    }

    public void fetchAndDisplayRatings(String userID) {
        // Fetch ratings where employeeID matches the given userID
        db.collection("ratings")
                .whereEqualTo("employeeID", userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Rating> ratings = new ArrayList<>();
                        float totalRating = 0;
                        int ratingCount = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String feedback = document.getString("feedback");
                            Double ratingValue = document.getDouble("rating");

                            if (ratingValue != null) {
                                ratings.add(new Rating(ratingValue.floatValue(), feedback));
                                totalRating += ratingValue;
                                ratingCount++;
                            }
                        }

                        // Calculate the average rating using the helper method
                        float averageRating = calculateAverageRating(totalRating, ratingCount);

                        // Display the average rating
                        TextView averageRatingTextView = findViewById(R.id.AverageRating);
                        averageRatingTextView.setText("Average Rating: " + averageRating);

                        // Display the ratings in the RecyclerView
                        if (ratings.isEmpty()) {
                            Toast.makeText(this, "No ratings found for this user.", Toast.LENGTH_SHORT).show();
                        } else {
                            ratingAdapter = new RatingAdapter(ratings);
                            recyclerView.setAdapter(ratingAdapter);
                        }
                    } else {
                        Toast.makeText(this, "Failed to fetch ratings.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to calculate the average rating
    private float calculateAverageRating(float totalRating, int ratingCount) {
        if (ratingCount > 0) {
            return totalRating / ratingCount;
        } else {
            return 0;
        }

    }
}
