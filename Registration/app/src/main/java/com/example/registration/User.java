package com.example.registration;

import android.util.Log;

import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

//based on location
public class User extends observer{
    protected String userID;
    protected Job job;

    public User(Job job, String userID) {
        this.userID = userID;
        this.job = job;
        job.attach(this);
    }
    @Override
    //set a listener for a new job
    public boolean onJobUpdate(){
        return true;
        //SendNotification sendNotification = new SendNotification(Employee.class.newInstance());
    }
}