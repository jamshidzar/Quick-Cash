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
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class preferredJobsUIAutomator {
    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackageName = "com.example.registration";
    private UiDevice device;
    @Before
    public void setup(){
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = new Intent();
        launcherIntent.setClassName(launcherPackageName, "com.example.registration.HompageActivity");
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
    }
    @Test
    public void checkIfPreferenceIsVisible(){
        UiObject preference = device.findObject(new UiSelector().text("job preference list"));
        assertTrue(preference.exists());
    }
    @Test
    public void checkIfMovedToPrefrenceList() throws UiObjectNotFoundException {
        UiObject preferenceButton = device.findObject(new UiSelector().text("job preference list"));
        preferenceButton.clickAndWaitForNewWindow();
        UiObject preferenceList = device.findObject(new UiSelector().text("List of preferred jobs"));
        assertTrue(preferenceButton.exists());
    }
    @Test
    public void testEnableAlert() throws UiObjectNotFoundException {
        UiObject employeeButton = device.findObject(new UiSelector().text("employee"));
        employeeButton.clickAndWaitForNewWindow();

        UiObject enableAlertSwitch = device.findObject(new UiSelector().resourceId("com.example.registration:id/alert_switch"));
        if (!enableAlertSwitch.isChecked()) {
            enableAlertSwitch.click();
        }
        assertTrue("Job alert is not enabled", enableAlertSwitch.isChecked());
    }
    @Test
    public void testReceiveJobAlert() throws UiObjectNotFoundException {
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("New job alert")), 5000);
        UiObject jobAlertNotification = device.findObject(new UiSelector().textContains("New job alert"));
        assertTrue("Job alert notification is not received", jobAlertNotification.exists());
        jobAlertNotification.clickAndWaitForNewWindow();
        UiObject jobDetailScreen = device.findObject(new UiSelector().textContains("Job Details"));
        assertTrue("Failed to navigate to job details", jobDetailScreen.exists());
    }
}
