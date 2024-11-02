package com.example.registration;

import static android.content.ContentValues.TAG;

import static com.paypal.android.sdk.payments.PayPalPayment.PAYMENT_INTENT_SALE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
    Button payEmployee;
    Button completedListingsBtn;
    EditText paymentAmount;
    String userID;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;

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

        db = FirebaseFirestore.getInstance();
        welcome = getIntent();
        email = welcome.getStringExtra("Email");

        jobPosting = findViewById(R.id.button2);
        payEmployee = findViewById(R.id.payEmployeeBtn);
        paymentAmount = findViewById(R.id.payAmt);
        completedListingsBtn = findViewById(R.id.completedListings);

        jobPosting.setOnClickListener(v -> onJobPostClick());
        completedListingsBtn.setOnClickListener(v-> goToCompletedListings());

        configPayPal();
        initActivityLauncher();
        setPaymentListener();


        if (email != null && !email.isEmpty()) {
            db.collection("user").whereEqualTo("Email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                    String name = documentSnapshot.getString("Name");
                                    String password = documentSnapshot.getString("Password");
                                    String creditCard = documentSnapshot.getString("Credit Card");
                                    String ID = documentSnapshot.getId();
                                    String message = "Name: " + name + "\nEmail: " + email + "\nPassword: " + password + "\nCredit Card: " + creditCard;
                                    TextView tv = findViewById(R.id.employerText);
                                    tv.setText(message);
                                } else {
                                    TextView tv = findViewById(R.id.employerText);
                                    tv.setText("No user found with the email: " + email);
                                }
                            } else {
                                TextView tv = findViewById(R.id.employerText);
                                tv.setText("Error retrieving user data.");
                            }
                        }
                    });
        } else {
            TextView tv = findViewById(R.id.employerText);
            tv.setText("No name passed to the Employer activity.");
        }

        getUserID(id -> {
            if (id != null){
                userID = id;
                Log.d("JobPosting", "User ID retrieved: " + userID);
            }
            else{
                Log.d("Firestore", "User not found.");
            }
        });
    }

    protected void onJobPostClick(){
        Intent intent = new Intent(Employer.this, JobPosting.class);
        intent.putExtra("Email", email);
        startActivity(intent);
//        jobPosting.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), JobPosting.class).putExtra("Email", email)));
    }

    protected void goToCompletedListings(){
        Intent intent = new Intent(Employer.this, CompletedListings.class);
        intent.putExtra("Email", email);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void configPayPal(){
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(getResources().getString(R.string.PAYPAL_ID).trim());
    }
    
    private void initActivityLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK){
                final PaymentConfirmation confirmation = result.getData().getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Log.i(TAG, paymentDetails);

                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        Toast.makeText(Employer.this, String.format("Payment %s%n with payment id is %s", state, payID), Toast.LENGTH_LONG).show();
//                        paymentStatusTV.setText(String.format("Payment %s%n with payment id is %s", state, payID));
                    } catch (JSONException e){
                        Log.e("Error", "An extremely unlikely failure occurred... ", e);
                    }
                }
            } else if (result.getResultCode() == PaymentActivity.RESULT_EXTRAS_INVALID){
                Log.d(TAG, "Launcher Result Invalid");
            } else if (result.getResultCode() == PaymentActivity.RESULT_CANCELED){
                Log.d(TAG, "Launcher Result Cancelled");
            }
        });
    }

    private void setPaymentListener() {
        payEmployee.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        final String amount = paymentAmount.getText().toString();
        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), "CAD", "Purchase Goods", PAYMENT_INTENT_SALE);

        final Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        activityResultLauncher.launch(intent);
    }

    protected void getUserID(JobPosting.FirestoreCallBack callback){

        db.collection("user").whereEqualTo("Email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String userID = documentSnapshot.getId();
                                callback.onCallBack(userID);
                            } else {
                                callback.onCallBack(null);
                            }
                        }
                    }
                });
    }

    public interface FirestoreCallBack{
        void onCallBack(String userID);
    }
}
