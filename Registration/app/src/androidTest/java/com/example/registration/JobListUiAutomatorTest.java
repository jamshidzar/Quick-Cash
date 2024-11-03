package com.example.registration;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.registration.JobList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class JobListUiAutomatorTest {
    private UiDevice device;

    @Before
    public void setUp() {
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void testGoBackToEmployer() throws UiObjectNotFoundException {
        try (ActivityScenario<JobList> scenario = ActivityScenario.launch(JobList.class)) {
            device.waitForIdle();
            UiObject goBackButton = device.findObject(new UiSelector().resourceId("com.example.registration:id/buttonBack"));
            goBackButton.click();
            device.waitForIdle();
            UiObject employerText = device.findObject(new UiSelector().resourceId("com.example.registration:id/employerText"));
            assertTrue("Employer page is not displayed", employerText.exists());
        }
    }

    @Test
    public void checkIfJobListIsVisible() throws UiObjectNotFoundException {
        try (ActivityScenario<JobList> scenario = ActivityScenario.launch(JobList.class)) {
            device.waitForIdle();
            UiObject jobListView = device.findObject(new UiSelector().resourceId("com.example.registration:id/list_view"));
            assertTrue("Job list is not visible", jobListView.exists());
        }
    }
}
