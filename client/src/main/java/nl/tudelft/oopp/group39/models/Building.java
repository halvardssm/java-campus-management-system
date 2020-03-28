package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "reservables" })
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

    public Integer getId() {
        return id;
    }

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
}
