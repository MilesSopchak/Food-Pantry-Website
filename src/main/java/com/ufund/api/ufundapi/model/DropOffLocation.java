package com.ufund.api.ufundapi.model;

import java.util.Arrays;
import java.util.logging.Logger;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single time slot for a drop-off location.
 * 
 * @author Miles Sopchak
 */
public class DropOffLocation {
    @SuppressWarnings("unused")
    private static final Logger LOG =
    Logger.getLogger(DropOffLocation.class.getName());

    // Package private for tests
    static final String STRING_FORMAT =
    "DropOffLocation [id=%d, town=%s, date=%s, hour=%d, minute=%d, user=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("town") private String town;
    @JsonProperty("date") private int[] date;
    @JsonProperty("hour") private int hour;
    @JsonProperty("minute") private int minute;
    @JsonProperty("user") private String user;

    /**
     * Creates a {@linkplain DropOffLocation drop-off location} with the
     * provided information.
     * @param id The id of the {@linkplain DropOffLocation drop-off location}.
     * @param town The town the {@linkplain DropOffLocation drop-off location} is in.
     * @param date The date of the day that the {@linkplain DropOffLocation drop-off
     * location} is on. [YYYY,MM,DD].
     * @param hour The hour that the {@linkplain DropOffLocation drop-off location}
     * starts on. Starting with 12am (hour = 0) until 11 pm (hour = 23).
     * @param minute The minute that the
     * {@linkplain DropOffLocation drop-off location} starts on. Starting on the hour
     * (minute = 0) until the minute before the next (minute = 59).
     * @param user The username of the {@linkplain User user} that has volunteered to
     * distribute the donated food at this {@linkplain DropOffLocation drop-off location}.
     * 
     * {@literal @JsonProperty} Used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int.
     */
    public DropOffLocation(@JsonProperty("id") int id,
    @JsonProperty("town") String town, @JsonProperty("date") int[] date,
    @JsonProperty("hour") int hour, @JsonProperty("minute") int minute,
    @JsonProperty("user") String user) {
        this.id = id;
        this.town = town;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.user = user;
    }

    /**
     * Assesses the validity of the date and time of the
     * {@linkplain DropOffLocation drop-off location}. It checks if the date and time
     * provided are real as well as if the
     * {@linkplain DropOffLocation drop-off location} has already happened.
     * @return True if the {@linkplain DropOffLocation drop-off location's} date and
     * time are real and have yet to pass. False otherwise.
     */
    public Boolean isValid() {
        return setSchedule(this.date, this.hour, this.minute);
    }

    //Getters
    /**
     * Gets the id of the {@linkplain DropOffLocation drop-off location}.
     * @return The id of the {@linkplain DropOffLocation drop-off location}.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the town that the {@linkplain DropOffLocation drop-off location} is in.
     * @return The town that the {@linkplain DropOffLocation drop-off location} is in.
     */
    public String getTown() {
        return town;
    }

    /**
     * Gets the date of the {@linkplain DropOffLocation drop-off location}.
     * @return The date of the {@linkplain DropOffLocation drop-off location}.
     */
    public int[] getDate() {
        return date;
    }

    /**
     * Gets the hour of the {@linkplain DropOffLocation drop-off location}.
     * @return The hour of the {@linkplain DropOffLocation drop-off location}.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets the minute of the {@linkplain DropOffLocation drop-off location}.
     * @return The minute of the {@linkplain DropOffLocation drop-off location}.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Gets the user signed up for the
     * {@linkplain DropOffLocation drop-off location}.
     * @return The user signed up for the
     * {@linkplain DropOffLocation drop-off location}.
     */
    public String getUser() {
        return user;
    }

    //Setters
    /**
     * Sets the id of the {@linkplain DropOffLocation drop-off location} to the
     * provided id.
     * @param id The new id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the town of the {@linkplain DropOffLocation drop-off location} to the
     * provided town.
     * @param town The new town.
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Sets the schedule (date and time) of the {@linkplain DropOffLocation drop-off
     * location} to the provided id.
     * @param date The new date.
     * @param hour The new hour.
     * @param minute The new minute.
     * @return True if the schedule provided is real and if it has yet to pass.
     * False otherwise.
     */
    public Boolean setSchedule(int[] date, int hour, int minute) {
        try {
            if(LocalDate.now().isAfter(LocalDate.of(date[0], date[1], date[2]))) {
                return false;
            }
            if(LocalDate.now().equals(LocalDate.of(date[0], date[1], date[2])) && LocalTime.now().isAfter(LocalTime.of(hour, minute))) {
                return false;
            }
        }
        catch (DateTimeException e) {
            return false;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        return true;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject.getClass() != this.getClass()) {
            return false;
        }
        DropOffLocation d = (DropOffLocation)anotherObject;
        if (town.equals(d.getTown()) && Arrays.equals(date, d.getDate()) &&
        hour == d.getHour() && minute == d.getMinute()) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,town,Arrays.toString(date),hour,minute,user);
    }
}

