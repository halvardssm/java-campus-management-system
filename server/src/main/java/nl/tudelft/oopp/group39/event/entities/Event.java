package nl.tudelft.oopp.group39.event.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.event.enums.EventTypes;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.lang.Nullable;

@Entity
@Table(name = Event.TABLE_NAME)
public class Event {
    public static final String TABLE_NAME = "events";
    public static final String MAPPED_NAME = "event";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private EventTypes type;
    @JsonFormat(pattern = Constants.FORMAT_DATE)
    private LocalDate startDate;
    @JsonFormat(pattern = Constants.FORMAT_DATE)
    private LocalDate endDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = TABLE_NAME + "_" + Room.TABLE_NAME,
        joinColumns = {
            @JoinColumn(name = MAPPED_NAME, referencedColumnName = "id",
                nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = Room.MAPPED_NAME, referencedColumnName = "id",
                nullable = false, updatable = false)})
    private Set<Room> rooms = new HashSet<>();

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
    public Event(EventTypes type, LocalDate startDate, @Nullable LocalDate endDate, Set<Room> rooms) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rooms.addAll(rooms != null ? rooms : new HashSet<>());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EventTypes getType() {
        return type;
    }

    public void setType(EventTypes type) {
        this.type = type;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

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
            && getRooms().equals(event.getRooms());
    }
}
