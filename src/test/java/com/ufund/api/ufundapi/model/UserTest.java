package com.ufund.api.ufundapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-teir")
public class UserTest {
    int expected_id;
    String expected_username;
    String expected_password;
    int expected_key;
    boolean expected_isAdmin;

    @BeforeEach
    public void setupUser() {
        expected_id = 3;
        expected_username = "BillyBob";
        expected_password = "TreesAreAwesome!!!";
        expected_key = 1;
        expected_isAdmin = true;
    }

    private String hashPassword(String password) {
        MessageDigest md = null;
        try {md = MessageDigest.getInstance("MD5");}
        catch(NoSuchAlgorithmException e) {return e.getMessage();}
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Test
    public void testFileConstructor() {
        ///Setup
        
        ///Invoke
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);

        ///Analyze
        assertEquals(expected_id, user.getId());
        assertEquals(expected_username, user.getUsername());
        assertTrue(user.isKey(expected_key));
        assertEquals(expected_isAdmin, user.isAdmin());
    }

    @Test
    public void testMainConstructor() {
        ///Setup
        
        ///Invoke
        User user = new User(expected_id, expected_username, expected_password, expected_isAdmin);

        ///Analyze
        assertEquals(expected_id, user.getId());
        assertEquals(expected_username, user.getUsername());
        assertEquals(expected_isAdmin, user.isAdmin());
    }

    @Test
    public void testnewKey() {
        ///Setup
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);

        ///Invoke
        int key = user.newKey();

        ///Analyze
        assertTrue(user.isKey(key));
        assertFalse(key == expected_key);
    }

    @Test
    public void testlogin() {
        ///Setup
        User user = new User(expected_id, expected_username, hashPassword(expected_password), expected_key, expected_isAdmin);
        int[] result = null;

        ///Invoke
        result = user.login(expected_username, expected_password);

        ///Analyze
        assertTrue(result != null);
        assertTrue(result[0] == user.getId());
        assertTrue(user.isKey(result[1]));
        assertFalse(user.isKey(expected_key));
    }

    @Test
    public void testisKey() {
        ///Setup
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);
        
        ///Invoke

        ///Analyze
        assertTrue(user.isKey(expected_key));
    }

    @Test
    public void testisAdmin() {
        ///Setup
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);
        
        ///Invoke


        ///Analyze
        assertTrue(user.isAdmin() == expected_isAdmin);
    }

    @Test
    public void testresetUsername() {
        ///Setup
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);
        String newUsername = "BobBilly";
        
        ///Invoke
        user.resetUsername(newUsername);

        ///Analyze
        assertEquals(user.getUsername(), newUsername);
        assertNotEquals(expected_username, user.getUsername());
    }

    @Test
    public void testresetPassword() {
        ///Setup
        User user = new User(expected_id, expected_username, hashPassword(expected_password), expected_key, expected_isAdmin);
        String newPassword = "ManILoveTrees";
        int[] result = null;
        
        ///Invoke
        user.resetPassword(newPassword);

        ///Analyze
        assertNull(user.login(expected_username, expected_password));
        result = user.login(expected_username, newPassword);
        assertTrue(result != null);
        assertTrue(result[0] == user.getId());
        assertTrue(user.isKey(result[1]));
    }

    @Test
    public void testgetId() {
        ///Setup
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);
        
        ///Invoke
        int id = user.getId();

        ///Analyze
        assertTrue(expected_id == id);
    }

    @Test
    public void testgetUsername() {
        ///Setup
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);
        
        ///Invoke
        String username = user.getUsername();

        ///Analyze
        assertTrue(expected_username == username);
    }

    @Test
    public void testtoString() {
        ///Setup
        User user = new User(expected_id, expected_username, expected_password, expected_key, expected_isAdmin);
        String expected_userString = String.format("User [id=%d, username=%s, key=%d, password=%s, isAdmin=%b]",expected_id,expected_username,expected_key,expected_password,expected_isAdmin);
        
        ///Invoke
        String userString = user.toString();

        ///Analyze
        assertEquals(expected_userString, userString);
    }
}

