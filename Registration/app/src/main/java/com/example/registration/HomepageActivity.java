package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.jar.Attributes;

public class HomepageActivity extends AppCompatActivity {
    Intent welcome;
    private String email;
    Button employee, employer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        employee = findViewById(R.id.employee);
        employer = findViewById(R.id.employer);
        welcome = getIntent();
        email = welcome.getStringExtra("Email");

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homePage = new Intent(HomepageActivity.this , Employee.class);
                homePage.putExtra("Email" , email);
                startActivity(homePage);
            }
        });

        employer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homePage = new Intent(HomepageActivity.this , Employer.class);
                homePage.putExtra("Email" , email);
                startActivity(homePage);
            }
        });
    }
}
