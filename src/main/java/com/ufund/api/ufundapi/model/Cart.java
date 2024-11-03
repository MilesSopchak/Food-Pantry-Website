package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a Cart entity.
 * 
 * @author Oscar Chen oyc2709@rit.edu
 * @author Miles Sopchak
 */
@JsonPropertyOrder({"id, needs"})
public class Cart {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(Cart.class.getName());

    static final String STRING_FORMAT = "Cart [id=%d, needs=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("needs") private Need[] needs;

     /**
     * Create a cart with the given id and array of {@linkplain Need needs}.
     * @param id the id of the {@linkplain Cart needs}.
     * @param needs the array of {@linkplain Need needs} in the cart.
     * 
     * {@literal @JsonProperty} Used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int.
     */
    public Cart(@JsonProperty("id") int id, @JsonProperty() Need[] needs) {
        this.id = id;
        this.needs = needs;
    }
    
    /**
     * Gets the id of the {@linkplain Cart cart}.
     * @return
     */
    public int getId() {return id;}

    /**
     * Gets the array of {@linkplain Need needs} in the {@linkplain Cart cart}.
     * @return The array of {@linkplain Need needs} in the {@linkplain Cart cart}.
     */
    public Need[] getNeeds() {return needs;}

    /**
     * Adds the given {@linkplain Need need} to the {@linkplain Cart cart}.
     * @param n The {@linkplain Need need} to add.
     * @return true if sucessful. false if the cart already contained a
     * {@linkplain Need need} with the given id.
     */
    public Boolean addNeed(Need n) {
        Need[] needArr = new Need[needs.length + 1];
        for (int i = 0; i < needs.length; i++) {
            if(needs[i].getId() == n.getId())
            return false;
            needArr[i] = needs[i];
        }
        needArr[needs.length] = n;
        needs = needArr;
        return true;
    }

    /**
     * Updates a {@linkplain Need need} in the {@linkplain Cart cart}.
     * @param id The id of the {@linkplain Need need} to update.
     * @param quantity The new quantity of the {@linkplain Need need} to update.
     * @return true if sucessful. false if the cart did not contain a
     * {@linkplain Need need} with the given id.
     */
    public Boolean updateNeed(int id, int quantity) {
        for (Need need: needs)
        {
            if(id == need.getId())
            {
                need.setQuantity(quantity);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a {@linkplain Need need} in the {@linkplain Cart cart}.
     * @param id The id of the {@linkplain Need need} to delete.
     * @return true if sucessful. false if the cart did not contain a
     * {@linkplain Need need} with the given id.
     */
    public Boolean deleteNeed(int id) {
        if(needs.length == 0) {
            return false;
        }
        int shift = 0;
        Need[] needArr = new Need[needs.length - 1];
        for (int i = 0; i < needs.length; i++)
        {
            if(id == needs[i].getId()) {
                shift = -1;
            }
            else if(i == needArr.length && shift == 0) {
                return false;
            }
            else {
                needArr[i + shift] = needs[i];
            }
        }
        needs = needArr;
        return true;
    }

    /**
     * Deletes all {@linkplain Need needs} from the cart.
     */
    public void checkout() {
        needs = new Need[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return String.format(STRING_FORMAT, id, needs.toString());
    }
}
