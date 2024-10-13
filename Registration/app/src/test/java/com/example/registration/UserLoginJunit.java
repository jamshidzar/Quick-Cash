package com.example.registration;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
public class UserLoginJunit {



// Code review by Jamshid Zar:
// The test cases are well-written and straightforward.
// Each test covers essential functionality (checking if email or password is empty, and validating email format).
// As long as all tests are passing and working as expected, there’s nothing to add.
// Good work on keeping the tests clean and focused on their specific assertions.

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
