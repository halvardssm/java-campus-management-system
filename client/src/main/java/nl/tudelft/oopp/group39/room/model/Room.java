package nl.tudelft.oopp.group39.room.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class Room {
    private long id;
    private int capacity;
    private String name;
    private boolean onlyStaff;
    private String description;
    private ArrayNode facilities;
    private ArrayNode events;
    private ArrayNode bookings;
    private long building;

    public Room() {

    }

    /**
     * Creates a room.
     *
     * @param capacity    capacity of the room
     * @param name        name of the room
     * @param onlyStaff   whether the room is only accessible to staff
     * @param buildingId  the id of the building
     * @param description description of the room
     * @param facilities  ArrayNode of facilities the room has
     * @param events      ArrayNode of events for the room
     * @param bookings    ArrayNode of bookings for the room
     */
    public Room(
        long id,
        int capacity,
        String name,
        boolean onlyStaff,
        String description,
        long buildingId,
        ArrayNode facilities,
        ArrayNode events,
        ArrayNode bookings
    ) {
        this.id = id;
        this.building = buildingId;
        this.name = name;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities = facilities;
        this.events = events;
        this.bookings = bookings;
    }

    public long getId() {
        return id;
    }

    public long getBuilding() {
        return building;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOnlyStaff() {
        return onlyStaff;
    }

    public String getDescription() {
        return description;
    }

    public ArrayNode getFacilities() {
        return facilities;
    }

    public ArrayNode getEvents() {
        return events;
    }

    public ArrayNode getBookings() {
        return bookings;
    }

    /**
     * Returns a string representation of the facilities.
     *
     * @return String of facilities
     */
    public String facilitiesToString() {
        if (facilities.size() == 0 || facilities == null) {
            return "none";
        } else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < facilities.size(); i++) {
                if (facilities.get(i).toString().contains("description")) {
                    if (i == facilities.size() - 1) {
                        result.append(facilities.get(i).get("description").asText());
                    } else {
                        result.append(facilities.get(i).get("description").asText()).append(", ");
                    }
                }
            }
            return result.toString();
        }
    }

    /**
     * Returns the building where the room is located.
     *
     * @return Building object
     * @throws JsonProcessingException when there is a processing exception
     */
    public Building getBuildingObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String buildingJson = ServerCommunication.getBuilding(building);
        JsonNode buildingNode = mapper.readTree(buildingJson).get("body");
        String buildingAsString = mapper.writeValueAsString(buildingNode);
        return mapper.readValue(buildingAsString, Building.class);
    }

}
