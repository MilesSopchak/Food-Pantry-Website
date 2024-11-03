package com.ufund.api.ufundapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Need entity.
 * 
 * @author Miles Sopchak
 */
public class Need {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    static final String STRING_FORMAT = "Need [id=%d, name=%s, type=%s, cost=%d, quantity=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("type") private String type;
    @JsonProperty("cost") private int cost;
    @JsonProperty("quantity") private int quantity;

    /**
     * Creates a {@linkplain Need need} with the provided information.
     * @param id The id of the {@linkplain Need need}.
     * @param name The name of the {@linkplain Need need}.
     * @param type The type (catagory) of the {@linkplain Need need}.
     * @param cost The cost of the {@linkplain Need need}.
     * @param quantity The quantity of the {@linkplain Need need}.
     * 
     * {@literal @JsonProperty} Used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int.
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") String type,
    @JsonProperty("cost") int cost, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
        this.type = type;
    }

    /**
     * Gets the id of the {@linkplain Need need}.
     * @return The id of the {@linkplain Need need}.
     */
    public int getId() {return id;}

    /**
     * Sets the name of the need - necessary for JSON object to Java object deserialization
     * @param name The name of the need
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the need
     * @return The name of the need
     */
    public String getName() {return name;}

    /**
     * Retrieves the type of the need
     * @return The type of the need
     */
    public String getType() {return type;}

    /**
     * Sets the type of the need - necessary for JSON object to Java object deserialization
     * @param name The type of the need
     */
    public void setType(String type) {this.type = type;}

    /**
     * Retrieves the cost of the need
     * @return The cost of the need
     */
    public int getCost() {return cost;}

    /**
     * Retrieves the quantity of the need
     * @return The quantity of the need
     */
    public int getQuantity() {return quantity;}

    /**
     * Sets the cost of the need - necessary for JSON object to Java object deserialization
     * @param cost The cost of the need
     */
    public void setCost(int cost) {this.cost = cost;}

    /**
     * Sets the quantity of the need - necessary for JSON object to Java object deserialization
     * @param quantity The quantity of the need
     */
    public void setQuantity(int quantity) {this.quantity = quantity;}

    public Need copy() {
        return new Need(id, name, type, cost, quantity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,type,cost,quantity);
    }
}