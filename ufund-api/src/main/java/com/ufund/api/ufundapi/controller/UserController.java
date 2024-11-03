package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.persistence.CartDAO;
import com.ufund.api.ufundapi.persistence.UserDAO;
//import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.ufund.api.ufundapi.model.User;

/**
 * Handles the REST API requests for the User resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a
 * REST API
 * method handler to the Spring framework
 * 
 * @author Miles Sopchak
 */

@SuppressWarnings("unused")
@RestController
public class UserController {
    private static final Logger LOG = Logger.getLogger(
        UserController.class.getName());
    private UserDAO userDao;
    private CartDAO cartDao;

    /**
     * Creates a REST API controller to reponds to requests.
     * 
     * @param userDao The {@link UserDAO User Data Access Object} to perform
     * CRUD operations.
     * 
     * This dependency is injected by the Spring Framework.
     */
    public UserController(UserDAO userDao, CartDAO cartDao) {
        this.userDao = userDao;
        this.cartDao = cartDao;
    }

    /**
     * Responds to the GET request for a {@linkplain User User} for the given
     * username and password.
     * 
     * @param username The username of the {@link User user}.
     * @param password The password of the {@link User user}.
     * 
     * @return ResponseEntity with {@link User user} object and HTTP status of OK
     * if found.
     * ResponseEntity with HTTP status of NOT_FOUND if not found.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PostMapping("/login")
    public ResponseEntity<int[]> login(@RequestBody String[] UnPw) {
        LOG.info("POST /login/" + UnPw);
        try {
            int[] user = userDao.login(UnPw[0], UnPw[1]);
            if (user != null)
                return new ResponseEntity<int[]>(user,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Responds to the POST request for a {@linkplain User User} for the given
     * id and password.
     * 
     * @param id The id of the {@link User user} to logout.
     * @param key The key of the {@link User user} to logout.
     * 
     * @return ResponseEntity with HTTP status of OK
     * if found.
     * ResponseEntity with HTTP status of NOT_FOUND if not found.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestBody int[] IdKey) {
        LOG.info("POST /logout/" + IdKey);
        try {
            boolean sucess = userDao.logout(IdKey[0], IdKey[1]);
            if (sucess)
                return new ResponseEntity<Boolean>(sucess, HttpStatus.OK);
            else
                return new ResponseEntity<Boolean>(sucess, HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates a {@linkplain User user} with the provided user object.
     * 
     * @param username The username of the new {@linkplain User user}.
     * @param password The password of the new {@linkplain User user}.
     * 
     * @return ResponseEntity with the id, key pair of the created
     * {@linkplain User user} object and HTTP status of CREATED.
     * ResponseEntity with HTTP status of CONFLICT if {@linkplain User user}
     * object already exists.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PostMapping("/signup")
    public ResponseEntity<int[]> createUser(@RequestBody String[] UnPw) {
        LOG.info("POST /signup/" + UnPw);
        try
        {
            int[] user = userDao.createUser(UnPw[0], UnPw[1], cartDao);
            if( user == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<int[]>(user,HttpStatus.CREATED);
        }
        catch(IOException e)
        {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Creates a {@linkplain User user} with the provided username and password.
     * 
     * @param username The username of the new {@linkplain User user}.
     * @param password The password of the new {@linkplain User user}.
     * 
     * @return ResponseEntity with the id, key pair of the created
     * {@linkplain User user} object and HTTP status of CREATED.
     * ResponseEntity with HTTP status of CONFLICT if {@linkplain User user}
     * object already exists.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PostMapping("/signup/admin")
    public ResponseEntity<int[]> createUserAdmin(@RequestBody Object[] IdKeyUnPwIA) {
        LOG.info("POST /signup/admin/" + IdKeyUnPwIA);
        try
        {
            int[] user = userDao.createUserAdmin((int)IdKeyUnPwIA[0], (int)IdKeyUnPwIA[1],
            (String)IdKeyUnPwIA[2], (String)IdKeyUnPwIA[3], (boolean)IdKeyUnPwIA[4]);
            if( user == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<int[]>(user,HttpStatus.CREATED);
        }
        catch(IOException e)
        {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates the {@linkplain User user's} username and password with the provided
     * {@linkplain User user} object, if it exists.
     * 
     * @param user The {@link User user} to update
     * 
     * @return ResponseEntity with  HTTP status of OK if updated.
     * ResponseEntity with HTTP status of NOT_FOUND if not found.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PutMapping("/user/edit")
    public ResponseEntity<Boolean> updateUser(@RequestBody Object[] IdKeyUnPw) {
        LOG.info("PUT /login/" + IdKeyUnPw);
        try {
            boolean sucess = userDao.updateUser((int)IdKeyUnPw[0], (int)IdKeyUnPw[1], (String)IdKeyUnPw[2], (String)IdKeyUnPw[3]);
            if (sucess)
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes a {@linkplain User user} with the given id and key.
     * 
     * @param id The id of the {@link User user} to deleted.
     * @param key The key of the {@link User user} to deleted.
     * 
     * @return ResponseEntity HTTP status of OK if deleted.
     * ResponseEntity with HTTP status of NOT_FOUND if not found.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @DeleteMapping("/user/edit")
    public ResponseEntity<Boolean> deleteUser(@RequestBody int[] IdKey) {
        LOG.info("DELETE /user/edit/" + IdKey);
        try {
            boolean sucess = userDao.deleteUser(IdKey[0], IdKey[1], cartDao);
            if (sucess)
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            else
                return new ResponseEntity<Boolean>(sucess,HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets a {@linkplain User user's} admin status with the given id and key.
     * 
     * @param id The id of the {@link User user} to check.
     * @param key The key of the {@link User user} to check.
     * 
     * @return ResponseEntity with a boolean that is true if the {@link User user} is
     * an admin and HTTP status of OK.
     * ResponseEntity with HTTP status of NOT_FOUND if not found.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PostMapping("/user")
    public ResponseEntity<Integer> getUserInfo(@RequestBody int[] IdKey) {
        LOG.info("POST /user/" + IdKey);
        try {
            int userInfo = userDao.getUserInfo(IdKey[0], IdKey[1]);
            return new ResponseEntity<Integer>(userInfo, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}