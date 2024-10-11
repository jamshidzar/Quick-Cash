package com.example.registration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserRegistrationJunit {
    CredentialValidator validator;


// Code review by Jamshid Zar:
// The test cases are well-structured, covering all the essential validation checks for name, email, password, and credit card fields.
// Each test method is named appropriately and makes use of clear assertions (`assertTrue`, `assertFalse`), ensuring the tests are easy to follow.
// As long as all the tests are passing, they seem to be doing exactly what they need to.
// No further comments, as the tests are well-written and focus on validating the key input fields. Good work!

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
