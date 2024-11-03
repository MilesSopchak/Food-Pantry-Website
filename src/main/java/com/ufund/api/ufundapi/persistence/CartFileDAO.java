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

import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.model.Need;

@Component
public class CartFileDAO implements CartDAO {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(CartFileDAO.class.getName());
    Map<Integer,Cart> cartPark;
    private ObjectMapper objectMapper;
    private String filename;

    public CartFileDAO(@Value("${cartPark.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the cartPark from the file
    }

    private Cart[] getcartParkArray() {
        ArrayList<Cart> cartArrayList = new ArrayList<>();

        for (Cart cart : cartPark.values()) {
            cartArrayList.add(cart);
        }
        Cart[] cartArray = new Cart[cartArrayList.size()];
        cartArrayList.toArray(cartArray);
        return cartArray;
    }

    private boolean save() throws IOException {
        Cart[] CartArray = getcartParkArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),CartArray);
        return true;
    }

    private boolean load() throws IOException {
        cartPark = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of car
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Cart[] CartArray = objectMapper.readValue(new File(filename),Cart[].class);

        // Add each Cart to the tree map and keep track of the greatest id
        for (Cart Cart : CartArray) {
            cartPark.put(Cart.getId(),Cart);
        }
        return true;
    }

    @Override
    public Boolean createCart(int id) throws IOException {
        synchronized(cartPark) {
            Cart cart = new Cart(id, new Need[0]);
            cartPark.put(cart.getId(), cart);
            return save();
        }
    }

    @Override
    public Boolean addNeed(int cId, int nId, int quantity, NeedDAO needDAO) throws IOException {
        synchronized(cartPark) {
            if(quantity < 1) {
                return false;
            }
            Need need = needDAO.getNeed(nId);
            Cart cart = cartPark.get(cId);
            if(need != null && cart != null) {
                if(quantity > need.getQuantity()) {
                    quantity = need.getQuantity();
                }
                Need needCopy = need.copy();
                needCopy.setQuantity(quantity);
                if(cart.addNeed(needCopy)) {
                    return save();
                }
            }
            return false;
        }
    }

    @Override
    public Boolean updateNeed(int cId, int nId, int quantity, NeedDAO needDAO) throws IOException {
        synchronized(cartPark) {
            if(quantity < 1) {
                return false;
            }
            Need need = needDAO.getNeed(nId);
            Cart cart = cartPark.get(cId);
            if(need != null && cart != null) {
                if(quantity > need.getQuantity()) {
                    quantity = need.getQuantity();
                }
                if(cart.updateNeed(nId, quantity)) {
                    return save();
                }
            }
            return false;
        }
    }

    @Override
    public Boolean deleteNeed(int cId, int nId) throws IOException {
        synchronized(cartPark) {
            Cart cart = cartPark.get(cId);
            if(cart != null) {
                if(cart.deleteNeed(nId)) {
                    return save();
                }
            }
            return false;
        }
    }

    @Override
    public Boolean deleteCart(int id) throws IOException {
        synchronized(cartPark) {
            return cartPark.remove(id) != null && save();
        }
    }

    @Override
    public Need[] getCart(int id) throws IOException {
        synchronized(cartPark) {
            Cart cart = cartPark.get(id);
            if(cart == null) {
                return null;
            }
            return cart.getNeeds();
        }
    }
    
    @Override
    public Boolean checkout(int id, NeedDAO needDAO) throws IOException {
        synchronized(cartPark) {
            Cart cart = cartPark.get(id);
            if(cart == null) {
                return false;
            }
            for (Need n : cart.getNeeds()) {
                Need need = needDAO.getNeed(n.getId());
                if(need != null) {
                    need.setQuantity(need.getQuantity() - n.getQuantity());
                    if(need.getQuantity() < 0) {
                        need.setQuantity(0);
                    }
                    needDAO.updateNeed(need);
                }
            }
            cart.checkout();
            return save();
        }
    }
}