package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

@JsonIgnoreProperties(value = { "reservations" })
public class Room {
    private long id;
    private int capacity;
    private String name;
    private boolean onlyStaff;
    private String description;
    private ArrayNode facilities;
    private ArrayNode events;
    private ArrayNode bookings;
    private long buildingId;

    @JsonProperty("building")
    public void setBuildingId(Building building){
        this.buildingId = building.getId();
    }

    public Room() {

    }

    /**
     * Creates a room.
     *
     * @param capacity    capacity of the room
     * @param name        name of the room
     * @param onlyStaff   whether the room is only accessible to staff
     * @param building  the id of the building
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
        ArrayNode building,
        ArrayNode facilities,
        ArrayNode events,
        ArrayNode bookings
    ) {
        System.out.println("Building: " + building);
        this.id = id;
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
        return buildingId;
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
        String buildingJson = ServerCommunication.getBuilding(this.buildingId);
        JsonNode buildingNode = mapper.readTree(buildingJson).get("body");
        String buildingAsString = mapper.writeValueAsString(buildingNode);
        return mapper.readValue(buildingAsString, Building.class);
    }

}
