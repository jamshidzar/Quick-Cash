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
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText name, email, password, creditcard;
    TextView errMSG;
    Button Registration, Login;

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
        creditcard = findViewById(R.id.TextNumber);
        Registration = findViewById(R.id.RegisterButton);
        Login = findViewById(R.id.goToLoginButton);
        errMSG = findViewById(R.id.errorMSG);

        setUpLoginButton();

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String CreditCard = creditcard.getText().toString();
                CredentialValidator validator = new CredentialValidator();
                String errorMessage = new String();
                boolean validRegistration = true;

                if (!validator.isValidName(Name)){
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.INVALID_NAME).trim();
                }
                if (!validator.isValidEmail(Email)){
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.INVALID_EMAIL).trim();
                }
                if(!validator.isValidPassword(Password)){
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.INVALID_PASSWORD).trim();
                }
                if(!validator.isValidCreditCard(CreditCard)){
                    validRegistration = false;
                    errorMessage = getResources().getString(R.string.INVALID_CREDIT_CARD).trim();
                }

                setErrorMessage(errorMessage);

                if (validRegistration) {
                    setErrorMessage(errorMessage); // clear text view containing error message upon valid credentials entered
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

    protected void setErrorMessage(String message){
        TextView errorMessage = findViewById(R.id.errorMSG);
        errorMessage.setText(message.trim());
        errorMessage.setTextSize(16);
        errorMessage.setBackgroundColor(Color.RED);
        errorMessage.setTextColor(Color.WHITE);
        errorMessage.setPadding(30, 10, 30, 10);
    }

    protected void setUpLoginButton(){
//        Login.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }

    protected void moveToLoginPage(){
//        MainActivity.this.startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}