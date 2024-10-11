package com.example.registration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Input fields for name, email, password and credit card
    EditText name, email, password, creditcard;

    // Text view for error message pop up on invalid registration
    TextView errMSG;

    // Buttons for registration and move to login page
    Button Registration, Login;

    // Database to store user info
    FirebaseFirestore db;

// Code review by Jamshid Zar:
// - The use of EdgeToEdge and WindowInsets is a nice touch for a modern UI.
// - Firestore initialization is correctly placed and UI elements are appropriately linked.
// - Button functionality for both Registration and Login is well implemented.
// - Credential validation logic is solid; however, consider breaking down large blocks of validation code into smaller helper methods for readability.
// - It's good that you use `Toast` for success/failure feedback, but you may want to handle potential errors in more detail (e.g., network failure).
// - Suggestion: Adding a progress bar during Firestore operations could improve user experience.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize firestore database and UI elements
        db = FirebaseFirestore.getInstance();
        name = findViewById(R.id.NameText);
        email = findViewById(R.id.EmailText);
        password = findViewById(R.id.PasswordText);
        creditcard = findViewById(R.id.CreditCard);
        Registration = findViewById(R.id.RegisterButton);
        Login = findViewById(R.id.goToLoginButton);
        errMSG = findViewById(R.id.errorMSG);

        // Call method to set up button that takes you to login page
        setUpLoginButton();

        // Set up registration button
        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get text input fields
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String CreditCard = creditcard.getText().toString();

                // Create credential validator object
                CredentialValidator validator = new CredentialValidator();
                String errorMessage = new String();

                // Boolean to keep track of valid registration
                boolean validRegistration = true;

                // Checking for any invalid credentials and setting appropriate error message
                if (!Name.isEmpty()) {
                    if (!validator.isValidName(Name)) {
                        validRegistration = false;
                        errorMessage = getResources().getString(R.string.INVALID_NAME).trim();
                    }
                }
                else{
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.EMPTY_NAME).trim();
                }

                if (!Email.isEmpty()) {
                    if (!validator.isValidEmail(Email)) {
                        validRegistration = false;
                        errorMessage = getResources().getString(R.string.INVALID_EMAIL).trim();
                    }
                }
                else{
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.EMPTY_EMAIL).trim();
                }

                if (!Password.isEmpty()) {
                    if (!validator.isValidPassword(Password)) {
                        validRegistration = false;
                        errorMessage = getResources().getString(R.string.INVALID_PASSWORD).trim();
                    }
                }
                else{
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.EMPTY_PASSWORD).trim();
                }

                if (!CreditCard.isEmpty()) {
                    if (!validator.isValidCreditCard(CreditCard)) {
                        validRegistration = false;
                        errorMessage = getResources().getString(R.string.INVALID_CREDIT_CARD).trim();
                    }
                }
                else{
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.EMPTY_CREDITCARD).trim();
                }

                // Call method to make error message appear
                setErrorMessage(errorMessage);

                // If registration is valid, submit data to firestore database and move to login page
                if (validRegistration) {
                    setErrorMessage(errorMessage);
                    errMSG.setVisibility(View.INVISIBLE);

                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", Name);
                    user.put("Email", Email);
                    user.put("Password", Password);
                    user.put("Credit Card", CreditCard);

                    db.collection("user").add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                }
                            });

                    moveToLoginPage();
                }
            }
        });

    }






// Code review by Jamshid Zar:
// - This method effectively sets the error message, and it’s a nice touch to customize the error message UI (background color, padding, etc.).
// - However, consider setting default styles for error messages in XML instead of hardcoding these properties in Java for easier UI management.
// - Suggestion: You might want to clear the error message or hide it when the input is corrected by the user.






    // Method to set up the error message and its UI element
    protected void setErrorMessage(String message){
        TextView errorMessage = findViewById(R.id.errorMSG);
        errorMessage.setText(message.trim());
        errorMessage.setTextSize(16);
        errorMessage.setBackgroundColor(Color.RED);
        errorMessage.setTextColor(Color.WHITE);
        errorMessage.setPadding(30, 10, 30, 10);
    }




// Code review by Jamshid Zar:
// - A straightforward method for transitioning to the login page after successful registration.
// - It might be worth adding `finish()` to this method, so the user can't return to the registration page using the back button after logging in.


    // Login button setup
    protected void setUpLoginButton(){
        Login.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }

    // Method to move you to login page after successful registration
    protected void moveToLoginPage(){
        MainActivity.this.startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

}