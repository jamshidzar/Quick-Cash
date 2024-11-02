//package com.example.registration;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import java.util.ArrayList;
//import java.util.List;
//
//public class JobViewActivity extends AppCompatActivity {
//    private ListView jobListView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.item_job); // Ensure this points to the correct layout for JobViewActivity
//
//        // Initialize ListView and Button
//        //jobListView = findViewById(R.id.jobListView);
//        Button viewMapButton = findViewById(R.id.ViewMap);
//
//        // Retrieve the list of jobs passed from JobListActivity
//        List<Job> jobs = (List<Job>) getIntent().getSerializableExtra("jobs");
//
//        // Set up an ArrayAdapter to display job names in the ListView
//        ArrayAdapter<Job> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobs);
//        jobListView.setAdapter(adapter);
//
//        // Set up button to view map
//        viewMapButton.setOnClickListener(v -> openMapView(jobs));
//    }
//
//    // Method to open MapViewActivity and pass the job data
//    private void openMapView(List<Job> jobs) {
//        Intent intent = new Intent(this, MapViewActivity.class);
//        intent.putExtra("jobs", (ArrayList<Job>) jobs);
//        startActivity(intent);
//    }
//}
