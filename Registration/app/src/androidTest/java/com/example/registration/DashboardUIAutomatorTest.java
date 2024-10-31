package com.example.registration;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DashboardUIAutomatorTest {
// Code review by Jamshid Zar:
// The UI tests are well-structured and do a good job of testing navigation between different parts of the application.
// The `setup()` method is properly initializing the `UiDevice` and launching the appropriate activity with a clear timeout, which is good.
// Both test cases—`checkIfMoveToEmployee()` and `checkIfMoveToEmployer()`—correctly simulate button clicks and validate the existence of the appropriate UI elements.
// As long as the tests pass consistently, they seem to be doing exactly what is needed.
// Good job implementing functional UI tests!


    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackageName = "com.example.registration";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = new Intent();
        launcherIntent.setClassName(launcherPackageName, "com.example.registration.HomepageActivity");
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfMoveToEmployee() throws UiObjectNotFoundException {
        UiObject employeeButton = device.findObject(new UiSelector().text("employee"));
        employeeButton.clickAndWaitForNewWindow();
        UiObject jobApplyButton = device.findObject(new UiSelector().text("Job Applying"));
        assertTrue(jobApplyButton.exists());
    }

    @Test
    public void checkIfMoveToEmployer() throws UiObjectNotFoundException {
        UiObject employerButton = device.findObject(new UiSelector().text("employer"));
        employerButton.clickAndWaitForNewWindow();
        UiObject jobPostButton = device.findObject(new UiSelector().text("Job Posting"));
        assertTrue(jobPostButton.exists());
    }
}
