package nl.tudelft.oopp.group39.room.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class Room {
    private Long id;
    private Integer capacity;
    private String name;
    private Boolean onlyStaff;
    private String description;
    private ArrayNode facilities;
    private ArrayNode events;
    private ArrayNode bookings;
    private Long building;

    /**
     * Creates a room.
     */
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
        Long id,
        Integer capacity,
        String name,
        Boolean onlyStaff,
        String description,
        Long buildingId,
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

    /**
     * Gets the id of the room.
     *
     * @return the room id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the building where the room is in.
     *
     * @return the building where the room is in
     */
    public Long getBuilding() {
        return building;
    }

    /**
     * Gets the name of the of the room.
     *
     * @return the name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the capacity of the room.
     *
     * @return the room capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Checks whether the room is only for staff, or also for students.
     *
     * @return true if the room is only for staff, false otherwise
     */
    public Boolean isOnlyStaff() {
        return onlyStaff;
    }

    /**
     * Gets the description of the room.
     *
     * @return the description of the room
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the facilities that the room has to offer.
     *
     * @return an ArrayNode with all the facilities
     */
    public ArrayNode getFacilities() {
        return facilities;
    }

    /**
     * Gets the events of the room.
     *
     * @return an ArrayNode with all the events
     */
    public ArrayNode getEvents() {
        return events;
    }

    /**
     * Gets the bookings of the room.
     *
     * @return an ArrayNode of all the bookings
     */
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
        String buildingString = ServerCommunication.getBuilding(building);
        JsonNode body = mapper.readTree(buildingString).get("body");
        String buildingAsString = mapper.writeValueAsString(body);
        return mapper.readValue(buildingAsString, Building.class);
    }
}
