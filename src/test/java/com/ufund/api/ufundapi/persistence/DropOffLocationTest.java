package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

@Tag("Persistence-teir")
public class DropOffLocationTest {
    DropOffLocationFileDAO dropOffLocationFileDAO;
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

        dropOffLocationFileDAO = new DropOffLocationFileDAO("Man I Love Schedule File Tests :D", mockObjectMapper);
    }



    @Test
    public void testgetSchedule() throws IOException {
        ///Setup
        String search1 = "r";
        String search2 = "ro";
        String search3 = "rop";
        
        ///Invoke
        DropOffLocation[] schedule1 = dropOffLocationFileDAO.getSchedule();
        DropOffLocation[] schedule2 = dropOffLocationFileDAO.getSchedule(search1);
        DropOffLocation[] schedule3 = dropOffLocationFileDAO.getSchedule(search2);
        DropOffLocation[] schedule4 = dropOffLocationFileDAO.getSchedule(search3);

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
        Boolean first = dropOffLocationFileDAO.addDropOffLocation(d1);
        Boolean second = dropOffLocationFileDAO.addDropOffLocation(d2);
        Boolean third = dropOffLocationFileDAO.addDropOffLocation(d3);
        Boolean fourth = dropOffLocationFileDAO.addDropOffLocation(d4);
        Boolean fifth = dropOffLocationFileDAO.addDropOffLocation(d5);

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
        Boolean first = dropOffLocationFileDAO.editDropOffLocation(d1);
        Boolean second = dropOffLocationFileDAO.editDropOffLocation(d2);
        Boolean third = dropOffLocationFileDAO.editDropOffLocation(d3);

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
        Boolean first = dropOffLocationFileDAO.deleteDropOffLocation(id1);
        Boolean second = dropOffLocationFileDAO.deleteDropOffLocation(id2);

        ///Analyze
        assertFalse(first);
        assertTrue(second);
    }

    @Test
    public void testvolunteerFunctions() throws IOException {
        ///Setup
        int id = 3;

        ///Invoke
        Boolean first = dropOffLocationFileDAO.volunteer(id, "BillyBob");
        Boolean second = dropOffLocationFileDAO.unVolunteer(id);
        Boolean third = dropOffLocationFileDAO.unVolunteer(id);
        Boolean fourth = dropOffLocationFileDAO.volunteer(id, "BillyBob");

        ///Analyze
        assertFalse(first);
        assertTrue(second);
        assertFalse(third);
        assertTrue(fourth);
    }
}

