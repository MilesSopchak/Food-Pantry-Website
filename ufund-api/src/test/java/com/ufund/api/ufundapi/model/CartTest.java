package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-teir")
public class CartTest {
    int expected_id;
    Need[] cartNeeds;
    Cart cart;

    @BeforeEach
    public void setupCart() {
        expected_id = 3;
        cartNeeds = new Need[2];

        Need Wendeez = new Need(1, "Wendeez", "Two-Dollah", 2, 100);
        Need frog = new Need(2, "frog", "amphibian", 7, 200);

        cartNeeds[0] = Wendeez;
        cartNeeds[1] = frog;

        cart = new Cart(expected_id, cartNeeds);
    }

    @Test
    public void testgetId() {
        ///Setup
        
        ///Invoke
        int id = cart.getId();

        ///Analyze
        assertEquals(expected_id, id);
    }

    @Test
    public void testgetNeeds() {
        ///Setup
        
        ///Invoke
        Need[] needs = cart.getNeeds();

        ///Analyze
        assertEquals(needs.length, cartNeeds.length);
        assertEquals(needs[0].getId(), cartNeeds[0].getId());
        assertEquals(needs[1].getId(), cartNeeds[1].getId());
    }

    @Test
    public void testaddNeed() {
        ///Setup
        Need newNeed = new Need(3, "Hollow Knight", "Bug", 15, 1);
        
        ///Invoke
        Boolean result = cart.addNeed(newNeed);

        ///Analyze
        assertTrue(result);
        assertEquals(cart.getNeeds().length, 3);
        assertEquals(cart.getNeeds()[2].getId(), newNeed.getId());
    }

    @Test
    public void testupdateNeed() {
        ///Setup
        int id = 1;
        int quantity = 200;
        
        ///Invoke
        Boolean result = cart.updateNeed(id, quantity);

        ///Analyze
        assertTrue(result);
        assertEquals(cart.getNeeds()[0].getId(), id);
        assertEquals(cart.getNeeds()[0].getQuantity(), quantity);
    }

    @Test
    public void testdeleteNeed() {
        ///Setup
        int id = 1;
        
        ///Invoke
        Boolean result = cart.deleteNeed(id);

        ///Analyze
        assertTrue(result);
        assertEquals(cart.getNeeds().length, 1);
        assertNotEquals(cart.getNeeds()[0].getId(), id);
    }

    @Test
    public void testcheckout() {
        ///Setup
        
        ///Invoke
        cart.checkout();

        ///Analyze
        assertEquals(cart.getNeeds().length, 0);
    }

    @Test
    public void testtoString() {
        ///Setup
        String expected_cartString = String.format("Cart [id=%d, needs=%s]",expected_id,cartNeeds.toString());
        
        ///Invoke
        String cartString = cart.toString();

        ///Analyze
        assertEquals(expected_cartString, cartString);
    }
}

