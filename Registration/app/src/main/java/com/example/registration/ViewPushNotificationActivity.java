package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewPushNotificationActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private Button goBack;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_push_notification);
        Log.d("PushNotification", "onCreate: push notification activity started");
        init();
        setData();

        Intent jobList = new Intent(this, JobListActivity.class);

    }
    private void init(){
        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(notificationAdapter);
    }
    private void setData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Notification notification = new Notification(
                    extras.getString("title"),
                    extras.getString("body"),
                    extras.getString("job_id"),
                    extras.getString("jobLocation")
            );
            notificationList.add(notification);
            Log.d("PushNotification", "setData: the job id is "+ extras.getString("jobId"));
        }
        notificationAdapter.notifyDataSetChanged();
    }
}
