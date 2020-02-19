package nl.tudelft.oopp.demo.objects.room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "buildingId")
    private int buildingId;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "onlyStaff")
    private boolean onlyStaff;

    @Column(name = "description")
    private String description;

    public Room() {}

    public Room(int id, int buildingId, int capacity, boolean onlyStaff, String description) {
        this.id = id;
        this.buildingId = buildingId;
        this.capacity = capacity;
        this.onlyStaff = onlyStaff;
        this.description = description;
    }

    public int getId() {
        return id;
    }
    public int getBuilding() {
        return buildingId;
    }
    public int getCapacity() {
        return capacity;
    }
    public boolean getOnlyStaff() {
        return onlyStaff;
    }
    public String getDescription() {
        return description;
    }

    public void setBuilding(int buildingId) {
        this.buildingId = buildingId;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setOnlyStaff(boolean onlyStaff) {
        this.onlyStaff = onlyStaff;
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

        Room room = (Room) o;

        return id == room.id;
    }
}
