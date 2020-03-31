package nl.tudelft.oopp.group39.facility.model;

public class Facility {
    private Long id;
    private String description;

    public Facility() {

    }

    public Facility(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
