package com.example.registration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class calculateDistUnitTest {
    calculateDistance caldist;
    @Before
    public void setup(){
        caldist = new calculateDistance(44.6356, 63.5952, "Halifax");
    }
    @Test
    public void testJob(){
        int result = caldist.jobList();
        assertEquals(0, result);
    }
}
