package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.User;

/**
 * Implements the functionality for JSON file-based peristance for Users
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class UserFileDAO implements UserDAO {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    Map<Integer,User> users;   // Provides a local cache of the users
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between User
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new user
    private String filename;    // Filename to read from and write to

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     * and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${user.file}") String filename,
    ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the user map from the file
    }

    /**
     * Generates the next id for a new {@linkplain User user}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain User users} from the tree map
     * 
     * @return  The array of {@link User users}, may be empty
     */
    private User[] getUserArray() {
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            userArrayList.add(user);
        }

        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Saves the {@linkplain User users} from the users map into the file as an
     * array of JSON objects
     * 
     * @return true if the {@link User users} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] userArray = getUserArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),userArray);
        return true;
    }

    /**
     * Loads {@linkplain User users} from the JSON file into the user map
     * 
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of users
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] userArray = objectMapper.readValue(new File(filename),User[].class);

        // Add each user to the tree map and keep track of the greatest id
        for (User user : userArray) {
            users.put(user.getId(),user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public int[] login(String username, String password) throws IOException{
        synchronized(users) {
            int[] user = null;
            for (User u : getUserArray()) {
                user = u.login(username, password);
                if(user != null)
                {
                    save(); // may throw an IOException
                    return user;
                }
            }
            return user;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean logout(int id, int key) throws IOException {
        synchronized(users) {
            User user = users.get(id);
            if(user != null && user.isKey(key))
            {
                user.newKey();
                return save();
            }
            return false;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public int getUserInfo(int id, int key) { 
        synchronized(users) {
            User user = users.get(id);
            if(user != null && user.isKey(key))
            {
                if(user.isAdmin())
                {
                    return 2;
                }
                return 1;
            }
            return 0;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public int[] createUser(String username, String password, CartDAO cartDAO) throws IOException {
        synchronized(users) {
            // We create a new user object because the id field is immutable
            // and we need to assign the next unique id
            for (User u : getUserArray()) {
                if(u.getUsername().equals(username)) {
                    return null;
                }
            }
            User newUser = new User(nextId(), username, password, false);
            int key = newUser.newKey();
            if(cartDAO.createCart(newUser.getId())) {
                users.put(newUser.getId(), newUser);
                save(); // may throw an IOException
                return new int[]{newUser.getId(), key};
            }
            return null;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public int[] createUserAdmin(int id, int key, String username, String password,
    boolean isAdmin) throws IOException
    {
        synchronized(users) {
            User admin = users.get(id);
            if(admin != null && admin.isKey(key) && admin.isAdmin()) {
                for (User u : getUserArray()) {
                    if(u.getUsername().equals(username)) {
                        return null;
                    }
                }
                User newUser = new User(nextId(), username, password, isAdmin);
                key = newUser.newKey();
                users.put(newUser.getId(), newUser);
                save(); // may throw an IOException
                return new int[]{newUser.getId(), key};
            }
            return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean updateUser(int id, int key, String username, String password)
    throws IOException {
        synchronized(users) {
            User oldUser = users.get(id);
            if(oldUser != null && oldUser.isKey(key))
            {
                if(username != null) {
                    oldUser.resetUsername(username);
                }
                if(password != null) {
                    oldUser.resetPassword(password);
                }
                return save();
            }
            return false;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteUser(int id, int key, CartDAO cartDAO) throws IOException {
        synchronized(users) {
            User user = users.get(id);
            if (user != null && user.isKey(key)) {
                users.remove(id);
                cartDAO.deleteCart(id);
                return save();
            }
            else
                return false;
        }
    }
}