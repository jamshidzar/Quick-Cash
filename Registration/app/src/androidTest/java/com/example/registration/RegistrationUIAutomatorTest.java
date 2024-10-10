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
public class RegistrationUIAutomatorTest {

    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackageName = "com.example.registration";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = context.getPackageManager().getLaunchIntentForPackage(launcherPackageName);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkIfLoginPageIsVisible() {
        UiObject nameTextBox = device.findObject(new UiSelector().text("Full Name"));
        UiObject emailText = device.findObject(new UiSelector().text("Email"));
        UiObject registerButton = device.findObject(new UiSelector().text("Register"));

        assertTrue("Full Name text box is not visible", nameTextBox.exists());
        assertTrue("Email text box is not visible", emailText.exists());
        assertTrue("Register button is not visible", registerButton.exists());
    }

    @Test
    public void checkIfMove2LoginPage() throws UiObjectNotFoundException {
        UiObject nameTextBox = device.findObject(new UiSelector().text("Full Name"));
        UiObject emailTextBox = device.findObject(new UiSelector().text("Email"));
        UiObject passwordTextBox = device.findObject(new UiSelector().text("Password"));
        UiObject creditCardTextBox = device.findObject(new UiSelector().text("Credit Card"));

        assertTrue("Full Name text box is not visible", nameTextBox.exists());
        assertTrue("Email text box is not visible", emailTextBox.exists());
        assertTrue("Password text box is not visible", passwordTextBox.exists());
        assertTrue("Credit Card text box is not visible", creditCardTextBox.exists());

        nameTextBox.setText("John Doe");
        emailTextBox.setText("johndoe@example.com");
        passwordTextBox.setText("Password1!");
        creditCardTextBox.setText("1111222233334444");

        UiObject registerButton = device.findObject(new UiSelector().text("Register"));
        registerButton.clickAndWaitForNewWindow();
        UiObject noAccountText = device.findObject(new UiSelector().text("Don't have an account? Register here."));
        assertTrue(noAccountText.exists());
    }

    public void checkIfMoveToLoginOnButton() throws UiObjectNotFoundException {
        UiObject loginButton = device.findObject(new UiSelector().text("Login"));
        loginButton.clickAndWaitForNewWindow();
        UiObject registerText = device.findObject(new UiSelector().text("Don't have an account? Register here."));
        assertTrue(registerText.exists());
    }
}


