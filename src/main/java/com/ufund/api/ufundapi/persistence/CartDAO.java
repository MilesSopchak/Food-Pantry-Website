package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.model.Need;

/**
 * Defines the interface for Cart object persistence.
 * 
 * @author Oscar Chen oyc2709@rit.edu
 * @author Miles Sopchak
 */
public interface CartDAO {
    /**
     * Creates and saves a {@linkplain Cart cart}.
     * 
     * @param id The id of the {@linkplain Cart cart} object to be created and saved.
     * 
     * @return true if successful, false otherwise.
     * 
     * @throws IOException if an issue with underlying storage.
     */
    Boolean createCart(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Need need} to the {@linkplain Cart cart}.
     * 
     * @param cId The id of the {@linkplain Cart cart}.
     * 
     * @param nId The id of the {@link Need need} object to be added.
     * 
     * @param {@link Need need} object to be created and saved.
     * 
     * @return true if successful, false otherwise.
     * 
     * @throws IOException if underlying storage cannot be accessed.
     */
    Boolean addNeed(int cId, int nId, int quantity, NeedDAO needDAO) throws IOException;

    /**
     * Updates and saves a {@linkplain Need need} in the {@linkplain Cart cart}.
     * 
     * @param id The id of the {@linkplain Cart cart}.
     * 
     * @param {@link Need need} object to be updated and saved.
     * 
     * @return true if successful, false otherwise.
     * 
     * @throws IOException if underlying storage cannot be accessed.
     */
    Boolean updateNeed(int cId, int nId, int quantity, NeedDAO needDAO) throws IOException;

    /**
     * Deletes a {@linkplain Need need} from the {@linkplain Cart cart}.
     * 
     * @param cId The id of the {@linkplain Cart cart}.
     * 
     * @param nId The id of the {@link Need need} object to be deleted.
     * 
     * @return true if successful, false otherwise.
     * 
     * @throws IOException if underlying storage cannot be accessed.
     */
    Boolean deleteNeed(int cId, int nId) throws IOException;

    /**
     * Deletes the {@linkplain Cart cart} with the given id.
     * 
     * @param id The id of the {@linkplain Cart cart}.
     * 
     * @return true if successful, false otherwise.
     * 
     * @throws IOException if underlying storage cannot be accessed.
     */
    Boolean deleteCart(int id) throws IOException;

    /**
     * Retrieves the needs of a {@linkplain Cart cart} with the given id.
     * 
     * @param id the id of the {@linkplain Cart cart}.
     * 
     * @return An array of {@linkplain Need needs} if successful, null otherwise.
     * 
     * @throws IOException if underlying storage cannot be accessed.
     */
    Need[] getCart(int id) throws IOException;

    /**
     * Implements a checkout functionality for the {@linkplain Cart cart}.
     * 
     * @param id the id of the {@linkplain Cart cart}.
     * 
     * @return true if successful, false otherwise.
     * 
     * @throws IOException
     */
    Boolean checkout(int id, NeedDAO needDAO) throws IOException;
}