package com.example.registration;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class CompletedListings extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView completedListingsRecyclerView;
    private String email;
    private String userID;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_listings);
    }
}
