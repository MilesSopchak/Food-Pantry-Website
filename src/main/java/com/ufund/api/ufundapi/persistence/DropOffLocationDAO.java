package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.DropOffLocation;

public interface DropOffLocationDAO {
    DropOffLocation[] getSchedule() throws IOException;

    DropOffLocation[] getSchedule(String town) throws IOException;

    Boolean addDropOffLocation(DropOffLocation dropOffLocation) throws IOException;

    Boolean editDropOffLocation(DropOffLocation dropOffLocation) throws IOException;

    Boolean deleteDropOffLocation(int id) throws IOException;

    Boolean volunteer(int id, String user) throws IOException;

    Boolean unVolunteer(int id) throws IOException;
}
