package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.User;

/**
 * Defines the interface for User object persistence
 * 
 * @author Miles Sopchak
 */
public interface UserDAO {
    /**
     * Retrieves a {@linkplain User user} with the given username and password.
     * 
     * @param username The username of the {@link User user} to get.
     * 
     * @param password The valid password of the corresponding user.
     * 
     * @return an id, key pair if successful, null otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    int[] login(String username, String password) throws IOException;

    /**
     * Logs a {@linkplain User user} out with the given id and key.
     * 
     * @param id The id of the {@link User user} to logout.
     * 
     * @param key The valid secession key of the {@link User user} to logout.
     * 
     * @return true if sucessful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean logout(int id, int key) throws IOException;

    /**
     * Attempts to find a {@linkplain User user's} given an id and key.
     * 
     * @param id The id of the {@link User user} to get.
     * 
     * @param key The valid secession key for the corresponding {@link User user}.
     * 
     * @return 2 if the user is an admin, 1 if the user is not, and 0 if the user
     * could not be found.
     * 
     * @throws IOException if an issue with underlying storage
     */
    int getUserInfo(int id, int key) throws IOException;

    /**
     * Creates and saves a {@linkplain User user}.
     * 
     * @param username The username of the new {@linkplain User user}.
     * @param password The password of the new {@linkplain User user}.
     * 
     * The id of the user object is ignored and a new uniqe id is assigned.
     *
     * @return an id, key pair if successful, null otherwise.
     * 
     * @throws IOException if an issue with underlying storage.
     */
    int[] createUser(String username, String password, CartDAO cartDAO) throws IOException;

    /**
     * Creates and saves a {@linkplain User user} that can be an admin if the
     * provided Id Key pair is that of an admin user.
     * 
     * @param id The id of an admin user.
     * @param key The secession key of an admin.
     * @param username The username of the new {@linkplain User user}.
     * @param password The password of the new {@linkplain User user}.
     * @param isAdmin The admin status of the new {@linkplain User user}.
     * 
     * The id of the user object is ignored and a new uniqe id is assigned.
     *
     * @return an id, key pair if successful, null otherwise.
     * 
     * @throws IOException if an issue with underlying storage.
     */
    int[] createUserAdmin(int id, int key, String username, String password,
    boolean isAdmin) throws IOException;

    /**
     * Updates and saves the username and password of a {@linkplain User user}.
     * 
     * @param user The user containing the id and the key of the {@link User user}
     * to be updated. It also contains the new username and password.
     * 
     * @return true if sucessful, false if the {@link User user} could not be found.
     * 
     * @throws IOException if underlying storage cannot be accessed.
     */
    boolean updateUser(int id, int key, String username, String password) throws IOException;

    /**
     * Deletes a {@linkplain User user} with the given id and key.
     * 
     * @param id The id of the {@link User user}.
     * @param key The secession key of the {@link User user}.
     * 
     * @return true if the {@link User user} was deleted.
     * 
     * false if user with the given id and key are not valid or do not exist.
     * 
     * @throws IOException if underlying storage cannot be accessed.
     */
    boolean deleteUser(int id, int key, CartDAO cartDAO) throws IOException;
}