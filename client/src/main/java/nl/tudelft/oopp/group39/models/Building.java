package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class Building {

    private Integer id;

    private String name;

    private String location;

    private String description;

    private String open;

    private String closed;

    public Building() {

    }

    /**
     * Doc. TODO Sven
     *
     * @param name        name
     * @param location    location
     * @param description description
     * @param open        open
     * @param closed      closed
     */
    public Building(
        Integer id,
        String name,
        String location,
        String description,
        String open,
        String closed
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.open = open;
        this.closed = closed;
    }

    public Integer getId() { return id; }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getOpen() {
        return open;
    }

    public String getClosed() {
        return closed;
    }

    /**
     * Finds the max capacity of the building.
     *
     * @return int of max capacity of building
     * @throws JsonProcessingException when there is a processing exception
     */
    public int getMaxCapacity() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String roomString = ServerCommunication.getRooms(id);
        ArrayNode body = (ArrayNode) mapper.readTree(roomString).get("body");
        if (body.isEmpty()) {
            return 0;
        } else {
            roomString = mapper.writeValueAsString(body);
            Room[] rooms = mapper.readValue(roomString, Room[].class);
            int maxCapacity = rooms[0].getCapacity();
            for (Room room : rooms) {
                if (room.getCapacity() > maxCapacity) {
                    maxCapacity = room.getCapacity();
                }
            }
            return maxCapacity;
        }

    }
}
