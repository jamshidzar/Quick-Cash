package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Employer extends AppCompatActivity {
    FirebaseFirestore db;
    Intent welcome;
    Button jobPosting;
    String email;

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
        setUpJobPostingButton();

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
    }

    protected void setUpJobPostingButton(){
        jobPosting.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), JobPosting.class).putExtra("Email", email)));
    }
}
