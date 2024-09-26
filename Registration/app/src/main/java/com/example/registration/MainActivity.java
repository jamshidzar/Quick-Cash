package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    // Declare UI elements
    private EditText emailInput;    // Input field for the email
    private EditText passwordInput; // Input field for the password
    private Button loginButton;     // Button to trigger the login process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the content view to the activity_main.xml layout
        setContentView(R.layout.activity_main);

        // Initialize the UI elements by linking them to the corresponding elements in the XML layout
        emailInput = findViewById(R.id.emailInput);        // Find the email input field by its ID
        passwordInput = findViewById(R.id.passwordInput);  // Find the password input field by its ID
        loginButton = findViewById(R.id.loginButton);      // Find the login button by its ID

        // Set a click listener on the login button to define what happens when the user clicks it
        loginButton.setOnClickListener(v -> {
            // Get the input values from the EditText fields and trim any extra spaces
            String email = emailInput.getText().toString().trim();    // Retrieve the email entered by the user
            String password = passwordInput.getText().toString().trim(); // Retrieve the password entered by the user

            // Simple validation to check if both fields are filled
            if (email.isEmpty() || password.isEmpty()) {
                // If either field is empty, display a message asking the user to fill both fields
                Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                // If both fields are filled, display a success message
                // In a real app, you would add the login logic here (like calling Firebase or a backend)
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}