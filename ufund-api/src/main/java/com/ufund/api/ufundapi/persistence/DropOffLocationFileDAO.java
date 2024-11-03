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

import com.ufund.api.ufundapi.model.DropOffLocation;

/**
 * Implements the functionality for JSON file-based peristance for Needs
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty
 */
@Component
public class DropOffLocationFileDAO implements DropOffLocationDAO {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DropOffLocationFileDAO.class.getName());
    Map<Integer,DropOffLocation> schedule;   // Provides a local cache of the cupboard
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Need
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new need
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Need File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public DropOffLocationFileDAO(@Value("${dropOffLocation.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the cupboard from the file
    }

    /**
     * Generates the next id for a new {@linkplain Need need}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates the cupboard array from the tree map
     * 
     * @return  The cupboard array, may be empty
     */
    private DropOffLocation[] getDropOffLocationArray() {
        return getDropOffLocationArray(null);
    }

    /**
     * Generates an array of {@linkplain Need needs} from the tree map for any
     * {@linkplain Need needs} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array is the entire cupboard in the tree map.
     * 
     * @return  The array of {@link Need needs}, may be empty
     */
    private DropOffLocation[] getDropOffLocationArray(String town) { // if containsText == null, no filter
        ArrayList<DropOffLocation> dropOffLocationArrayList = new ArrayList<>();

        for (DropOffLocation d : schedule.values()) {
            if (d.isValid()) {
                if ((town == null || d.getTown().toLowerCase().contains(
                    town.toLowerCase()))) {
                    dropOffLocationArrayList.add(d);
                }
            }
            else {
                schedule.remove(d.getId());
            }
        }

        DropOffLocation[] dropOffLocationArray = new DropOffLocation[dropOffLocationArrayList.size()];
        dropOffLocationArrayList.toArray(dropOffLocationArray);
        return dropOffLocationArray;
    }

    /**
     * Saves the {@linkplain Need needs} from the cupboard into the file as an array of JSON objects
     * 
     * @return true if the {@link Need needs} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        DropOffLocation[] needArray = getDropOffLocationArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),needArray);
        return true;
    }

    /**
     * Loads {@linkplain Need needs} from the JSON file into the cupboard
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        schedule = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        DropOffLocation[] dropOffLocationArray = objectMapper.readValue(new File(filename),DropOffLocation[].class);

        // Add each need to the tree map and keep track of the greatest id
        for (DropOffLocation d : dropOffLocationArray) {
            if (d.isValid()) {
                schedule.put(d.getId(),d);
                if (d.getId() > nextId)
                    nextId = d.getId();
            }
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    @Override
    public DropOffLocation[] getSchedule() throws IOException {
        synchronized(schedule) {
            return getDropOffLocationArray();
        }
    }

    @Override
    public DropOffLocation[] getSchedule(String town) throws IOException {
        synchronized(schedule) {
            return getDropOffLocationArray(town);
        }
    }

    @Override
    public Boolean addDropOffLocation(DropOffLocation dropOffLocation) throws IOException {
        synchronized(schedule) {
            if (!dropOffLocation.isValid() || dropOffLocation.getDate().length != 3) {
                return false;
            }
            for (DropOffLocation d : getDropOffLocationArray()) {
                if (d.equals(dropOffLocation)) {
                    return false;
                }
            }
            DropOffLocation newDropOffLocation = new DropOffLocation(nextId(),
            dropOffLocation.getTown(), dropOffLocation.getDate(), dropOffLocation.getHour(),
            dropOffLocation.getMinute(), null);
            schedule.put(newDropOffLocation.getId(), newDropOffLocation);
            return save();
        }
    }

    @Override
    public Boolean editDropOffLocation(DropOffLocation dropOffLocation) throws IOException {
        synchronized(schedule) {
            if (!(schedule.containsKey(dropOffLocation.getId()) && dropOffLocation.isValid()
            && dropOffLocation.getDate().length == 3)) {
                return false;
            }
            schedule.put(dropOffLocation.getId(), dropOffLocation);
            return save();
        }
    }

    @Override
    public Boolean deleteDropOffLocation(int id) throws IOException {
        synchronized(schedule) {
            if (schedule.containsKey(id)) {
                schedule.remove(id);
                return save();
            }
            save();
            return false;
        }
    }

    @Override
    public Boolean volunteer(int id, String user) throws IOException {
        synchronized(schedule) {
            DropOffLocation d = schedule.get(id);
            if (d != null && d.getUser() == null) {
                d.setUser(user);
                return save();
            }
            return false;
        }
    }

    @Override
    public Boolean unVolunteer(int id) throws IOException {
        synchronized(schedule) {
            DropOffLocation d = schedule.get(id);
            if (d != null && d.getUser() != null) {
                d.setUser(null);
                return save();
            }
            return false;
        }
    }
}
