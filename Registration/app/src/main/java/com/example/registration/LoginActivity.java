package com.example.registration;
/*
* Code review done by Tanishika:
* Get rid of unused library dependencies
* add string resources for error handing.
* */
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;




/**
 * This class handles the login functionality of the application.
 * It allows users to enter their credentials (email and password), validates them,
 * and then attempts to authenticate the user with Firestore.
 * If the user does not have an account, they can navigate to the registration page.
 *
 * @author Jamshid Zar
 */

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText emailInput;    // Input field for the email
    private EditText passwordInput; // Input field for the passwordgit
    private Button loginButton;     // Button to trigger the login process
    private Button registerButton;  // Button to navigate to the registration page

    // Declare Firestore
    private FirebaseFirestore db;




    /**
     * This method is called when the activity is created.
     * It initializes Firestore and the UI elements (email, password fields, and buttons),
     * and sets up click listeners for the login and register buttons.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     * @author Jamshid Zar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the content view to the activity_login.xml layout
        setContentView(R.layout.activity_login);

        // Initialize Firestore
        db = FirebaseSingleton.getInstance().getDb();

        // Initialize the UI elements by linking them to the corresponding elements in the XML layout
        emailInput = findViewById(R.id.emailInput);        // Find the email input field by its ID
        passwordInput = findViewById(R.id.passwordInput);  // Find the password input field by its ID
        loginButton = findViewById(R.id.loginButton);      // Find the login button by its ID
        registerButton = findViewById(R.id.registerButton); // Find the register button by its ID

        // Set click listeners for login and register buttons
        loginButton.setOnClickListener(v -> onLoginClick());
        registerButton.setOnClickListener(v -> onRegisterClick());
    }


    /**
     * This method is called when the login button is clicked.
     * It validates the email and password fields, checks if the input is correct,
     * and if valid, proceeds to log in the user by calling the loginUser method.
     *
     * @author Jamshid Zar
     */
    protected void onLoginClick() {
        String email = emailInput.getText().toString().trim(); // Retrieve the email entered by the user
        String password = passwordInput.getText().toString().trim(); // Retrieve the password entered by the user
        UserCredential validator = new UserCredential();
        String error = new String();
        // Validate input fields
        if (validator.emailIsEmpty(email)) {
            // Display a message asking the user to enter the email
            error = "Please enter Email";
        } else if (validator.passwordIsEmpty(password)) {
            // Display a message asking the user to enter the password
            error = "Please enter password";
        } else if (!validator.emailPattern(email)) {
            // Display a message if the email format is not valid
            error = "Invalid email";
        } else {
            // If all validations pass, proceed with login logic
            loginUser(email, password); // Call the loginUser method to authenticate with Firestore
        }
        setStatusMessage(error);
    }

    /**
     * This method is called when the register button is clicked.
     * It navigates the user to the registration page (MainActivity).
     *
     * @author Jamshid Zar
     */
    protected void onRegisterClick() {
        // Create an intent to navigate to MainActivity (Registration Page)
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

        startActivity(intent);
    }



    /**
     * This method attempts to log in the user by querying the Firestore database.
     * It checks if the user with the given email and password exists. If authentication is successful,
     * the user is navigated to the homepage. If not, an error message is displayed.
     *
     * @param email    The email entered by the user.
     * @param password The password entered by the user.
     * @author Jamshid Zar
     */
    protected void loginUser(String email, String password) {
        db.collection("user")
                .whereEqualTo("Email", email)
                .whereEqualTo("Password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String userId = task.getResult().getDocuments().get(0).getId();
                        Log.d("LoginActivity", "User found with userId: " + userId);

                        // Store userId in SharedPreferences
                        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("userId", userId);
                        editor.putString("Email", email);
                        editor.apply();

                        // Navigate to HomepageActivity
                        Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * This method updates the status message on the UI.
     * It displays error or success messages to the user.
     *
     * @param message The message to be displayed.
     * @author Jamshid Zar
     */
    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message.trim());
    }
}






