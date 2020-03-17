package nl.tudelft.oopp.group39.entities;

import java.time.LocalTime;

public class Building {

    private long id;

    private String name;

    private String location;

    private String description;

    private String open;

    private String closed;


    public Building() {
    }

    public Building(String name, String location, String description, String open, String closed) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.open = open;
        this.closed = closed;
    }

    public long getId() {
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
