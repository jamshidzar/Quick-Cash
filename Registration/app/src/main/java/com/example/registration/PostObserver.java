package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import javax.security.auth.Subject;

public abstract class PostObserver extends AppCompatActivity {
    protected Subject subject;
    public abstract void update();
}
