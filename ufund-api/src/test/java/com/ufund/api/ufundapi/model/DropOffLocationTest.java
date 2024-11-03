package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-teir")
public class DropOffLocationTest {
    @Test
    public void testConstructor() {
        ///Setup
        int expected_id = 1;
        String expected_town = "Rochester";
        int[] expected_date = new int[]{};
        int expected_hour = 12;
        int expected_minute = 15;
        String expected_user = "Stephen";
        
        ///Invoke
        DropOffLocation d = new DropOffLocation(expected_id, expected_town, expected_date, expected_hour, expected_minute, expected_user);

        ///Analyze
        assertEquals(expected_id, d.getId());
        assertEquals(expected_town, d.getTown());
        assertEquals(expected_date, d.getDate());
        assertEquals(expected_hour, d.getHour());
        assertEquals(expected_minute, d.getMinute());
        assertEquals(expected_user, d.getUser());
    }

    @Test
    public void testisValid() {
        ///Setup
        DropOffLocation d1 = new DropOffLocation(1, "Rochester", new int[]{}, 0, 0, "billy");
        DropOffLocation d2 = new DropOffLocation(1, "Rochester", new int[]{0,0,0}, 0, 0, "billy");
        DropOffLocation d3 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "billy");
        
        ///Invoke
        Boolean first = d1.isValid();
        Boolean second = d2.isValid();
        Boolean third = d3.isValid();

        ///Analyze
        assertFalse(first);
        assertFalse(second);
        assertTrue(third);
    }

    @Test
    public void testequals() {
        ///Setup
        Object o = new Object();
        DropOffLocation d1 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "billy");
        DropOffLocation d2 = new DropOffLocation(2, "Rochester", new int[]{2025,1,1}, 0, 0, "billy");
        DropOffLocation d3 = new DropOffLocation(1, "Chili", new int[]{2025,1,1}, 0, 0, "billy");
        DropOffLocation d4 = new DropOffLocation(1, "Rochester", new int[]{2024,1,1}, 0, 0, "billy");
        DropOffLocation d5 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 1, 0, "billy");
        DropOffLocation d6 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 1, "billy");
        DropOffLocation d7 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "bobby");
        DropOffLocation d8 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "billy");
        
        ///Invoke
        Boolean object = d1.equals(o);
        Boolean first = d1.equals(d1);
        Boolean second = d1.equals(d2);
        Boolean third = d1.equals(d3);
        Boolean fourth = d1.equals(d4);
        Boolean fifth = d1.equals(d5);
        Boolean sixth = d1.equals(d6);
        Boolean seventh = d1.equals(d7);
        Boolean eighth = d1.equals(d8);

        ///Analyze
        assertFalse(object);
        assertTrue(first);
        assertTrue(second);
        assertFalse(third);
        assertFalse(fourth);
        assertFalse(fifth);
        assertFalse(sixth);
        assertTrue(seventh);
        assertTrue(eighth);
    }

    @Test
    public void testtoString() {
        ///Setup
        DropOffLocation d = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "billy");
        String expected_dropOffLocationString = String.format("DropOffLocation [id=%d, town=%s, date=%s, hour=%d, minute=%d, user=%s]",1,"Rochester",Arrays.toString(new int[]{2025,1,1}),0,0,"billy");
        
        ///Invoke
        String dropOffLocationString = d.toString();

        ///Analyze
        assertEquals(expected_dropOffLocationString, dropOffLocationString);
    }
}