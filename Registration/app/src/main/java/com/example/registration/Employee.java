package com.example.registration;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class Employee extends AppCompatActivity {
    private Button jobApplyingButton;

    private static final int APPLY_JOB_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee);

        // Initialize "Job Listing" button and set it to start JobListActivity
        jobApplyingButton = findViewById(R.id.jobApplyingButton);
        jobApplyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employee.this, JobListActivity.class);
                startActivityForResult(intent, APPLY_JOB_REQUEST); // Start JobListActivity for job listing
            }
        });
    }
    // Handle the result of job application (if you still need to pass data back to this page)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APPLY_JOB_REQUEST && resultCode == RESULT_OK) {
            // Job was applied successfully, show a confirmation if necessary
            Intent intent = new Intent(Employee.this, AppliedJobsActivity.class);
            startActivity(intent); // Redirect to the applied jobs page
        }
    }
    public boolean enableJobAlerts() {
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Job alerts enabled", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (checkBox.isChecked()) {
            return true;
        }
        return false;
    }


}


