package com.example.registration;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText name, email, password, creditcard;
    TextView statusLabel;

    Button Registration;

    FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.NameText);
        email = findViewById(R.id.EmailText);
        password = findViewById(R.id.PasswordText);
        creditcard = findViewById(R.id.CreditCard);
        statusLabel = findViewById(R.id.statusLabel);

        Registration = findViewById(R.id.RegisterButton);
        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String CreditCard = creditcard.getText().toString();
                CredentialValidator validator = new CredentialValidator();
                String errorMessage;

                if(Name.isEmpty()){
                    statusLabel.setText("Name cannot be empty");
                    return;
                }

                if(Email.isEmpty()){
                    statusLabel.setText("Email cannot be empty");
                    return;
                }

                if(Password.isEmpty()){
                    statusLabel.setText("Password cannot be empty");
                    return;
                }

                if(CreditCard.isEmpty()){
                    statusLabel.setText("Credit Card cannot be empty");
                    return;
                }

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
            }
        });
    }


}