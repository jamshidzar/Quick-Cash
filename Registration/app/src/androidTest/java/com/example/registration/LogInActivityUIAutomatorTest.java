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
public class LogInActivityUIAutomatorTest {
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
    public void checkIfLoginPageIsVisible(){
        UiObject email = device.findObject(new UiSelector().text("Email"));
        assertTrue(email.exists());
        UiObject password = device.findObject(new UiSelector().text("Password"));
        assertTrue(password.exists());
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        assertTrue(loginButton.exists());
    }
    @Test
    public void checkIfMovedtoDashboard() throws UiObjectNotFoundException{
        UiObject email = device.findObject(new UiSelector().text("Email"));
        email.setText("12@google.com");
        UiObject password = device.findObject(new UiSelector().text("Password"));
        password.setText("PassWo00rd!");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();
        UiObject dashboard = device.findObject(new UiSelector().textContains("quickCash"));
        assertTrue(dashboard.exists());
    }
    @Test
    public void checkIfMovedToLoginPage() throws UiObjectNotFoundException {
        // Input email and password and click the login button
        UiObject email = device.findObject(new UiSelector().text("Email"));
        email.setText("12@google.com");

        UiObject password = device.findObject(new UiSelector().text("Password"));
        password.setText("PassWo00rd!");

        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();

        // Ensure the dashboard is displayed after login
        UiObject dashboard = device.findObject(new UiSelector().textContains("quickCash"));
        assertTrue(dashboard.exists());

        // Click the logout button
        UiObject logoutButton = device.findObject(new UiSelector().text("Logout"));
        logoutButton.clickAndWaitForNewWindow();

        // Handle the logout confirmation dialog
        UiObject confirmLogoutYesButton = device.findObject(new UiSelector().text("Yes"));
        confirmLogoutYesButton.waitForExists(5000);  // Wait for the "Yes" button to appear
        confirmLogoutYesButton.click();  // Confirm the logout

        // After confirming, wait for the login page
        UiObject loginButton_login = device.findObject(new UiSelector().text("Login"));
        boolean loginButtonExists = loginButton_login.waitForExists(5000);  // Wait for the login button to appear
        assertTrue("Login button not found after logout", loginButtonExists);
    }




}

