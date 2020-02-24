package nl.tudelft.oopp.demo.objects.facility.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "facilities")
public class Facility {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "description")
    private String description;

    public Facility() {}

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

    public void setDescription(String description) {
        this.description = description;
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

        return id == facility.id;
    }
}
