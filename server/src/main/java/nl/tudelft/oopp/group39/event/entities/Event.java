package nl.tudelft.oopp.group39.event.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.config.abstracts.IEntity;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
import nl.tudelft.oopp.group39.room.entities.Room;

@Entity
@Table(name = Event.TABLE_NAME)
@JsonIgnoreProperties(allowSetters = true, value = {Event.COL_ROOMS})
public class Event extends AbstractEntity<Event, IEntity> {
    public static final String TABLE_NAME = "events";
    public static final String MAPPED_NAME = "event";
    public static final String COL_ROOMS = "rooms";

    @Enumerated(EnumType.STRING)
    private EventTypes type;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = (TABLE_NAME + "_" + Room.TABLE_NAME),
        joinColumns = {@JoinColumn(name = TABLE_NAME, referencedColumnName = COL_ID)},
        inverseJoinColumns = {
            @JoinColumn(name = Room.TABLE_NAME, referencedColumnName = Room.COL_ID)
        })
    private Set<Room> rooms = new HashSet<>();

    /**
     * Creates an event.
     */
    public Event() {
    }

    /**
     * Creates an event.
     *
     * @param type      the {@link EventTypes} type
     * @param startDate the start date yyyy-mm-dd
     * @param endDate   the end date yyyy-mm-dd, nullable
     * @param rooms     the rooms
     */
    public Event(
        EventTypes type,
        LocalDate startDate,
        LocalDate endDate,
        Set<Room> rooms
    ) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rooms.addAll(initSet(rooms));
    }

    /**
     * Gets the starting date of the event.
     *
     * @return the starting date of the event
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Changes the starting date of the event.
     *
     * @param startDate the new starting date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the event.
     *
     * @return the end date of the event
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Changes the end date of the event.
     *
     * @param endDate the new end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EventTypes getType() {
        return type;
    }

    public void setType(EventTypes type) {
        this.type = type;
    }

    /**
     * Gets the rooms for which the event holds.
     *
     * @return a set with all the rooms for which the event holds
     */
    public Set<Room> getRooms() {
        return rooms;
    }

    /**
     * Change the rooms for which the event holds.
     *
     * @param rooms the new set of rooms for which the event holds
     */
    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Checks if two events are equal.
     *
     * @param o the other object
     * @return  true if the two events are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(getId(), event.getId())
            && getType() == event.getType()
            && Objects.equals(getStartDate(), event.getStartDate())
            && Objects.equals(getEndDate(), event.getEndDate())
            && Objects.equals(getRooms(),event.getRooms());
    }

    /**
     * Changes the Event to an EventDto object.
     *
     * @return null
     */
    @Override
    public IEntity toDto() {
        return null;
    }
}
