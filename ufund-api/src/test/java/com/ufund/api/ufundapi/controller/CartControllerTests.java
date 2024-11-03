package com.ufund.api.ufundapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.CartFileDAO;
import com.ufund.api.ufundapi.persistence.NeedFileDAO;

@SuppressWarnings("null")
@Tag("Controller-tier")
public class CartControllerTests {
    CartController cartController;
    CartFileDAO mockCartFileDAO;
    NeedFileDAO mockNeedFileDAO;
    ObjectMapper mockObjectMapper;
    ObjectMapper mockNeedObjectMapper;
    
    int[] expected_ids;
    static Cart[] carts;
    static Need[] needs;
    
    @BeforeEach
    public void setUpCartFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);

        expected_ids = new int[]{1,2};
        carts = new Cart[2];

        Cart broken = new Cart(expected_ids[0], new Need[0]);
        Cart notbroken = new Cart(expected_ids[1], new Need[0]);

        carts[0] = broken;
        carts[1] = notbroken;

        when(mockObjectMapper
            .readValue(new File("Man I Love Cart File Tests :D"), Cart[].class))
            .thenReturn(carts);

        doAnswer((i) -> {
            Cart[] updatedCarts = i.getArgument(1);
            carts = new Cart[updatedCarts.length];
            for(int j = 0; j < updatedCarts.length; j++) {
                carts[j] = updatedCarts[j];
            }
            return null;
        }).when(mockObjectMapper).writeValue(new File("Man I Love Cart File Tests :D"), Cart[].class);

        mockNeedObjectMapper = mock(ObjectMapper.class);

        expected_ids = new int[]{1,2};
        needs = new Need[2];

        Need Wendeez = new Need(1, "Wendeez", "Two-Dollah", 2, 100);
        Need frog = new Need(2, "frog", "amphibian", 7, 200);

        needs[0] = Wendeez;
        needs[1] = frog;

        when(mockNeedObjectMapper
            .readValue(new File("Man I Love Cart File Tests :D"), Need[].class))
            .thenReturn(needs);

        doAnswer((i) -> {
            Need[] updatedNeeds = i.getArgument(1);
            needs = new Need[updatedNeeds.length];
            for(int j = 0; j < updatedNeeds.length; j++) {
                needs[j] = updatedNeeds[j];
            }
            return null;
        }).when(mockNeedObjectMapper).writeValue(new File("Man I Love Cart File Tests :D"), Need[].class);

        mockCartFileDAO = new CartFileDAO("Man I Love Cart File Tests :D", mockObjectMapper);
        mockNeedFileDAO = new NeedFileDAO("Man I Love Cart File Tests :D", mockNeedObjectMapper);
        cartController = new CartController(mockCartFileDAO, mockNeedFileDAO);
    }

    @Test
    public void testaddNeed() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        
        ///Invoke
        Boolean first = cartController.addNeed(new int[]{expected_ids[0], ids[0], 20}).getBody();
        Boolean second = cartController.addNeed(new int[]{expected_ids[0], ids[1], 0}).getBody();
        Boolean third = cartController.addNeed(new int[]{expected_ids[0], ids[1], 201}).getBody();
        Boolean fourth = cartController.addNeed(new int[]{expected_ids[0], ids[2], 100}).getBody();

        ///Analyze
        assertTrue(first);
        assertNull(second);
        assertTrue(third);
        assertNull(fourth);
    }

    @Test
    public void testupdateNeed() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        mockCartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        mockCartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Boolean first = cartController.updateNeed(new int[]{expected_ids[0], ids[0], 30}).getBody();
        Boolean second = cartController.updateNeed(new int[]{expected_ids[0], ids[1], 0}).getBody();
        Boolean third = cartController.updateNeed(new int[]{expected_ids[0], ids[1], 201}).getBody();
        Boolean fourth = cartController.updateNeed(new int[]{expected_ids[0], ids[2], 100}).getBody();

        ///Analyze
        assertTrue(first);
        assertNull(second);
        assertTrue(third);
        assertNull(fourth);
    }

    @Test
    public void testdeleteNeed() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        mockCartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        mockCartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Boolean first = cartController.deleteNeed(new int[]{expected_ids[0], ids[1]}).getBody();
        Boolean second = cartController.deleteNeed(new int[]{expected_ids[0], ids[2]}).getBody();

        ///Analyze
        assertTrue(first);
        assertNull(second);
    }

    @Test
    public void testgetCart() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        mockCartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        mockCartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Need[] first = cartController.getCart(expected_ids[0]).getBody();
        Need[] second = cartController.getCart(ids[2]).getBody();

        ///Analyze
        assertNotNull(first);
        assertEquals(2, first.length);
        assertNull(second);
    }

    @Test
    public void testcheckout() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        mockCartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        mockCartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Boolean first = cartController.checkout(expected_ids[0]).getBody();
        Boolean second = cartController.checkout(expected_ids[1]).getBody();
        Boolean third = cartController.checkout(ids[2]).getBody();

        ///Analyze
        assertTrue(first);
        assertTrue(second);
        assertNull(third);
    }
}
