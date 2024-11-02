package com.example.registration;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.registration.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)

public class JobPostingEspressoTest {
    public ActivityScenario<JobPosting> activityScenario;
    @Before
    public void setUp(){
        activityScenario = ActivityScenario.launch(JobPosting.class);

    }


    @Test
    public void checkIfJobNameIsEmpty() {
        onView(withId(R.id.location)).perform(typeText("Halifax"), closeSoftKeyboard());
        onView(withId(R.id.postalCode)).perform(typeText("B3L2A4"), closeSoftKeyboard());
        onView(withId(R.id.duration)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.urgency)).perform(typeText("High"), closeSoftKeyboard());
        onView(withId(R.id.salary)).perform(typeText("50000"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Job name cannot be blank")));
    }


    @Test
    public void checkIfLocationIsEmpty() {
        onView(withId(R.id.jobName)).perform(typeText("Software Engineer"), closeSoftKeyboard());
        onView(withId(R.id.duration)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.postalCode)).perform(typeText("B3L2A4"), closeSoftKeyboard());
        onView(withId(R.id.urgency)).perform(typeText("High"), closeSoftKeyboard());
        onView(withId(R.id.salary)).perform(typeText("50000"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Location cannot be blank")));
    }

    @Test
    public void checkIfDurationIsEmpty() {
        onView(withId(R.id.jobName)).perform(typeText("Software Engineer"), closeSoftKeyboard());
        onView(withId(R.id.location)).perform(typeText("Halifax"), closeSoftKeyboard());
        onView(withId(R.id.urgency)).perform(typeText("High"), closeSoftKeyboard());
        onView(withId(R.id.postalCode)).perform(typeText("B3L2A4"), closeSoftKeyboard());
        onView(withId(R.id.salary)).perform(typeText("50000"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Duration cannot be blank")));
    }
    @Test
    public void checkIfPostalCodeIsEmpty() {
        onView(withId(R.id.jobName)).perform(typeText("Software Engineer"), closeSoftKeyboard());
        onView(withId(R.id.location)).perform(typeText("Halifax"), closeSoftKeyboard());
        onView(withId(R.id.urgency)).perform(typeText("High"), closeSoftKeyboard());
        onView(withId(R.id.salary)).perform(typeText("50000"), closeSoftKeyboard());
        onView(withId(R.id.duration)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Postal Code cannot be blank")));
    }


    @Test
    public void checkIfUrgencyIsEmpty() {
        onView(withId(R.id.jobName)).perform(typeText("Software Engineer"), closeSoftKeyboard());
        onView(withId(R.id.location)).perform(typeText("Halifax"), closeSoftKeyboard());
        onView(withId(R.id.duration)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.postalCode)).perform(typeText("B3L2A4"), closeSoftKeyboard());
        onView(withId(R.id.salary)).perform(typeText("50000"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Urgency cannot be blank")));
    }

    @Test
    public void checkIfSalaryIsEmpty() {
        onView(withId(R.id.jobName)).perform(typeText("Software Engineer"), closeSoftKeyboard());
        onView(withId(R.id.location)).perform(typeText("Halifax"), closeSoftKeyboard());
        onView(withId(R.id.postalCode)).perform(typeText("B3L2A4"), closeSoftKeyboard());
        onView(withId(R.id.duration)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.urgency)).perform(typeText("High"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Salary cannot be blank")));
    }

    @Test
    public void testSuccessfulJobPosting() {
        onView(withId(R.id.jobName)).perform(typeText("Software Engineer"), closeSoftKeyboard());
        onView(withId(R.id.location)).perform(typeText("Halifax"), closeSoftKeyboard());
        onView(withId(R.id.duration)).perform(typeText("6"), closeSoftKeyboard());
        onView(withId(R.id.urgency)).perform(typeText("High"), closeSoftKeyboard());
        onView(withId(R.id.postalCode)).perform(typeText("B3L2A4"), closeSoftKeyboard());
        onView(withId(R.id.salary)).perform(typeText("50000"), closeSoftKeyboard());
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.errorMessage)).check(matches(withText("Successful")));
    }
}
