package nl.tudelft.oopp.group39.room.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.Objects;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class Room {
    private Long id;
    private Long building;
    private String name;
    private String description;
    private Boolean onlyStaff;
    private Integer capacity;
    private ArrayNode facilities;
    private ArrayNode bookings;

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
        ArrayNode bookings
    ) {
        this.id = id;
        this.building = buildingId;
        this.name = name;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
        this.facilities = facilities;
        this.bookings = bookings;
    }

    public Long getId() {
        return id;
    }

    public Long getBuilding() {
        return building;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Boolean isOnlyStaff() {
        return onlyStaff;
    }

    public String getDescription() {
        return description;
    }

    public ArrayNode getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayNode facilities) {
        this.facilities = facilities;
    }

    public ArrayNode getBookings() {
        return bookings;
    }

    public void setBookings(ArrayNode bookings) {
        this.bookings = bookings;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Room)) {
            return false;
        }

        Room room = (Room) o;

        return Objects.equals(id, room.id)
            && Objects.equals(building, room.building)
            && Objects.equals(name, room.name)
            && Objects.equals(onlyStaff, room.onlyStaff)
            && Objects.equals(description, room.description)
            && Objects.equals(facilities, room.facilities)
            && Objects.equals(bookings, room.bookings);
    }

    /**
     * Returns the building where the room is located.
     *
     * @return Building object
     * @throws JsonProcessingException when there is a processing exception
     */
    @JsonIgnore
    public Building getBuildingObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String buildingString = ServerCommunication.getBuilding(building);
        JsonNode body = mapper.readTree(buildingString).get("body");
        String buildingAsString = mapper.writeValueAsString(body);
        return mapper.readValue(buildingAsString, Building.class);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
