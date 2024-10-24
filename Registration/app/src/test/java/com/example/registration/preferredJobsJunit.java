package com.example.registration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
public class preferredJobsJunit {
    Employee employee;

    @Before
    public void setup() {
        this.employee = new Employee();
    }

    @Test
    public void testSetJobPreferences() {
        String preference = "Mowing lawn";
        employee.setPreferredCriteria(preference);
        assertEquals(preference, employee.getPreferredCriteria());
    }
    @Test
    public void testIfPrefencesIsEmpty(){
        employee.setPreferredCriteria(null);
        assertEquals("",employee.getPreferredCriteria());
    }
    @Test
    public void testEnableAlert(){
        String preference = "Mowing lawn";
        employee.setPreferredCriteria(preference);
        employee.enableJobAlerts();
        assertTrue(employee.isJobAlertEnabled());
    }
}