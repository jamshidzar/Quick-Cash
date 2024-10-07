package com.example.registration;

public class UserCredential {
    protected boolean emailIsEmpty(String email){
        return email.isEmpty();
    }
    protected boolean passwordIsEmpty(String password){
        return password.isEmpty();
    }
}
