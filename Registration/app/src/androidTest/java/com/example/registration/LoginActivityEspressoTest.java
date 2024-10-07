package com.example.registration;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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

}
