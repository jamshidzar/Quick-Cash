package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.jar.Attributes;

public class HomepageActivity extends AppCompatActivity {
    Intent welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Button employee = findViewById(R.id.employee);
        Button employer = findViewById(R.id.employer);

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcome = getIntent();
                String name = welcome.getStringExtra("Name");
                Intent homePage = new Intent(HomepageActivity.this , Employee.class);
                homePage.putExtra("Name" , name);
                startActivity(homePage);
            }
        });

        employer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcome = getIntent();
                String name = welcome.getStringExtra("Name");
                Intent homePage = new Intent(HomepageActivity.this , Employer.class);
                homePage.putExtra("Name" , name);
                startActivity(homePage);
            }
        });
    }
}
