package nl.tudelft.oopp.demo.objects.building;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "open")
    private LocalTime open;

    @Column(name = "closed")
    private LocalTime closed;

    //opening times (open & closed)

    public Building() {
    }

    public Building(long id, String name, String location, String description, LocalTime open, LocalTime closed) {
        this.id = id;
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
    public LocalTime getOpen() {
        return open;
    }
    public LocalTime getClosed() {
        return closed;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(String location) {
         this.location = location;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setOpen(LocalTime open) {
        this.open = open;
    }
    public void setClosed(LocalTime closed) {
        this.closed = closed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Building building = (Building) o;

        return id == building.id;
    }
}
