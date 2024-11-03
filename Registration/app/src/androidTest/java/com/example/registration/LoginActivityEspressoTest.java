package com.example.registration;

/*
* Code review done by Tanishika:
* Get rid of unused library dependencies
* */
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.Matchers.allOf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class LoginActivityEspressoTest {
    public ActivityScenario<LoginActivity> activityScenario;
    @Before
    public void setUp(){
        activityScenario = ActivityScenario.launch(LoginActivity.class);

    }


// Code review by Jamshid Zar:
// - Overall, this test class is well-structured and thoroughly tests the login functionality for various scenarios (empty email/password, invalid email, valid/invalid user).
// - The use of `ActivityScenario.launch(LoginActivity.class)` in the `setUp()` method is a good practice for initializing the login activity before each test.
// - Each test case covers specific aspects of the login flow, and the test names are clear and descriptive, which makes it easy to understand what is being tested.
// - The validation tests for empty email and password fields are well-implemented and handle user feedback through the status label effectively.
// - For the `UserAuthenticationWithValidUser` and `UserAuthenticationWithInvalidUser` tests, you are using `Thread.sleep(2000)` to wait for authentication. A more efficient approach would be to use Espresso’s `IdlingResource` to handle asynchronous operations without hardcoding delays, but it's acceptable for now if the tests pass consistently.
// - Overall, this test suite does a good job of covering edge cases and typical user behavior during login, and as long as the tests pass, the functionality seems robust.



    @Test
    public void checkIfEmailIsEmpty(){
        onView(withId(R.id.emailInput)).perform(typeText(""));
        onView(withId(R.id.passwordInput)).perform(typeText("password123"));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Please enter Email")));
    }
    @Test
    public void checkIfPasswordIsEmpty(){
        onView(withId(R.id.emailInput)).perform(typeText("tanishika@gmail.com"));
        onView(withId(R.id.passwordInput)).perform(typeText(""));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Please enter password")));
    }
    @Test
    public void checkIfEmailIsValid(){
        onView(withId(R.id.emailInput)).perform(typeText("tanishika.gmail.com"));
        onView(withId(R.id.passwordInput)).perform(typeText("pass123"));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText("Invalid email")));
    }
    @Test
    public void UserAuthenticationWithValidUser() throws InterruptedException {
        onView(withId(R.id.emailInput)).perform(typeText("12@google.com"));
        onView(withId(R.id.passwordInput)).perform(typeText("PassWo00rd!"));
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.title)).check(matches(withText("QuickCash")));
    }
    @Test
    public void UserAuthenticationWithInvalidUser() throws InterruptedException {
        onView(withId(R.id.emailInput)).perform(typeText("tttanishika@gmail.com"));
        onView(withId(R.id.passwordInput)).perform(typeText("PassWord!"));
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.statusLabel)).check(matches(withText("You don't have an account, please register.")));
    }
}
