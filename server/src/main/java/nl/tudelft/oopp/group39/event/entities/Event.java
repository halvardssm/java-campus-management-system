package nl.tudelft.oopp.group39.event.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.event.dto.EventDto;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;

@Entity
@Table(name = Event.TABLE_NAME)
@JsonIgnoreProperties(allowSetters = true, value = {Event.COL_ROOMS})
public class Event extends AbstractEntity<Event, EventDto> {
    public static final String TABLE_NAME = "events";
    public static final String MAPPED_NAME = "event";
    public static final String COL_TITLE = "title";
    public static final String COL_STARTS_AT = "startsAt";
    public static final String COL_ENDS_AT = "endsAt";
    public static final String COL_IS_GLOBAL = "isGlobal";
    public static final String COL_USER = "user";
    public static final String COL_ROOMS = "rooms";

    private String title;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    @JsonProperty(value = COL_IS_GLOBAL)
    private Boolean isGlobal;
    @ManyToOne
    @JoinColumn(name = User.MAPPED_NAME)
    private User user;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = (TABLE_NAME + "_" + Room.TABLE_NAME),
        joinColumns = {@JoinColumn(name = TABLE_NAME, referencedColumnName = COL_ID)},
        inverseJoinColumns = {
            @JoinColumn(name = Room.TABLE_NAME, referencedColumnName = Room.COL_ID)
        })
    private Set<Room> rooms = new HashSet<>();

    public Event() {
    }

    /**
     * Creates an event.
     *
     * @param id       the id
     * @param title    the title of the event
     * @param startsAt the start date yyyy-mm-dd
     * @param endsAt   the end date yyyy-mm-dd, nullable
     * @param isGlobal if the event is global, or user specific
     * @param user     the user owning the event, is null if it is global
     * @param rooms    the rooms
     */
    public Event(
        Long id,
        String title,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Boolean isGlobal,
        User user,
        Set<Room> rooms
    ) {
        setId(id);
        setTitle(title);
        setStartsAt(startsAt);
        setEndsAt(endsAt);
        setGlobal(isGlobal != null ? isGlobal : false);
        setUser(user);
        getRooms().addAll(initSet(rooms));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startDate) {
        this.startsAt = startDate;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endDate) {
        this.endsAt = endDate;
    }

    public Boolean getGlobal() {
        return isGlobal;
    }

    public void setGlobal(Boolean global) {
        isGlobal = global;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
            && Objects.equals(getTitle(), event.getTitle())
            && Objects.equals(getStartsAt(), event.getStartsAt())
            && Objects.equals(getEndsAt(), event.getEndsAt())
            && Objects.equals(getGlobal(), event.getGlobal())
            && Objects.equals(getUser(), event.getUser())
            && Objects.equals(getRooms(), event.getRooms());
    }

    @Override
    public EventDto toDto() {
        return new EventDto(
            getId(),
            getTitle(),
            getStartsAt(),
            getEndsAt(),
            getGlobal(),
            Utils.safeNull((p) -> getUser().getUsername()),
            Utils.entitiesToIds(getRooms())
        );
    }
}
