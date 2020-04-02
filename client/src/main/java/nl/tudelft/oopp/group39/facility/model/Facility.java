package nl.tudelft.oopp.group39.facility.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facility facility = (Facility) o;
        return Objects.equals(id, facility.id)
            && Objects.equals(description, facility.description);
    }
}
