package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.DropOffLocation;
import com.ufund.api.ufundapi.persistence.DropOffLocationFileDAO;

@SuppressWarnings("null")
@Tag("Controller-tier")
public class DropOffLocationControllerTest {
    DropOffLocationController dropOffLocationController;
    DropOffLocationFileDAO mockDropOffLocationFileDAO;
    ObjectMapper mockObjectMapper;
    DropOffLocation[] testSchedule;
    
    @BeforeEach
    public void setUpUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testSchedule = new DropOffLocation[3];

        DropOffLocation d1 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "Billy");
        DropOffLocation d2 = new DropOffLocation(2, "Chili", new int[]{2026,2,2}, 6, 15, "Bob");
        DropOffLocation d3 = new DropOffLocation(3, "Richmond", new int[]{2027,3,3}, 12, 30, "BillyBob");

        testSchedule[0] = d1;
        testSchedule[1] = d2;
        testSchedule[2] = d3;

        when(mockObjectMapper
            .readValue(new File("Man I Love Schedule File Tests :D"), DropOffLocation[].class))
            .thenReturn(testSchedule);

        doAnswer((i) -> {
            DropOffLocation[] schedule = i.getArgument(1);
            testSchedule = new DropOffLocation[schedule.length];
            for(int j = 0; j < schedule.length; j++) {
                testSchedule[j] = testSchedule[j];
            }
            return null;
        }).when(mockObjectMapper).writeValue(new File("Man I Love Schedule File Tests :D"), DropOffLocation[].class);

        mockDropOffLocationFileDAO = new DropOffLocationFileDAO("Man I Love Schedule File Tests :D", mockObjectMapper);
        dropOffLocationController = new DropOffLocationController(mockDropOffLocationFileDAO);
    }



    @Test
    public void testgetSchedule() throws IOException {
        ///Setup
        String search1 = "r";
        String search2 = "ro";
        String search3 = "rop";
        
        ///Invoke
        DropOffLocation[] schedule1 = dropOffLocationController.getSchedule().getBody();
        DropOffLocation[] schedule2 = dropOffLocationController.getSchedule(search1).getBody();
        DropOffLocation[] schedule3 = dropOffLocationController.getSchedule(search2).getBody();
        DropOffLocation[] schedule4 = dropOffLocationController.getSchedule(search3).getBody();

        ///Analyze
        assertEquals(3, schedule1.length);
        assertEquals(2, schedule2.length);
        assertEquals(1, schedule3.length);
        assertEquals(0, schedule4.length);
    }

    @Test
    public void testaddDropOffLocation() throws IOException {
        ///Setup
        DropOffLocation d1 = new DropOffLocation(1, "Rochester", new int[]{}, 0, 0, "billy");
        DropOffLocation d2 = new DropOffLocation(1, "Rochester", new int[]{0,0,0}, 0, 0, "billy");
        DropOffLocation d3 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "silly");
        DropOffLocation d4 = new DropOffLocation(3, "Richmond", new int[]{2027,3,3}, 12, 30, "BillyBob");
        DropOffLocation d5 = new DropOffLocation(3, "Richmond", new int[]{2028,3,3}, 12, 30, "BillyBob");
        
        
        ///Invoke
        Boolean first = dropOffLocationController.addDropOffLocation(d1).getBody();
        Boolean second = dropOffLocationController.addDropOffLocation(d2).getBody();
        Boolean third = dropOffLocationController.addDropOffLocation(d3).getBody();
        Boolean fourth = dropOffLocationController.addDropOffLocation(d4).getBody();
        Boolean fifth = dropOffLocationController.addDropOffLocation(d5).getBody();

        ///Analyze
        assertFalse(first);
        assertFalse(second);
        assertFalse(third);
        assertFalse(fourth);
        assertTrue(fifth);
    }

    @Test
    public void testeditDropOffLocation() throws IOException {
        ///Setup
        DropOffLocation d1 = new DropOffLocation(4, "Rochester", new int[]{2025,1,1}, 0, 0, "silly");
        DropOffLocation d2 = new DropOffLocation(1, "Rochester", new int[]{}, 0, 0, "billy");
        DropOffLocation d3 = new DropOffLocation(1, "Rochester", new int[]{2025,1,1}, 0, 0, "silly");
        
        
        ///Invoke
        Boolean first = dropOffLocationController.editDropOffLocation(d1).getBody();
        Boolean second = dropOffLocationController.editDropOffLocation(d2).getBody();
        Boolean third = dropOffLocationController.editDropOffLocation(d3).getBody();

        ///Analyze
        assertFalse(first);
        assertFalse(second);
        assertTrue(third);
    }

    @Test
    public void testdeleteDropOffLocation() throws IOException {
        ///Setup
        int id1 = 4;
        int id2 = 3;
        
        
        ///Invoke
        Boolean first = dropOffLocationController.deleteDropOffLocation(id1).getBody();
        Boolean second = dropOffLocationController.deleteDropOffLocation(id2).getBody();

        ///Analyze
        assertNull(first);
        assertTrue(second);
    }

    @Test
    public void testvolunteerFunctions() throws IOException {
        ///Setup
        int id = 3;

        ///Invoke
        Boolean first = dropOffLocationController.volunteer(id, "BillyBob").getBody();
        Boolean second = dropOffLocationController.unVolunteer(id).getBody();
        Boolean third = dropOffLocationController.unVolunteer(id).getBody();
        Boolean fourth = dropOffLocationController.volunteer(id, "BillyBob").getBody();

        ///Analyze
        assertFalse(first);
        assertTrue(second);
        assertFalse(third);
        assertTrue(fourth);
    }
}

