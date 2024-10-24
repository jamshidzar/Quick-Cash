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
    private String email;
    Button employee, employer;
    @Override

// Code review by Jamshid Zar:
// This code looks well-structured and achieves the expected functionality. A few suggestions:
// - Good job setting up the employee and employer buttons with appropriate intents to pass the email.
// - Consider adding null checks for the `email` variable to ensure it is not null before using it in intents.
// - The logout process is clear and well-implemented. Nice use of the confirmation dialog to ensure the user wants to log out.
// - In the `performLogout` method, clearing user session or data like shared preferences can be added if necessary, especially if handling sensitive data.
// - Everything else looks good and well-organized.
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
        Button logoutButton = findViewById(R.id.Logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
    }


// Code review by Jamshid Zar:
// - The use of `AlertDialog` to confirm the logout is good practice and ensures the user doesn't accidentally log out.
// - The `setCancelable(false)` option is well-chosen to prevent the user from accidentally dismissing the dialog by touching outside of it.
// - One suggestion: Consider using string resources for the dialog message ("Are you sure you want to log out?") instead of hardcoding it. This will make it easier to manage translations or updates to the message.
// - The use of positive and negative buttons is clear, and calling `performLogout()` on positive response is effective.

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
// Code review by Jamshid Zar:
// - This method handles the logout process effectively by clearing the activity stack with the correct `Intent` flags.
// - You provide a useful comment on the possibility of clearing stored user data or Firebase authentication; consider implementing this if your app uses sessions or Firebase Auth to manage user sessions.
// - Using `finish()` to prevent the user from navigating back to the homepage is a good touch.
// - Suggestion: You might want to consider adding logic to clear any user-specific data (such as preferences or cache) if applicable before logging out.


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
