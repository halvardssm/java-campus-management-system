package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

@JsonIgnoreProperties(value = { "reservables", "rooms" })
public class Building {
    private Integer id;
    private String name;
    private String location;
    private String description;
    private String open;
    private String closed;

    /**
     * Creates a building.
     */
    public Building() {
    }

    /**
     * Creates a building.
     *
     * @param id          the id of the building
     * @param name        the name of the building
     * @param location    the location of the building
     * @param description the description of the building
     * @param open        the opening time of the building
     * @param closed      the closing time of the building
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

    /**
     * Gets the id of the building.
     *
     * @return the id of the building
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the name of the building.
     *
     * @return the name of the building
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the address of the building.
     *
     * @return the location of the building
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the description of the building.
     *
     * @return the description of the building
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the opening time of the building.
     *
     * @return the opening time of the building
     */
    public String getOpen() {
        return open;
    }

    /**
     * Gets the closing time of the building.
     *
     * @return the closing time of the building
     */
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
