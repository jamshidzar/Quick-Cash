package com.example.registration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import androidx.test.core.app.ApplicationProvider;
import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.example.registration.CurrentLocation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


public class GoogleMapsUITest {
    private static final String launcherPackage = "androidTest";
    private static final int TIMEOUT = 5000;
    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
        Intent launcherIntent = new Intent(ApplicationProvider.getApplicationContext(), MapsActivity.class);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ApplicationProvider.getApplicationContext().startActivity(launcherIntent);
    }


    @Test
    public void testManualLocationSearch() throws UiObjectNotFoundException {
        // Find and enter text in the location input field
        UiObject locationInput = device.findObject(new UiSelector().resourceId("com.example.registration:id/locationInput"));
        locationInput.setText("Toronto");

        // Find the search button by resource ID and click it
        UiObject searchButton = device.findObject(new UiSelector().resourceId("com.example.registration:id/searchButton"));

        // Check if the button exists, then click it
        assertTrue("Search button not found", searchButton.exists());
        searchButton.clickAndWaitForNewWindow();

        // Wait for the marker with "Toronto" description to appear on the map
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Toronto"));
        marker.waitForExists(TIMEOUT);

        // Verify that the marker is displayed
        assertTrue("The marker for the location 'Toronto' is not displayed on the map.", marker.exists());
    }
}

