package com.example.registration;
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
}
