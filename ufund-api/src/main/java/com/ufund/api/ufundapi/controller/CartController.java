package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.persistence.CartDAO;
import com.ufund.api.ufundapi.persistence.NeedDAO;
import com.ufund.api.ufundapi.model.Cart;
import com.ufund.api.ufundapi.model.Need;

@SuppressWarnings("unused")
@RestController
@RequestMapping("cart")
public class CartController {
    private static final Logger LOG = 
    Logger.getLogger(CartController.class.getName());
    private CartDAO cartDao;
    private NeedDAO needDao;

    public CartController(CartDAO cartDao, NeedDAO needDao) {
        this.cartDao = cartDao;
        this.needDao = needDao;
    }

     /**
     * Responds to the GET request for a {@linkplain Cart cart} for the given id.
     * 
     * @param id The id used to locate the {@link Cart cart}.
     * 
     * @return ResponseEntity with the {@link Cart cart} object's
     * {@linkplain Need needs} and HTTP status of OK if found.<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found.<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need[]> getCart(@PathVariable int id) {
        LOG.info("GET /cart/" + id);
        try {
            Need[] cart = cartDao.getCart(id);
            if (cart != null)
                return new ResponseEntity<Need[]>(cart,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the POST request for a {@linkplain Cart cart} for the given id
     * and a {@link Need need} with the given id.
     * 
     * @param CNQ An array of two objects:
     *  The first is and integer used to locate the {@link Cart cart}.
     *  The second is a need used to locate the {@link Need need} and then update it
     *      with new information.
     *  The third is and integer that represents the new quantity of the need.
     * 
     * @return ResponseEntity with a boolean representing the sucess of the operation
     * and  HTTP status of OK.<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PostMapping("")
    public ResponseEntity<Boolean> addNeed(@RequestBody int[] CNQ) {
        LOG.info("GET /cart/" + CNQ);
        try {
            Boolean sucess = cartDao.addNeed(CNQ[0], CNQ[1], CNQ[2], needDao);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
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
     * Responds to the PUT request for a {@linkplain Cart cart} for the given id.
     * and a {@link Need need} with the given {@link Need need}.
     * 
     * @param CNQ An array of two objects:
     *  The first is and integer used to locate the {@link Cart cart}.
     *  The second is a need used to locate the {@link Need need} and then update it
     *      with new information.
     *  The third is and integer that represents the new quantity of the need.
     * 
     * @return ResponseEntity with a boolean representing the sucess of the operation
     * and  HTTP status of OK.<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PutMapping("")
    public ResponseEntity<Boolean> updateNeed(@RequestBody int[] CNQ) {
        LOG.info("PUT /cart/" + CNQ);
        try {
            Boolean sucess = cartDao.updateNeed(CNQ[0], CNQ[1], CNQ[2], needDao);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
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
     * Responds to the DELETE request for a {@linkplain Cart cart} for the given id
     * and a {@link Need need} with the given id.
     * 
     * @param CN An array of two integers:
     *  The first is used to locate the {@link Cart cart}.
     *  The second is used to locate the {@link Need need}.
     * 
     * @return ResponseEntity with a boolean representing the sucess of the operation
     * and  HTTP status of OK.<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteNeed(@RequestBody int[] CN) {
        LOG.info("GET /cart/" + CN);
        try {
            Boolean sucess = cartDao.deleteNeed(CN[0], CN[1]);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
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
     * Responds to the PUT request for a {@linkplain Cart cart} for the given id
     * and a {@link Need need} with the given id.
     * 
     * @param id the id of the cart to checkout.
     * 
     * @return ResponseEntity with a boolean representing the sucess of the operation
     * and  HTTP status of OK.<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> checkout(@PathVariable int id) {
        LOG.info("GET /cart/" + id);
        try {
            Boolean sucess = cartDao.checkout(id, needDao);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
