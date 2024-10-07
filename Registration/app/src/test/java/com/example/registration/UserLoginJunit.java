package com.example.registration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
public class UserLoginJunit {
    UserCredential validator;
    @Before
    public void setup(){
        validator = new UserCredential();
    }
    @Test
    public void checkIfEmailIsEmpty(){
        assertTrue(validator.emailIsEmpty(""));
    }
    @Test
    public void  checkIfPasswordIsEmpty(){
        assertTrue(validator.passwordIsEmpty(""));
    }
    @Test
    public void  checkIfEmailIsValid(){
        assertTrue(validator.emailPattern("tanishika_123@gmail.com"));
    }
    @Test
    public void  checkIfEmailIsInValid(){
        assertFalse(validator.emailPattern("tanishika_123.gmail.com"));
    }
}
