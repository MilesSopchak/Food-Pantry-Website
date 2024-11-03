package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.DropOffLocation;
import com.ufund.api.ufundapi.persistence.DropOffLocationDAO;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

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
@RequestMapping("schedule")
public class DropOffLocationController {
    private static final Logger LOG = Logger.getLogger(
        DropOffLocationController.class.getName());
    private DropOffLocationDAO dropOffLocationDao;

    public DropOffLocationController(DropOffLocationDAO dropOffLocationDao) {
        this.dropOffLocationDao = dropOffLocationDao;
    }

    @GetMapping("")
    public ResponseEntity<DropOffLocation[]> getSchedule() {
        LOG.info("GET /schedule");
        try {
            DropOffLocation[] schedule = dropOffLocationDao.getSchedule();
            return new ResponseEntity<DropOffLocation[]>(schedule,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{town}")
    public ResponseEntity<DropOffLocation[]> getSchedule(@PathVariable String town) {
        LOG.info("GET /schedule/" + town);
        try {
            DropOffLocation[] schedule = dropOffLocationDao.getSchedule(town);
            return new ResponseEntity<DropOffLocation[]>(schedule,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<Boolean> addDropOffLocation(@RequestBody DropOffLocation dropOffLocation) {
        LOG.info("POST /schedule/" + dropOffLocation);
        try {
            Boolean sucess = dropOffLocationDao.addDropOffLocation(dropOffLocation);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
            return new ResponseEntity<Boolean>(sucess,HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Boolean> editDropOffLocation(@RequestBody DropOffLocation dropOffLocation) {
        LOG.info("PUT /schedule/" + dropOffLocation);
        try {
            Boolean sucess = dropOffLocationDao.editDropOffLocation(dropOffLocation);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
            return new ResponseEntity<Boolean>(sucess,HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDropOffLocation(@PathVariable int id) {
        LOG.info("DELETE /schedule/" + id);
        try {
            Boolean sucess = dropOffLocationDao.deleteDropOffLocation(id);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
            return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/{user}")
    public ResponseEntity<Boolean> volunteer(@PathVariable int id, @PathVariable String user) {
        LOG.info("POST /schedule/" + id + "/" + user);
        try {
            Boolean sucess = dropOffLocationDao.volunteer(id, user);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
            return new ResponseEntity<Boolean>(sucess,HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> unVolunteer(@PathVariable int id) {
        LOG.info("PUT /schedule/" + id);
        try {
            Boolean sucess = dropOffLocationDao.unVolunteer(id);
            if(sucess) {
                return new ResponseEntity<Boolean>(sucess,HttpStatus.OK);
            }
            return new ResponseEntity<Boolean>(sucess,HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}