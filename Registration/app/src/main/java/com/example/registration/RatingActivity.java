package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText feedbackEditText;
    private View submitRatingButton;

    private String jobName;
    private String employeeID;
    private String employerID;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);

        ratingBar = findViewById(R.id.ratingBar);
        feedbackEditText = findViewById(R.id.etFeedback);
        submitRatingButton = findViewById(R.id.btnSubmitRating);

        db = FirebaseSingleton.getInstance().getDb();

        jobName = getIntent().getStringExtra("jobName");
        employeeID = getIntent().getStringExtra("employeeID");
        employerID = getIntent().getStringExtra("employerID");
        submitRatingButton.setOnClickListener(v -> submitRating());
    }

    private void submitRating() {
        float rating = ratingBar.getRating();
        String feedback = feedbackEditText.getText().toString();

        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> ratingData = new HashMap<>();
//        ratingData.put("jobId", jobId);
//        ratingData.put("userId", userId);
        ratingData.put("rating", rating);
        ratingData.put("feedback", feedback);
        ratingData.put("employeeID", employeeID);
        ratingData.put("jobName", jobName);

        db.collection("ratings").add(ratingData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Rating submitted successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RatingActivity.this, CompletedListingsActivity.class);
                    intent.putExtra("userID", employerID);
                    startActivity(intent);
//                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to submit rating.", Toast.LENGTH_SHORT).show();
                });
    }
}
