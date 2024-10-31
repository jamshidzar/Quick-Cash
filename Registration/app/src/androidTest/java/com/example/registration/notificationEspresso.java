package com.example.registration;
import static org.hamcrest.Matchers.allOf;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class notificationEspresso {
    public ActivityScenario<Employee> activityScenario;
    @Before
    public void setUp(){
        activityScenario = ActivityScenario.launch(Employee.class);
    }
}
