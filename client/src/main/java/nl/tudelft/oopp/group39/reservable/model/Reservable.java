package nl.tudelft.oopp.group39.reservable.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class Reservable {
    private Long id;
    private Double price;
    private Long building;

    /**
     * Creates a reservable.
     */
    public Reservable() {
    }

    /**
     * Creates a reservable.
     *
     * @param id       of the reservable
     * @param price    of the reservable
     * @param building where the reservable is available
     */
    public Reservable(Long id, double price, Long building) {
        this.id = id;
        this.price = price;
        this.building = building;
    }

    /**
     * Gets the id of the reservable.
     *
     * @return the id of the reservable
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the price of the reservable.
     *
     * @return the price of the reservable
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Gets the building id.
     *
     * @return the building id
     */
    public Long getBuilding() {
        return building;
    }

    /**
     * Retrieves the building as object.
     *
     * @return building object
     * @throws JsonProcessingException when there is a processing exception
     */
    public Building getBuildingObj() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String buildingString = ServerCommunication.getBuilding(this.building);
        JsonNode body = mapper.readTree(buildingString).get("body");
        String buildingAsString = mapper.writeValueAsString(body);
        return mapper.readValue(buildingAsString, Building.class);
    }
}
