package com.example.registration;

import static android.content.ContentValues.TAG;

import static com.paypal.android.sdk.payments.PayPalPayment.PAYMENT_INTENT_SALE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used such that employers can view all of the listings their employees have
 * completed. Additionally, it allows the employers to directly pay their employees for each listing
 * using PayPal.
 */
public class CompletedListingsActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private RecyclerView completedListingsRecyclerView;
    private CompletedJobsAdapter completedJobsAdapter;
    private CompletedJobsAdapter ratingsAdapter;
    private List<CompletedListing> completedJobsList;

    private String email;
    private String userID;
    private Button backBtn;
    private String employeeID;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private PayPalConfiguration payPalConfig;

    /**
     * This method is called when the activity is created. It initializes the database and all UI
     * elements, as well as the PayPal integration.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_listings);
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        userID = intent.getStringExtra("userID");
        employeeID=intent.getStringExtra("employeeID");
        db = FirebaseFirestore.getInstance();


        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> goToEmployerPage());

        completedJobsList = new ArrayList<>();
        completedJobsAdapter = new CompletedJobsAdapter(completedJobsList, this::onPayment, this::onRating,this);
        completedListingsRecyclerView = findViewById(R.id.completedListingRecyclerView);
        completedListingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        completedListingsRecyclerView.setAdapter(completedJobsAdapter);

        loadCompletedJobs();

        configPayPal();
        initActivityLauncher();

    }

    /**
     * This method loads all of the completed jobs associated with the employer currently logged in,
     * making them visible on the page.
     */
    private void loadCompletedJobs(){
        db.collection("completedJobs").whereEqualTo("employerID", userID)
                .whereEqualTo("paymentStatus", "Not Paid")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    completedJobsList.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        CompletedListing completedListing = new CompletedListing();

                        completedListing.setId(document.getString("jobId"));
                        completedListing.setJobName(document.getString("jobName"));
                        completedListing.setEmployerID(document.getString("employerID"));
                        completedListing.setLocation(document.getString("location"));
                        completedListing.setDuration(document.getString("duration"));
                        completedListing.setSalary(document.getString("salary"));
                        completedListing.setUrgency(document.getString("urgency"));
                        completedListing.setPostalCode(document.getString("postalCode"));
                        completedListing.setStatus(document.getString("status"));

                        completedJobsList.add(completedListing);
                    }
                    completedJobsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load applied jobs.", Toast.LENGTH_SHORT).show();
                });

    }

    private void updateEmployeeTotalIncome(String employeeID, String addToTotal){

        db.collection("user").document(employeeID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.exists()){
                        final String currIncome = (String) querySnapshot.get("Total Income");
                        int newTotalIncome = Integer.parseInt(currIncome) + Integer.parseInt(addToTotal);
                        db.collection("user").document(employeeID)
                                .update("Total Income", String.valueOf(newTotalIncome))
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Fields successfully updated"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating fields", e));
                    }
                });
    }
    /**
     * This method is called when an Employer wants to pay on employee for their completed
     * listing. It extracts the total amount needed to be paid from the listing then
     * initiates the PayPal activity.
     *
     * @param completedListing this is the completed listing that the employer will be paying for
     */
    private void onPayment(CompletedListing completedListing){

        String listingId = completedListing.getId();

        db.collection("completedJobs")
                .whereEqualTo("jobId", listingId)
                .whereEqualTo("employerID", userID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()){
                        DocumentSnapshot completedJobListing = querySnapshot.getDocuments().get(0);
                        String docID = completedJobListing.getId();

                        db.collection("completedJobs")
                                .document(docID)
                                .update("paymentStatus", "Paid")
                                .addOnSuccessListener(aVoid -> {
                                    completedJobsList.remove(completedListing);
                                    completedJobsAdapter.notifyDataSetChanged();
                                    Log.d("FirestoreUpdate", "Field updated successfully for document: " + docID);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirestoreUpdate", "Error updating field for document: " + docID, e);
                                });
//                        completedJobListing.getReference().delete()
//                                .addOnSuccessListener(deleteVoid -> {
//                                    completedJobsList.remove(completedListing);
//                                    completedJobsAdapter.notifyDataSetChanged();
//                                });
                        Map<String, Object> completedJobData = completedJobListing.getData();
                        final String employeeID = (String) completedJobData.get("userId");
                        final String salary = (String) completedJobData.get("salary");
                        final String hours = (String) completedJobData.get("duration");
                        final int totalPay = Integer.parseInt(salary) * Integer.parseInt(hours);
                        final PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalPay), "CAD", "Purchase Goods", PAYMENT_INTENT_SALE);
                        updateEmployeeTotalIncome(employeeID, String.valueOf(totalPay));
                        final Intent intent = new Intent(this, PaymentActivity.class);
                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                        activityResultLauncher.launch(intent);
                    }
                });
    }

    private void onRating(CompletedListing completedListing){
        String listingID = completedListing.getId();

        db.collection("completedJobs")
                .whereEqualTo("jobId", listingID)
                .whereEqualTo("employerID", userID)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()){
                        DocumentSnapshot completedJobListing = querySnapshot.getDocuments().get(0);
                        Map<String, Object> completedJobData = completedJobListing.getData();
                        String employeeID = (String) completedJobData.get("userId");
                        String jobName = (String) completedJobData.get("jobName");

                        Intent intent = new Intent(CompletedListingsActivity.this, RatingActivity.class); // Navigate to RatingActivity
                        intent.putExtra("jobName", jobName); // Pass jobId or other relevant data
                        intent.putExtra("employeeID", employeeID);
                        intent.putExtra("employerID", userID);
                        startActivity(intent);
                    }
                });
    }

    /**
     *
     */
    protected void goToEmployerPage(){
        Intent intent = new Intent(CompletedListingsActivity.this, Employer.class);
        intent.putExtra("Email", email);
        startActivity(intent);
    }

    /**
     * Configures the PayPal. I.e. we are defining that we are using the SANDBOX environment
     * and then setting the PayPal client ID
     */
    private void configPayPal(){
        payPalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(getResources().getString(R.string.PAYPAL_ID).trim());
    }

    /**
     * Creating the activity launcher for PayPal
     */
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
                        Toast.makeText(CompletedListingsActivity.this, String.format("Payment %s%n with payment id is %s", state, payID), Toast.LENGTH_LONG).show();
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
}


