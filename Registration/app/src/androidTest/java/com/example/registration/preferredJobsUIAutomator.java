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
        launcherIntent.setClassName(launcherPackageName, "com.example.registration.LoginActivity");
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfMovedToNotificationList() throws UiObjectNotFoundException {
        UiObject email = device.findObject(new UiSelector().text("Email"));
        email.setText("tanishika@gmail.com");
        UiObject password = device.findObject(new UiSelector().text("Password"));
        password.setText("quickCash@123");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();
        UiObject dashboard = device.findObject(new UiSelector().textContains("quickCash"));
        assertTrue(dashboard.exists());
        UiObject employeeButton = device.findObject(new UiSelector().textContains("employee"));
        employeeButton.clickAndWaitForNewWindow();
        UiObject notificationsButton = device.findObject(new UiSelector().textContains("Notifications"));
        notificationsButton.clickAndWaitForNewWindow();
        UiObject notificationView = device.findObject(new UiSelector().textContains("Job Notifications"));
        assertTrue(notificationView.exists());
    }
    @Test
    public void checkIfNotMovedToNotificationList() throws UiObjectNotFoundException {
        UiObject email = device.findObject(new UiSelector().text("Email"));
        email.setText("blueskye@gmail.com");
        UiObject password = device.findObject(new UiSelector().text("Password"));
        password.setText("quickCash@123");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();
        UiObject dashboard = device.findObject(new UiSelector().textContains("quickCash"));
        assertTrue(dashboard.exists());
        UiObject employeeButton = device.findObject(new UiSelector().textContains("employee"));
        employeeButton.clickAndWaitForNewWindow();
        UiObject notificationsButton = device.findObject(new UiSelector().textContains("Notifications"));
        notificationsButton.clickAndWaitForNewWindow();
        UiObject notificationView = device.findObject(new UiSelector().textContains("Enable Job Alerts"));
        assertTrue(notificationView.exists());
    }
}
