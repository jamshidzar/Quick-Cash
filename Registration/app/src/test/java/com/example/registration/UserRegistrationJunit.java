package com.example.registration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserRegistrationJunit {
    CredentialValidator validator;
    @Before
    public void setup(){
        validator = new CredentialValidator();
    }

    @Test
    public void checkIfNameIsEmpty(){
        assertTrue(validator.isEmptyName(""));
    }

    @Test
    public void checkIfEmailIsEmpty(){
        assertTrue(validator.isEmptyEmail(""));
    }

    @Test
    public void  checkIfPasswordIsEmpty(){
        assertTrue(validator.isEmptyPassword(""));
    }

    @Test
    public void checkIfCreditCardIsEmpty(){
        assertTrue(validator.isEmptyCreditCard(""));
    }

    @Test
    public void checkIfNameIsValid(){
        assertTrue(validator.isValidName("John Doe"));
    }

    @Test
    public void checkIfNameIsInvalid(){
        assertFalse(validator.isValidName("JOE"));
    }

    @Test
    public void  checkIfEmailIsValid(){
        assertTrue(validator.isValidEmail("tanishika_123@gmail.com"));
    }

    @Test
    public void  checkIfEmailIsInValid(){
        assertFalse(validator.isValidEmail("tanishika_123.gmail.com"));
    }

    @Test
    public void checkIfPasswordIsValid(){
        assertTrue(validator.isValidPassword("Password1!"));
    }

    @Test
    public void checkIfPasswordIsInvalid(){
        assertFalse(validator.isValidPassword("Password"));
    }

    @Test
    public void checkIfCreditCardIsValid(){
        assertTrue(validator.isValidCreditCard("1234123412341234"));
    }

    @Test
    public void checkIfCreditCardIsInvalid(){
        assertFalse(validator.isValidCreditCard("12341234123"));
    }
}
