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

    @Test
    public void checkIfNameIsEmpty() {
        onView(withId(R.id.EmailText)).perform(typeText("example@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_NAME)));
    }

    @Test
    public void checkIfEmailIsEmpty() {
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_EMAIL)));
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.CreditCard)).perform(typeText("1111222233334444"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_PASSWORD)));
    }

    @Test
    public void checkIfCreditCardIsEmpty() {
        onView(withId(R.id.NameText)).perform(typeText("Bob Joe"), closeSoftKeyboard());
        onView(withId(R.id.EmailText)).perform(typeText("bob@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText("pass123"), closeSoftKeyboard());
        onView(withId(R.id.RegisterButton)).perform(click());
        onView(withId(R.id.statusLabel)).check(matches(withText(R.string.EMPTY_CREDITCARD)));
    }

    
}
