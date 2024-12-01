package com.example.registration;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class JobHistoryActivityUIAutomatorTest {

    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String APP_PACKAGE = "com.example.registration";
    private UiDevice device;

    @Before
    public void setUp() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Set userId in SharedPreferences
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userId", "testUserId");
        editor.apply();

        // Launch the JobHistoryActivity
        Intent intent = new Intent(context, JobHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        // Wait for the activity to appear
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testJobHistoryRecyclerViewIsDisplayed() {
        // Wait for the RecyclerView to be displayed
        device.wait(Until.hasObject(By.res(APP_PACKAGE, "recyclerViewJobHistory")), 5000);
        UiObject2 recyclerView = device.findObject(By.res(APP_PACKAGE, "recyclerViewJobHistory"));
        assertNotNull("Job History RecyclerView should be displayed", recyclerView);
    }
}

