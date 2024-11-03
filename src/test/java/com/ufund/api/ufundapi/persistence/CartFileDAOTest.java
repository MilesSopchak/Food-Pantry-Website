package com.ufund.api.ufundapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

@Tag("Persistence-teir")
public class CartFileDAOTest {
    CartFileDAO cartFileDAO;
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

        cartFileDAO = new CartFileDAO("Man I Love Cart File Tests :D", mockObjectMapper);
        mockNeedFileDAO = new NeedFileDAO("Man I Love Cart File Tests :D", mockNeedObjectMapper);
    }

    @Test
    public void testcreateCart() throws IOException {
        ///Setup
        int id = 9;
        
        ///Invoke
        Boolean result = cartFileDAO.createCart(id);

        ///Analyze
        assertTrue(result);
    }

    @Test
    public void testaddNeed() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        
        ///Invoke
        Boolean first = cartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        Boolean second = cartFileDAO.addNeed(expected_ids[0], ids[1], 0, mockNeedFileDAO);
        Boolean third = cartFileDAO.addNeed(expected_ids[0], ids[1], 201, mockNeedFileDAO);
        Boolean fourth = cartFileDAO.addNeed(expected_ids[0], ids[2], 100, mockNeedFileDAO);

        ///Analyze
        assertTrue(first);
        assertFalse(second);
        assertTrue(third);
        assertFalse(fourth);
    }

    @Test
    public void testupdateNeed() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        cartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        cartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Boolean first = cartFileDAO.updateNeed(expected_ids[0], ids[0], 30, mockNeedFileDAO);
        Boolean second = cartFileDAO.updateNeed(expected_ids[0], ids[1], 0, mockNeedFileDAO);
        Boolean third = cartFileDAO.updateNeed(expected_ids[0], ids[1], 201, mockNeedFileDAO);
        Boolean fourth = cartFileDAO.updateNeed(expected_ids[0], ids[2], 100, mockNeedFileDAO);

        ///Analyze
        assertTrue(first);
        assertFalse(second);
        assertTrue(third);
        assertFalse(fourth);
    }

    @Test
    public void testdeleteNeed() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        cartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        cartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Boolean first = cartFileDAO.deleteNeed(expected_ids[0], ids[1]);
        Boolean second = cartFileDAO.deleteNeed(expected_ids[0], ids[2]);

        ///Analyze
        assertTrue(first);
        assertFalse(second);
    }

    @Test
    public void testdeleteCart() throws IOException {
        ///Setup
        int id = 3;

        ///Invoke
        Boolean first = cartFileDAO.deleteCart(expected_ids[0]);
        Boolean second = cartFileDAO.deleteCart(id);

        ///Analyze
        assertTrue(first);
        assertFalse(second);
    }

    @Test
    public void testgetCart() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        cartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        cartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Need[] first = cartFileDAO.getCart(expected_ids[0]);
        Need[] second = cartFileDAO.getCart(ids[2]);

        ///Analyze
        assertNotNull(first);
        assertEquals(2, first.length);
        assertNull(second);
    }

    @Test
    public void testcheckout() throws IOException {
        ///Setup
        int[] ids = new int[]{1,2,3};
        cartFileDAO.addNeed(expected_ids[0], ids[0], 20, mockNeedFileDAO);
        cartFileDAO.addNeed(expected_ids[0], ids[1], 100, mockNeedFileDAO);
        
        ///Invoke
        Boolean first = cartFileDAO.checkout(expected_ids[0], mockNeedFileDAO);
        Boolean second = cartFileDAO.checkout(expected_ids[1], mockNeedFileDAO);
        Boolean third = cartFileDAO.checkout(ids[2], mockNeedFileDAO);

        ///Analyze
        assertTrue(first);
        assertTrue(second);
        assertFalse(third);
    }
}
