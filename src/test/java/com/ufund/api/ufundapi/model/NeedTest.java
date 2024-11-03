package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-teir")
public class NeedTest{
    @Test
    public void testCtor(){
        ///Setup
        int expected_id = 5;
        String expected_name = "Canned Vegetable";
        String expected_type = "Food";
        int expected_cost = 1;
        int expected_quantity = 200;

        ///Invoke
        Need need = new Need(expected_id, expected_name, expected_type, expected_cost, expected_quantity);

        ///Analyze
        assertEquals(expected_id, need.getId());
        assertEquals(expected_name, need.getName());
        assertEquals(expected_type, need.getType());
        assertEquals(expected_cost,need.getCost());
        assertEquals(expected_quantity, need.getQuantity());
    }

    @Test
    public void testsetName(){
        ///Setup
        int id = 5;
        String name = "Canned Vegetable";
        String type = "Food";
        int cost = 1;
        int quantity = 200;
        Need need = new Need(id, name, type, cost, quantity);

        String expected_name = "Waffles";

        ///Invoke
        need.setName(expected_name);

        ///Analyze
        assertEquals(expected_name, need.getName());

    }

    @Test
    public void testsetType(){
        ///Setup
        int id = 5;
        String name = "Canned Vegetable";
        String type = "Food";
        int cost = 1;
        int quantity = 200;
        Need need = new Need(id, name, type, cost, quantity);
        
        String expected_type = "Canned Food";

        ///Invoke
        need.setType(expected_type);

        ///Analyze
        assertEquals(expected_type,need.getType());
    }

    @Test
    public void testsetCost(){
        ///Setup
        int id = 5;
        String name = "Canned Vegetable";
        String type = "Food";
        int cost = 1;
        int quantity = 200;
        Need need = new Need(id, name, type, cost, quantity);

        int expected_cost = 2;

        ///Invoke
        need.setCost(expected_cost);

        ///Analyze
        assertEquals(expected_cost, need.getCost());
    }

    @Test
    public void testsetQuantity(){
        ///Setup
        int id = 5;
        String name = "Canned Vegetable";
        String type = "Food";
        int cost = 1;
        int quantity = 200;
        Need need = new Need(id, name, type, cost, quantity);

        int expected_quantity = 300;

        ///Invoke
        need.setQuantity(expected_quantity);

        ///Analyze
        assertEquals(expected_quantity, need.getQuantity());
    }

    @Test
    public void testtoString(){
        ///Setup
        int id = 5;
        String name = "Canned Vegetable";
        String type = "Food";
        int cost = 1;
        int quantity = 200;
        String expectedString = String.format(Need.STRING_FORMAT,id,name,type,cost,quantity);
        Need need = new Need(id, name, type, cost, quantity);

        ///Invoke
        String actual_String = need.toString();

        ///Analyze
        assertEquals(expectedString, actual_String);
    }
}

