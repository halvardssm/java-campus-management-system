package nl.tudelft.oopp.group39.models;

public class Facility {
    private long id;
    private String description;

    public Facility() {

    }

    public Facility(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
