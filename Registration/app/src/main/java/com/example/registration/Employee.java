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
    FirebaseFirestore db;




// Code review by Jamshid Zar:
// Overall, the onCreate method is well-structured, and the Firebase integration is solid.
// A few suggestions for improvement:
// - Consider adding null checks for the Intent to prevent potential crashes.
// - Be cautious about displaying sensitive information such as passwords and credit card details in the UI.
// - Ensure all user data fields are properly null-checked before using them to avoid null pointer exceptions.
// - The error message in the else block references "name" when it should reference "email" as that is being checked.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empolyee);  // Ensure you're using the correct layout file
        db = FirebaseFirestore.getInstance();
        Intent welcome = getIntent();
        String email = welcome.getStringExtra("Email");

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
                                    String message = "Name: " + name + "\nEmail: " + email + "\nPassword: " + password + "\nCredit Card: " + creditCard;

                                    TextView tv = findViewById(R.id.empolyeeText);
                                    tv.setText(message);
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
