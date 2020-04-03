package nl.tudelft.oopp.group39.event.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Event {

    private Long id;
    private String title;
    private String startsAt;
    private String endsAt;
    private Boolean global;
    private String user;
    private List<Long> rooms;

    public Event() {

    }

    /**
     * Creates an event.
     *
     * @param title    name of event
     * @param startsAt start date and time of event
     * @param endsAt   end date and time of event
     * @param global   whether event is global
     * @param user     netid of user that created the event
     * @param rooms    rooms the event is applicable to
     */
    public Event(
        String title,
        String startsAt,
        String endsAt,
        Boolean global,
        String user,
        List<Long> rooms
    ) {
        this.title = title;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.global = global;
        this.user = user;
        this.rooms = rooms;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public Boolean isGlobal() {
        return global;
    }

    public String getUser() {
        return user;
    }

    public List<Long> getRooms() {
        return rooms;
    }

    public LocalDateTime getStartTime() {
        return LocalDateTime.parse(startsAt.replace(" ", "T"));
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.parse(endsAt.replace(" ", "T"));
    }

    /**
     * Checks if the event is a full day.
     *
     * @return boolean true if the event is a full day, false otherwise
     */
    public boolean isFullDay() {
        return this.getStartTime().toLocalTime().equals(LocalTime.of(0, 0, 0))
            && this.getEndTime().toLocalTime().equals(LocalTime.of(23, 59, 59));
    }
}
