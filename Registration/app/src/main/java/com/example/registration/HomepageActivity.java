package com.example.registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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
        Button logoutButton = findViewById(R.id.Logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomepageActivity.this);
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        performLogout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void performLogout() {
        // Perform logout actions, such as clearing stored user data
        // For example, clear shared preferences or Firebase auth sign-out
        // Clear user session and redirect to login screen
        Intent intent = new Intent(HomepageActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Finish current activity so the user can't go back
    }




}
