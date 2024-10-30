package com.example.registration;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Employee extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button jobApplyingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empolyee);  // Ensure you're using the correct layout file

        db = FirebaseFirestore.getInstance();
        Intent welcome = getIntent();
        String email = welcome.getStringExtra("Email");

        // Initialize the "Job Applying" button with the correct ID from XML
        jobApplyingButton = findViewById(R.id.button); // Adjusted to match XML ID

        // Set an OnClickListener to open JobListActivity
        jobApplyingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Employee.this, JobListActivity.class);
                startActivity(intent);
            }
        });

        // Existing code to fetch and display user information
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
                                    // Display or use the fetched user information as needed
                                } else {
                                    TextView tv = findViewById(R.id.empolyeeText);
                                    tv.setText("No user found with the email: " + email);
                                }
                            } else {
                                Log.d("Firestore", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {
            TextView tv = findViewById(R.id.empolyeeText);
            tv.setText("No name passed to the Employee activity.");
        }
    }
}
