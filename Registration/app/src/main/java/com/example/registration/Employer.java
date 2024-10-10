package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empolyer);
        db = FirebaseFirestore.getInstance();
        welcome = getIntent();
        String name = welcome.getStringExtra("Name");
        if (!name.isEmpty()) {
            db.collection("user").whereEqualTo("Name", name).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {
                                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                    String email = documentSnapshot.getString("Email");
                                    String password = documentSnapshot.getString("Password");
                                    String creditCard = documentSnapshot.getString("Credit Card");
                                    String message = "Name: " + name + "\nEmail: " + email + "\nPassword: " + password + "\nCredit Card: " + creditCard;
                                    TextView tv = findViewById(R.id.employerText);
                                    tv.setText(message);
                                } else {
                                    TextView tv = findViewById(R.id.employerText);
                                    tv.setText("No user found with the name: " + name);
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
}
