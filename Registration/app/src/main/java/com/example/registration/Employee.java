package com.example.registration;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Employee extends AppCompatActivity {
    FirebaseFirestore db;
    private MapManager currentLocation;
    List<String> preferenceList = new ArrayList<>();
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
        enableJobAlerts();
        sendJobNotification();
    }
    //sets the preferred criteria
    public void setPreferredCriteria(String preference){
        preferenceList.add(preference);
    }
    public String getPreferredCriteria() {
        return null;
    }
    public String getCurrentLocation(){
        return null;
    }
    public String getJobLocation(){
        return null;
    }
    public boolean enableJobAlerts() {
        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Job alerts enabled", Toast.LENGTH_LONG).show();
                }
            }
        });
        if(checkBox.isChecked()){
            return true;
        }
        return false;
    }
    public void sendJobNotification(){
        boolean enableNotification = enableJobAlerts();
        if(enableNotification){
            //get list of jobs
            String[] Jobs = {"Tutor", "Walk dog", "baby sitting"};
            String jobLocation = getJobLocation();
            String preferedJob = getPreferredCriteria();
            String userLocation = getCurrentLocation();
            for(int i = 0 ; i < Jobs.length; i++){
                if(preferedJob.equals(Jobs[i])){
                    if(userLocation.equals(jobLocation)){
                        Toast.makeText(getApplicationContext(), preferedJob+"job available in your area"+ userLocation, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}
