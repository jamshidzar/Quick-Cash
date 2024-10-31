package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import javax.security.auth.Subject;

public abstract class PostObserver extends AppCompatActivity{
    protected Job JobPosting;
    public abstract void update(int state);
}
