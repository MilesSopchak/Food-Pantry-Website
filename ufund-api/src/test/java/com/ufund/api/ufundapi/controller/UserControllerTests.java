package com.ufund.api.ufundapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.CartFileDAO;
import com.ufund.api.ufundapi.persistence.UserFileDAO;

@SuppressWarnings("null")
@Tag("Controller-tier")
public class UserControllerTests {
    private UserController userController;
    private UserFileDAO mockUserFileDAO;
    private CartFileDAO mockCartFileDAO;
    ObjectMapper mockObjectMapper;
    User[] testUsers;
    
    int expected_id;
    String expected_username;
    String expected_password;
    int expected_key;
    boolean expected_isAdmin;
    
    @BeforeEach
    public void setUpUserFileDAO() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);
        mockCartFileDAO = mock(CartFileDAO.class);
        testUsers = new User[4];

        User admin = new User(1, "admin", "admin", true);
        User billy = new User(2, "billy", "bob", false);
        User Scooby = new User(3, "Scooby", "Doo", false);

        testUsers[0] = admin;
        testUsers[1] = billy;
        testUsers[2] = Scooby;
        testUsers[3] = Scooby;

        when(mockObjectMapper
            .readValue(new File("Man I Love User File Tests :D"), User[].class))
            .thenReturn(testUsers);

        doAnswer((i) -> {
            User[] users = i.getArgument(1);
            for(int j = 0; j < users.length; j++) {
                testUsers[j] = users[j];
            }
            return null;
        }).when(mockObjectMapper).writeValue(new File("Man I Love User File Tests :D"), User[].class);

        when(mockCartFileDAO
            .createCart(4))
            .thenReturn(true);

        when(mockCartFileDAO
            .deleteCart(4))
            .thenReturn(true);

        mockUserFileDAO = new UserFileDAO("Man I Love User File Tests :D", mockObjectMapper);
        userController = new UserController(mockUserFileDAO, mockCartFileDAO);

        expected_id = 3;
        expected_username = "Scooby";
        expected_password = "Doo";
        expected_key = 0;
        expected_isAdmin = false;
    }



    @Test
    public void testcreateUser() throws IOException {
        ///Setup
        expected_id = 4;
        expected_username = "Billy";
        expected_password = "Bob";
        expected_key = 0;
        expected_isAdmin = false;
        
        ///Invoke
        int[] IdKey = userController.createUser(new String[]{expected_username, expected_password}).getBody();

        ///Analyze
        assertTrue(IdKey != null);
        assertTrue(IdKey.length == 2);
        assertEquals(expected_id, IdKey[0]);
    }

    @Test
    public void testlogin() throws IOException {
        ///Setup
        int[] IdKey = null;

        ///Invoke
        IdKey = userController.login(new String[]{expected_username, expected_password}).getBody();

        ///Analyze
        assertTrue(IdKey != null);
        assertTrue(IdKey.length == 2);
        assertTrue(IdKey[0] == testUsers[3].getId());
        assertTrue(testUsers[3].isKey(IdKey[1]));
    }

    @Test
    public void testlogout() throws IOException {
        ///Setup
        int[] IdKey = null;
        IdKey = userController.login(new String[]{expected_username, expected_password}).getBody();

        ///Invoke
        Boolean result = userController.logout(new int[]{IdKey[0], IdKey[1]}).getBody();

        ///Analyze
        assertTrue(result);
        assertFalse(testUsers[2].isKey(IdKey[1]));
    }

    @Test
    public void testgetUserInfo() throws IOException {
        ///Setup
        int[] notuser = new int[]{0,1};
        int[] user = userController.login(new String[]{expected_username, expected_password}).getBody();
        int[] admin = userController.login(new String[]{"admin", "admin"}).getBody();

        ///Invoke
        int first = userController.getUserInfo(new int[]{notuser[0], notuser[1]}).getBody();
        int second = userController.getUserInfo(new int[]{user[0], user[1]}).getBody();
        int third = userController.getUserInfo(new int[]{admin[0], admin[1]}).getBody();

        ///Analyze
        assertEquals(first, 0);
        assertEquals(second, 1);
        assertEquals(third, 2);
    }

    @Test
    public void testcreateUserAdmin() throws IOException {
        ///Setup
        expected_id = 4;
        expected_username = "Billy";
        expected_password = "Bob";
        expected_key = 0;
        expected_isAdmin = true;
        int[] admin = userController.login(new String[]{"admin", "admin"}).getBody();
        
        ///Invoke
        int[] IdKey = userController.createUserAdmin(new Object[]{admin[0], admin[1], expected_username, expected_password, expected_isAdmin}).getBody();

        ///Analyze
        assertTrue(IdKey != null);
        assertTrue(IdKey.length == 2);
        assertEquals(expected_id, IdKey[0]);
    }

    @Test
    public void testupdateUser() throws IOException {
        ///Setup
        String newUsername = "Bart";
        String newPassword = "Plart";
        int[] IdKey = null;
        IdKey = userController.login(new String[]{expected_username, expected_password}).getBody();

        ///Invoke
        Boolean result = userController.updateUser(new Object[]{IdKey[0], IdKey[1], newUsername, newPassword}).getBody();

        ///Analyze
        assertTrue(result);
    }

	@Test
    public void testdeleteUser() throws IOException {
        ///Setup
        int[] IdKey = null;
        IdKey = userController.login(new String[]{expected_username, expected_password}).getBody();

        ///Invoke
        Boolean result = userController.deleteUser(new int[]{IdKey[0], IdKey[1]}).getBody();

        ///Analyze
        assertTrue(result);
    }
}