package com.example.registration;

import androidx.core.util.PatternsCompat;

import java.util.regex.Pattern;

public class CredentialValidator {

    protected boolean isEmptyName(String name){
        return name.isEmpty();
    }

    protected boolean isEmptyEmail(String email){
        return email.isEmpty();
    }

    protected boolean isEmptyPassword(String password){
        return password.isEmpty();
    }

    protected boolean isEmptyCreditCard(String creditcard){
        return creditcard.isEmpty();
    }

    protected boolean isValidName(String name){
        // Full Name must start capitalized and accounts for multiple last names with hyphens
        String regex = "^[A-Z]{1}[a-z]+ ([A-Z]{1}[a-z]+-?)+";
        if(Pattern.compile(regex).matcher(name).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    protected boolean isValidEmail(String email){
        String regex = "^[a-zA-Z0-9._\\-$%]+@[a-zA-Z]+.[a-zA-Z]{2,8}";
        if(Pattern.compile(regex).matcher(email).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    protected boolean isValidPassword(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&_-])[A-Za-z\\d@$!%*?&_-]{8,}$";

        if(Pattern.compile(regex).matcher(password).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    protected boolean isValidCreditCard(String creditcard){
        String regex = "[0-9]{16}";
        if(Pattern.compile(regex).matcher(creditcard).matches()){
            return true;
        }
        else{
            return false;
        }
    }
}
