package com.example.registration; // Replace with your package name

import android.content.Context;
import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class JobListActivityUiAutomatorTest {

    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String APP_PACKAGE = "com.example.registration"; // Replace with your app's package name
    private UiDevice device;

    @Before
    public void setUp() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Launch the app with an intent that passes the userId
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, JobListActivity.class);
        intent.putExtra("userId", "testUserId");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        // Wait for the activity to appear
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testRecyclerViewIsDisplayed() {
        // Verify that the RecyclerView is displayed
        device.wait(Until.hasObject(By.res(APP_PACKAGE, "availableJobsRecyclerView")), 5000);
        UiObject2 recyclerView = device.findObject(By.res(APP_PACKAGE, "availableJobsRecyclerView"));
        assertNotNull("RecyclerView should be displayed", recyclerView);
    }

    @Test
    public void testApplyButtonPresence() {
        // Wait for an "Apply" button to appear (assumes the button is in a job item)
        device.wait(Until.hasObject(By.res(APP_PACKAGE, "applyButton")), 5000);
        UiObject2 applyButton = device.findObject(By.res(APP_PACKAGE, "applyButton"));
        assertNotNull("Apply button should be present", applyButton);
        applyButton.click();

        // Check for the toast message indicating a successful application
        device.wait(Until.hasObject(By.text("Job applied successfully!")), 5000);
        UiObject2 toastMessage = device.findObject(By.text("Job applied successfully!"));


    }

    @Test
    public void testSaveToFavoritesButtonPresence() {
        // Wait for a "Save to Favorites" button to appear (assumes the button is in a job item)
        device.wait(Until.hasObject(By.res(APP_PACKAGE, "saveToFavoritesButton")), 5000);
        UiObject2 favoritesButton = device.findObject(By.res(APP_PACKAGE, "saveToFavoritesButton"));
        assertNotNull("Save to Favorites button should be present", favoritesButton);
        favoritesButton.click();

        // Check for the toast message indicating a successful save
        device.wait(Until.hasObject(By.text("Job added to favorites!")), 3000);
        UiObject2 toastMessage = device.findObject(By.text("Job added to favorites!"));

    }
}




