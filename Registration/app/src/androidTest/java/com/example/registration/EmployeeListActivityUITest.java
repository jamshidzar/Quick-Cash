package com.example.registration;






import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EmployeeListActivityUITest {

    private ActivityScenario<EmployeeListActivity> scenario;

    @Before
    public void setup() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EmployeeListActivity.class);
        intent.putExtra("jobID", "testJobID");
        intent.putExtra("jobName", "Test Job");

        scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void testUIComponentsExist() {
        onView(withId(R.id.employeeListView)).check(matches(isDisplayed()));
        onView(withId(R.id.btnBackToListings)).check(matches(isDisplayed()));
    }

    @Test
    public void testIntentExtrasAreReceived() {
        scenario.onActivity(activity -> {
            String jobID = activity.getIntent().getStringExtra("jobID");
            String jobName = activity.getIntent().getStringExtra("jobName");

            assertTrue(jobID.equals("testJobID"));
            assertTrue(jobName.equals("Test Job"));
        });
    }

    @Test
    public void testBackButtonFunctionality() {
        onView(withId(R.id.btnBackToListings)).perform(click());
        scenario.onActivity(activity -> {
            assertTrue(activity.isFinishing());
        });
    }

    @Test
    public void testActivityRecreation() {
        scenario.recreate();
        onView(withId(R.id.employeeListView)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        if (scenario != null) {
            scenario.close();
        }
    }
}