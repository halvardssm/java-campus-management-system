package nl.tudelft.oopp.group39.event.dto;

import static nl.tudelft.oopp.group39.config.Utils.initList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.event.entities.Event;
import org.springframework.stereotype.Component;

@Component
public class EventDto extends AbstractDto<Event, EventDto> {
    private String title;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Boolean isGlobal;
    private String user;
    private List<Long> rooms = new ArrayList<>();

    /**
     * Creates an event.
     */
    public EventDto() {
    }

    /**
     * Creates an event.
     *
     * @param title    the title of the event
     * @param startsAt the start date yyyy-mm-dd
     * @param endsAt   the end date yyyy-mm-dd, nullable
     * @param isGlobal if the event is global, or user specific
     * @param user     the user owning the event, is null if it is global
     * @param rooms    the rooms
     */
    public EventDto(
        Long id,
        String title,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Boolean isGlobal,
        String user,
        List<Long> rooms
    ) {
        setId(id);
        setTitle(title);
        setStartsAt(startsAt);
        setEndsAt(endsAt);
        setIsGlobal(isGlobal != null ? isGlobal : false);
        setUser(user);
        getRooms().addAll(initList(rooms));
    }

    /**
     * Gets the title of the event.
     *
     * @return the title of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes the title of the event.
     *
     * @param title the new title of the event
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the starting time of the event.
     *
     * @return the starting time of the event
     */
    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    /**
     * Changes the starting time of the event.
     *
     * @param startDate the new starting time
     */
    public void setStartsAt(LocalDateTime startDate) {
        this.startsAt = startDate;
    }

    /**
     * Gets the ending time of the event.
     *
     * @return the ending time of the event
     */
    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    /**
     * Changes the ending time of the event.
     *
     * @param endDate the new ending time of the event
     */
    public void setEndsAt(LocalDateTime endDate) {
        this.endsAt = endDate;
    }

    /**
     * Checks whether the event is global.
     *
     * @return true if the event is global, false otherwise
     */
    public Boolean getIsGlobal() {
        return isGlobal;
    }

    /**
     * Changes the state of the event whether it is global or not.
     *
     * @param isGlobal true if you want the event to be global, false otherwise
     */
    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    /**
     * Gets the user who owns the event.
     *
     * @return the user who owns the event
     */
    public String getUser() {
        return user;
    }

    /**
     * Changes the user who owns the event.
     *
     * @param user the new user who you want to own the event
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the rooms that are affected by the event.
     *
     * @return the rooms that are affected by the event
     */
    public List<Long> getRooms() {
        return rooms;
    }

    /**
     * Changes the rooms that are affected by the event.
     *
     * @param rooms the new rooms that you want to be affected by the event
     */
    public void setRooms(List<Long> rooms) {
        this.rooms = rooms;
    }

    /**
     * Checks whether two EventDto's are equal.
     *
     * @param o the other object
     * @return  true if the two EventDto's are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDto)) {
            return false;
        }
        EventDto event = (EventDto) o;
        return Objects.equals(getId(), event.getId())
            && Objects.equals(getTitle(), event.getTitle())
            && Objects.equals(getStartsAt(), event.getStartsAt())
            && Objects.equals(getEndsAt(), event.getEndsAt())
            && Objects.equals(getIsGlobal(), event.getIsGlobal())
            && Objects.equals(getUser(), event.getUser())
            && Objects.equals(getRooms(), event.getRooms());
    }

    /**
     * Changes the Dto object to a normal entity.
     *
     * @return the entity version of the eventDto
     */
    @Override
    public Event toEntity() {
        return new Event(
            getId(),
            getTitle(),
            getStartsAt(),
            getEndsAt(),
            getIsGlobal(),
            null,
            null
        );
    }
}
