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



// Code review by Jamshid Zar:
// - Overall, this test class is well-written and effectively tests the login page UI and flow using UI Automator.
// - The `setup()` method does a great job initializing the `UiDevice` and launching the login activity. Using `LAUNCH_TIMEOUT` to ensure the app is fully loaded is good practice.
// - In the `checkIfLoginPageIsVisible()` test, you correctly check for the presence of key elements (email field, password field, login button), ensuring that the login page is displayed as expected.
// - The test `checkIfMovedtoDashboard()` successfully simulates a valid login scenario and verifies that the user is redirected to the dashboard, which is good. The use of `clickAndWaitForNewWindow()` ensures the test waits for the dashboard to load.
// - The `checkIfMovedToLoginPage()` test is comprehensive, as it not only tests login functionality but also verifies that the user can log out and return to the login page. Good use of the logout confirmation dialog handling.
// - Suggestion: It’s good practice to use string resources for text such as "Email", "Password", and "Login" instead of hardcoding them directly in the test. This can make your tests more maintainable and less prone to breaking if the text changes in the app.
// - As long as the tests pass consistently, they cover key aspects of the login flow and logout process, ensuring the user experience is smooth.




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
        UiObject email = device.findObject(new UiSelector().text("Email"));
        email.setText("12@google.com");
        UiObject password = device.findObject(new UiSelector().text("Password"));
        password.setText("PassWo00rd!");
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();
        UiObject dashboard = device.findObject(new UiSelector().textContains("quickCash"));
        assertTrue(dashboard.exists());
        UiObject logoutButton = device.findObject(new UiSelector().text("Logout"));
        logoutButton.clickAndWaitForNewWindow();
        UiObject confirmButton = device.findObject(new UiSelector().text("Yes"));
        confirmButton.clickAndWaitForNewWindow();
        UiObject email_login = device.findObject(new UiSelector().text("Email"));
        assertTrue("Login page not visible", email_login.exists());
        UiObject password_login = device.findObject(new UiSelector().text("Password"));
        assertTrue(password_login.exists());
        UiObject loginButton_login = device.findObject(new UiSelector().text("Login"));
        assertTrue(loginButton_login.exists());
    }
}
