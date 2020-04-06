package nl.tudelft.oopp.group39.facility.model;

import java.util.Objects;

public class Facility {
    private Long id;
    private String description;

    /**
     * Creates a facility.
     */
    public Facility() {
    }

    /**
     * Creates a facility.
     *
     * @param id the id of the facility
     * @param description the description of the facility
     */
    public Facility(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Gets the id of the facility.
     *
     * @return the facility id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the description of the facility.
     *
     * @return the description of the facility
      */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether two facilities are equal.
     *
     * @param o the other object
     * @return  true if the two buildings are equal, false otherwise
     */
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
