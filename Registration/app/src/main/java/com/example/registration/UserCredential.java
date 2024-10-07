package com.example.registration;

public class UserCredential {
    protected boolean emailIsEmpty(String email){
        return email.isEmpty();
    }
    protected boolean passwordIsEmpty(String password){
        return password.isEmpty();
    }
    protected boolean emailPattern(String email){
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}
