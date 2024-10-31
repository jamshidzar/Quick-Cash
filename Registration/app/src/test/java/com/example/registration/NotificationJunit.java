package com.example.registration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class NotificationJunit {
    Employee employee;

    @Before
    public void setup() {

        employee = new Employee();
        //Mockito.when(employee.getPreferredCriteria()).thenReturn("Mowing Lawn");
    }

    @Test
    public void testSetJobPreferences() {
        String preference = "Mowing Lawn";
        //employee.setPreferredCriteria(preference);
        //assertEquals(preference, employee.getPreferredCriteria());
    }
    @Test
    public void testIfPrefencesIsEmpty(){
        //employee.setPreferredCriteria(null);
        //assertEquals("Mowing Lawn",employee.getPreferredCriteria());
    }
    @Test
    public void testEnableAlert(){
        //employee.enableJobAlerts();
        //assertTrue("true", employee.enableJobAlerts());
    }
}
