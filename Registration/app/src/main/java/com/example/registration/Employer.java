package com.example.registration;

import static android.content.ContentValues.TAG;

import static com.paypal.android.sdk.payments.PayPalPayment.PAYMENT_INTENT_SALE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;


public class Employer extends AppCompatActivity {

    // Database
    FirebaseFirestore db;

    Intent welcome;

    // UI Elements
    Button jobPosting;
    String email;
    Button completedListingsBtn;
    Button myListingsBtn;
    String userID;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;

    Button back;
// Code review by Jamshid Zar:
// Overall, the onCreate method is well-implemented and handles Firestore queries effectively.
// A few suggestions for improvement:
// - It's good that you're checking for a non-null and non-empty email before querying Firestore.
// - Consider adding a null check for the Intent to prevent potential crashes.
// - Avoid displaying sensitive information such as passwords and credit card details in a TextView for security reasons.
// - Error handling is good, but ensure the message in the else block references the correct data (you're checking for an email, not a name, so update the error message accordingly).
// - Good practice with logging and error handling for Firestore queries, but consider adding more robust error handling for user feedback.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empolyer);

        db = FirebaseSingleton.getInstance().getDb();
        welcome = getIntent();

        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userID = sharedPref.getString("userId", null);
        email = sharedPref.getString("Email", null);

        jobPosting = findViewById(R.id.button2);
        completedListingsBtn = findViewById(R.id.completedListings);
        myListingsBtn = findViewById(R.id.myListings);

        jobPosting.setOnClickListener(v -> onJobPostClick());
        completedListingsBtn.setOnClickListener(v-> goToCompletedListings());
        myListingsBtn.setOnClickListener(v-> goToMyListings());

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> goBackToHomePage());

    }

    protected void onJobPostClick(){
        Intent intent = new Intent(Employer.this, JobPosting.class);
        intent.putExtra("Email", email);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    protected void goToCompletedListings(){
        Intent intent = new Intent(Employer.this, CompletedListingsActivity.class);
        intent.putExtra("Email", email);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    protected void goToMyListings(){
        Intent intent = new Intent(Employer.this, JobList.class);
        intent.putExtra("Email", email);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
    protected void goBackToHomePage(){
        Intent intent = new Intent(Employer.this, HomepageActivity.class);
        intent.putExtra("Email", email);
        startActivity(intent);
    }

}
