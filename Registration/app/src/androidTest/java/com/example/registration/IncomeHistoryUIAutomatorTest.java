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
public class IncomeHistoryUIAutomatorTest {

    private static final int LAUNCH_TIMEOUT = 5000;
    final String launcherPackageName = "com.example.registration";
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();
        Intent launcherIntent = new Intent();
        launcherIntent.setClassName(launcherPackageName, "com.example.registration.IncomeHistoryActivity");
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launcherIntent);
        device.wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), LAUNCH_TIMEOUT);
    }


    @Test
    public void checkIfMoveToChart() throws UiObjectNotFoundException {
        UiObject chartButton = device.findObject(new UiSelector().text("Earnings Chart"));
        chartButton.clickAndWaitForNewWindow();
        UiObject chartTitle = device.findObject(new UiSelector().text("Earnings History"));
        assertTrue(chartTitle.exists());
    }

    @Test
    public void checkIfMoveToEmployee() throws UiObjectNotFoundException {
        UiObject backButton = device.findObject(new UiSelector().text("Back"));
        backButton.clickAndWaitForNewWindow();
        UiObject incomeHistory = device.findObject(new UiSelector().text("Income History"));
        assertTrue(incomeHistory.exists());
    }

    @Test
    public void checkIfListingsExist() throws UiObjectNotFoundException {
        UiObject listings = device.findObject(new UiSelector().text("Income History"));
        assertTrue(listings.exists());
    }


}