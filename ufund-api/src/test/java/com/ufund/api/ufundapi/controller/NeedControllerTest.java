package com.ufund.api.ufundapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.NeedDAO;

@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockNeedDAO;


    @BeforeEach
    public void setupNeedController(){
        mockNeedDAO = mock(NeedDAO.class);
        needController = new NeedController(mockNeedDAO);
    }

    @Test
    public void testgetNeed() throws IOException{
        ///Setup
        Need need = new Need(5, "Canned Vegtables", "Food", 1, 200);
        when(mockNeedDAO.getNeed(need.getId())).thenReturn(need);

        ///Invoke
        ResponseEntity<Need> response = needController.getNeed(need.getId());

        ///Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testgetNeedNotFound() throws Exception{
        ///Setup
        int Needid = 5;
        when(mockNeedDAO.getNeed(Needid)).thenReturn(null);

        ///Invoke
        ResponseEntity<Need> response = needController.getNeed(Needid);

        ///Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testgetNeedHandleException() throws Exception{
        ///Setup
        int Needid = 5;
        doThrow(new IOException()).when(mockNeedDAO).getNeed(Needid);

        ///Invoke
        ResponseEntity<Need> response = needController.getNeed(Needid);

        ///Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testgetCupboard() throws IOException{
        ///Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(5, "Canned Vegtables", "Food", 1, 200);
        needs[1] = new Need(6, "Canned Sausage", "Food", 3, 300);
        when(mockNeedDAO.getCupboard()).thenReturn(needs);

        ///Invoke
        ResponseEntity<Need[]> response = needController.getCupboard();

        ///Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testgetCupboardHandleException() throws IOException{
        ///Setup
        doThrow(new IOException()).when(mockNeedDAO).getCupboard();

        ///Invoke
        ResponseEntity<Need[]> response = needController.getCupboard();

        ///Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testsearchCupboard() throws IOException{
        ///Setup
        String stringSearch = "Canned Vegtables";
        Need[] needs = new Need[2];
        needs[0] = new Need(5, "Canned Vegtables", "Food", 1, 200);
        needs[1] = new Need(6, "Canned Sausage", "Food", 3, 300);
        when(mockNeedDAO.findNeeds(stringSearch)).thenReturn(needs);

        ///Invoke
        ResponseEntity<Need[]> response = needController.searchCupboard(stringSearch);

        ///Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(needs, response.getBody());
    }

    @Test
    public void testsearchCupboardHandleException() throws IOException{
        ///Setup
        String stringSearch = "Cannned Sausage";
        doThrow(new IOException()).when(mockNeedDAO).findNeeds(stringSearch);

        ///Invoke
        ResponseEntity<Need[]> response = needController.searchCupboard(stringSearch);

        ///Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testcreateNeed() throws IOException{
        ///Setup
        Need need = new Need(5, "Canned Vegtables", "Food", 1, 200);
        Need[] newneeds = new Need[2];
        newneeds[0] = new Need(6, "Canned Fruit", "Food", 2, 250);
        newneeds[1] = new Need(7, "Canned Sausage", "Food", 3, 300);
        when(mockNeedDAO.getCupboard()).thenReturn(newneeds);
        when(mockNeedDAO.createNeed(need)).thenReturn(need);

        ///Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        ///Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testcreateNeedHandleException() throws IOException{
        ///Setup
        Need need = new Need(5, "Canned Vegtables", "Food", 1, 200);
        Need[] newneeds = new Need[2];
        newneeds[0] = new Need(6, "Canned Fruit", "Food", 2, 250);
        newneeds[1] = new Need(7, "Canned Sausage", "Food", 3, 300);
        when(mockNeedDAO.getCupboard()).thenReturn(newneeds);
        doThrow(new IOException()).when(mockNeedDAO).createNeed(need);

        ///Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        ///Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testcreateNeedFailed() throws IOException{
        ///Setup
        Need need = new Need(5, "Canned Vegtables", "Food", 1, 200);
        Need[] newneeds = new Need[2];
        newneeds[0] = new Need(5, "Canned Vegtables", "Food", 1, 200);
        newneeds[1] = new Need(6, "Canned Sausage", "Food", 3, 300);
        when(mockNeedDAO.getCupboard()).thenReturn(newneeds);
        when(mockNeedDAO.createNeed(need)).thenReturn(null);

        ///Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        ///Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testupdateNeed() throws IOException{
        ///Setup
        Need need = new Need(5, "Canned Vegtables", "Food", 1, 200);
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setQuantity(100);
        
        ///Invoke
        response = needController.updateNeed(need);

        ///Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testupdateNeedFailed() throws IOException{
        ///Setup
        Need need = new Need(5, "Canned Vegtables", "Food", 1, 200);
        when(mockNeedDAO.updateNeed(need)).thenReturn(null);

        ///Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        ///Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testupdateNeedHandleException() throws IOException{
        ///Setup
        Need need = new Need(5, "Canned Vegtables", "Food", 1, 200);
        doThrow(new IOException()).when(mockNeedDAO).updateNeed(need);

        ///Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        ///Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testdeleteNeed() throws IOException{
        ///Setup
        int Needid = 5;
        when(mockNeedDAO.deleteNeed(Needid)).thenReturn(true);

        ///Invoke
        ResponseEntity<Need> response = needController.deleteNeed(Needid);

        ///Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testdeleteNeedNotFound() throws IOException{
        ///Setup
        int Needid = 5;
        when(mockNeedDAO.deleteNeed(Needid)).thenReturn(false);

        ///Invoke
        ResponseEntity<Need> response = needController.deleteNeed(Needid);

        ///Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testdeleteNeedHandleException() throws IOException{
        ///Setup
        int Needid = 5;
        doThrow(new IOException()).when(mockNeedDAO).deleteNeed(Needid);

        ///Invoke
        ResponseEntity<Need> response = needController.deleteNeed(Needid);

        ///Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
        


}
