package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User entity.
 * 
 * @author Miles Sopchak
 */
public class User {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = 
    "User [id=%d, username=%s, key=%d, password=%s, isAdmin=%b]";

    @JsonProperty("username") private String username;
    @JsonProperty("id") private int id;
    @JsonProperty("key") private int key;
    @JsonProperty("password") private String password;
    @JsonProperty("isAdmin") private boolean isAdmin;

    /**
     * Create a user with the given id, username, and password.
     * @param id The id of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param key This is the secession key of the user. This param dosen't mater
     * because the key is randomized anyway.
     * @param isAdmin The admin status of the user.
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int.
     */
    public User(@JsonProperty("id") int id, @JsonProperty("username")
    String username, @JsonProperty("password") String password,
    @JsonProperty("key") int key, @JsonProperty("isAdmin") boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.key = key;
        this.isAdmin = isAdmin;
    }

    public User(int id, String username, String password, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = hashPassword(password);
        newKey();
        this.isAdmin = isAdmin;
    }

    /**
     * Hashes the given password.
     * @param password The password to hash.
     * @return The hashed password.
     */
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

    /**
     * Generates a random key for user authentication.
     * @return The new generated key.
     */
    public int newKey() {
        Random rd = new Random();
        int key = rd.nextInt(2147483647);
        while (this.key == key) {
            key = rd.nextInt(2147483647);
        }
        this.key = key;
        return key;
    }

    /**
     * Verifies username and password credentials then if they are valid, creates a
     * new secession key.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A id, key pait if the credentials are correct, null otherwise.
     */
    public int[] login(String username, String password)
    {
        if(username.equals(this.username) && hashPassword(password).equals(this.password)) {
            return new int[]{id, newKey()};
        }
        return null;
    }

    /**
     * Checks to see if the provided key is valid.
     * @param key The key to check.
     * @return True if the keys match, false otherwise.
     */
    public boolean isKey(int key) {
        return key == this.key;
    }

    /**
     * Gets isAdmin.
     * @return true if user is an admin, false otherwise.
     */
    public boolean isAdmin() {
        return isAdmin;
    }
    
    /**
     * Changes the username to the provided
     * username.
     * @param username The new username.
     */
    public void resetUsername(String username)
    {
        this.username = username;
    }

    /**
     * Changes the password to the provided
     * password.
     * @param password The new password.
     */
    public void resetPassword(String password)
    {
        this.password = hashPassword(password);
    }

    /**
     * Gets the id of the user.
     * @return the id of the user.
     */
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,username,key,password,isAdmin);
    }
}