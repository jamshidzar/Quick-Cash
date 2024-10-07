package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText emailInput;    // Input field for the email
    private EditText passwordInput; // Input field for the password
    private Button loginButton;     // Button to trigger the login process
    private Button registerButton;  // Button to navigate to the registration page

    // Declare Firebase Auth
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the content view to the activity_login.xml layout
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
         auth = FirebaseAuth.getInstance();

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
        UserCredential validator = new UserCredential();
        // Validate input fields
        if (validator.emailIsEmpty(email)) {
            // Display a message asking the user to enter the email
            Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
        } else if (validator.passwordIsEmpty(password)) {
            // Display a message asking the user to enter the password
            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Display a message if the email format is not valid
            Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else {
            // If all validations pass, proceed with login logic
            loginUser(email, password); // Call the loginUser method to authenticate with Firebase
        }
    }

    // Method for handling register button click
    private void onRegisterClick() {
        // Create an intent to navigate to MainActivity (Registration Page)
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // Method to log in the user with Firebase Authentication
    // Method to log in the user with Firebase Authentication
    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        // TODO: Navigate to the main app screen or user-specific dashboard

                    } else {
                        // If sign in fails, check the exception
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthException) {
                            FirebaseAuthException authException = (FirebaseAuthException) exception;

                            // Log the exact error code to identify the issue
                            String errorCode = authException.getErrorCode();
                            Log.d("FirebaseAuth", "Error code: " + errorCode);

                            // Handle specific error codes
                            if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                Toast.makeText(LoginActivity.this, "You don't have an account. Please register.", Toast.LENGTH_LONG).show();
                            } else if (errorCode.equals("ERROR_WRONG_PASSWORD")) {
                                Toast.makeText(LoginActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_LONG).show();
                            } else if (errorCode.equals("ERROR_OPERATION_NOT_ALLOWED")) {
                                Toast.makeText(LoginActivity.this, "Email/password accounts are disabled. Please enable in Firebase Console.", Toast.LENGTH_LONG).show();
                            } else {
                                // Display general authentication failure message
                                Toast.makeText(LoginActivity.this, "Authentication failed: " + authException.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Handle other types of exceptions, if any
                            Toast.makeText(LoginActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }





}
