package nl.tudelft.oopp.group39.event.dto;

import static nl.tudelft.oopp.group39.config.Utils.initList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.event.entities.Event;
import nl.tudelft.oopp.group39.room.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class EventDto extends AbstractDto<Event, EventDto> {

    private String title;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Boolean isGlobal;
    private String user;
    private List<Long> rooms = new ArrayList<>();

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
        setGlobal(isGlobal != null ? isGlobal : false);
        setUser(user);
        getRooms().addAll(initList(rooms));
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Long> getRooms() {
        return rooms;
    }

    public void setRooms(List<Long> rooms) {
        this.rooms = rooms;
    }

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
            && Objects.equals(getGlobal(), event.getGlobal())
            && Objects.equals(getUser(), event.getUser())
            && Objects.equals(getRooms(), event.getRooms());
    }

    @Override
    public Event toEntity() {
        return new Event(
            getId(),
            getTitle(),
            getStartsAt(),
            getEndsAt(),
            getGlobal(),
            null,
            Utils.idsToComponentSet(getRooms(), Room.class)
        );
    }
}
