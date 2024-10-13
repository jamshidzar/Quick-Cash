package com.example.registration;

import androidx.core.util.PatternsCompat;

import java.util.regex.Pattern;

// Object to check registration credentials
public class CredentialValidator {

    // Checks for empty name input
    protected boolean isEmptyName(String name){
        return name.isEmpty();
    }

    // Checks for empty email input
    protected boolean isEmptyEmail(String email){
        return email.isEmpty();
    }

    // Checks for empty password input
    protected boolean isEmptyPassword(String password){
        return password.isEmpty();
    }

    // Checks for empty credit card input
    protected boolean isEmptyCreditCard(String creditcard){
        return creditcard.isEmpty();
    }

    // Checks if name input is valid
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

    // Checks if email is valid
    protected boolean isValidEmail(String email){

        // Must follow valid email template
        String regex = "^[a-zA-Z0-9._\\-$%]+@[a-zA-Z]+.[a-zA-Z]{2,8}";

        if(Pattern.compile(regex).matcher(email).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    // Checks if password is valid
    protected boolean isValidPassword(String password){

        // Password must contain 1 lower case, upper case, special character and number and must be
        // at least 8 characters in length
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&_-])[A-Za-z\\d@$!%*?&_-]{8,}$";

        if(Pattern.compile(regex).matcher(password).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    // Checks valid credit card
    protected boolean isValidCreditCard(String creditcard){

        // Must only contain digits and be 16 characters in length
        String regex = "[0-9]{16}";

        if(Pattern.compile(regex).matcher(creditcard).matches()){
            return true;
        }
        else{
            return false;
        }
    }
}
