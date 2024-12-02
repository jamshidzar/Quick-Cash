package com.example.registration;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationPreferenceManager {
    private final FirebaseFirestore db;
    private Context context;

    public NotificationPreferenceManager(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public NotificationPreferenceManager() {
        this.db = FirebaseFirestore.getInstance();;
    }

    public void updateNotificationPreference(String userId) {
        Log.d("UserId", "User ID: " + userId);
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();*/
        db.collection("user")  // Replace with your collection name
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get document reference
                                if(document.getId().equals(userId)){
                                    DocumentReference userRef = document.getReference();
                                    Log.d("UserRef", "User Document Reference: " + userRef.getPath());
                                    // Do something with userRef
                                    userRef.update("isNotificationEnabled", true);
                                    break;
                                }
                                else{
                                    Log.d("UserRef", "No matching document ID found for userId: " + userId);
                                }
                            }
                        } else {
                            Log.d("UserRef", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public Task<Boolean> retrieveNotificationPreference(String userId) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        db.collection("user")  // Replace with your collection name
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve the isNotificationEnabled field
                                Boolean isNotificationEnabled = document.getBoolean("isNotificationEnabled");
                                if (isNotificationEnabled != null) {
                                    taskCompletionSource.setResult(isNotificationEnabled);
                                    Log.d("NotificationStatus", "isNotificationEnabled: " + isNotificationEnabled);
                                }
                            } else {
                                taskCompletionSource.setResult(false);
                                taskCompletionSource.setResult(false);
                                Log.d("NotificationStatus", "No document found with ID: " + userId);
                            }
                        } else {
                            taskCompletionSource.setResult(false);
                            Log.d("NotificationStatus", "Error retrieving document: ", task.getException());
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

}
