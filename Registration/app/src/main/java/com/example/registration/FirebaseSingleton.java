package com.example.registration;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseSingleton {

    private static FirebaseSingleton instance;
    private FirebaseFirestore db;

    private FirebaseSingleton(){
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirebaseSingleton getInstance(){
        if (instance == null){
            instance = new FirebaseSingleton();
        }
        return instance;
    }

    public FirebaseFirestore getDb(){
        return db;
    }
}
