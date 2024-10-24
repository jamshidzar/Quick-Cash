package com.example.registration;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.registration.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)

public class EspressoTest {

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class);
    }

// Code review by Jamshid Zar:
// - Overall, the test cases are well-written and cover a wide range of scenarios for the registration form.
// - The use of `ActivityScenario.launch(MainActivity.class)` in the `setup()` method is a great way to initialize the activity for testing.
// - Each test is focused on a specific field (name, email, password, credit card) and its validation, which is good practice for maintaining clear and concise tests.
// - The use of `onView().perform()` followed by `check()` for UI interaction and validation is solid and standard practice for Espresso testing.
// - The tests for invalid inputs (name, email, password, credit card) are thorough and should catch any input errors in the form validation.
// - The `Thread.sleep(2000)` in `checkIfValidRegistration()` could be replaced with Espresso’s `IdlingResource` to make it more efficient and avoid hardcoding delays, but it's acceptable for now as long as the tests pass.
// - As long as all tests are passing, they are functioning as expected and correctly validating the form input fields.




    @Test
    public void checkIfNameIsEmpty() {
        onView(withId(R.id.EmailText)).perform(typeText("example@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("Password1!"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText(R.string.EMPTY_NAME)));
    }

    @Test
    public void checkIfEmailIsEmpty() {
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("Password1!"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText(R.string.EMPTY_EMAIL)));
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText(R.string.EMPTY_PASSWORD)));
    }

    @Test
    public void checkIfCreditCardIsEmpty() {
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("Password1!"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText(R.string.EMPTY_CREDITCARD)));
    }

    @Test
    public void checkIfValidRegistration() throws InterruptedException {
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("Password1!"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.registerPrompt)).check(matches(withText("Don't have an account? Register here.")));
    }

    @Test
    public void checkIfInvalidName(){
        onView(withId(R.id.NameText)).perform(typeText("B"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("Password1!"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText("Invalid Name")));
    }

    @Test
    public void checkIfInvalidEmail(){
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("Password1!"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText("Invalid Email")));
    }

    @Test
    public void checkIfInvalidPassword(){
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText("Invalid Password")));
    }

    @Test
    public void checkIfInvalidCreditCard(){
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("Password1!"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.errorMSG)).check(matches(withText("Invalid Credit Card")));
    }

    
}
