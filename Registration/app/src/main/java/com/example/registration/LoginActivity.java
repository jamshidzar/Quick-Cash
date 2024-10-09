package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText emailInput;    // Input field for the email
    private EditText passwordInput; // Input field for the passwordgit
    private Button loginButton;     // Button to trigger the login process
    private Button registerButton;  // Button to navigate to the registration page

    // Declare Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the content view to the activity_login.xml layout
        setContentView(R.layout.activity_login);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize the UI elements by linking them to the corresponding elements in the XML layout
        emailInput = findViewById(R.id.emailInput);        // Find the email input field by its ID
        passwordInput = findViewById(R.id.passwordInput);  // Find the password input field by its ID
        loginButton = findViewById(R.id.loginButton);      // Find the login button by its ID
        registerButton = findViewById(R.id.registerButton); // Find the register button by its ID

        // Set click listeners for login and register buttons
        loginButton.setOnClickListener(v -> onLoginClick());
        registerButton.setOnClickListener(v -> onRegisterClick());
    }

    // Method for handling login button click
    private void onLoginClick() {
        String email = emailInput.getText().toString().trim(); // Retrieve the email entered by the user
        String password = passwordInput.getText().toString().trim(); // Retrieve the password entered by the user

        // Validate input fields
        if (email.isEmpty()) {
            // Display a message asking the user to enter the email
            Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            // Display a message asking the user to enter the password
            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Display a message if the email format is not valid
            Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else {
            // If all validations pass, proceed with login logic
            loginUser(email, password); // Call the loginUser method to authenticate with Firestore
        }
    }

    // Method for handling register button click
    private void onRegisterClick() {
        // Create an intent to navigate to MainActivity (Registration Page)
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        startActivity(intent);
    }

    // Method to log in the user with Firestore
    private void loginUser(String email, String password) {
        // Query Firestore collection "user" to find a document with the matching email
        db.collection("user")
                .whereEqualTo("Email", email)
                .whereEqualTo("Password", password) // You should hash passwords for security
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean userExists = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // If document is found, user is authenticated
                                userExists = true;
                                break;
                            }
                            if (userExists) {
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                // TODO: Navigate to the main app screen or user-specific dashboard
                            } else {
                                Toast.makeText(LoginActivity.this, "You don't have an account. Please register.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If task failed
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //HomepageActivity
    }
}









