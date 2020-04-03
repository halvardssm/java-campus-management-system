package nl.tudelft.oopp.group39.event.model;

import java.time.LocalDateTime;
import java.util.List;

public class Event {

    private Long id;
    private String title;
    private String startsAt;
    private String endsAt;
    private Boolean isGlobal;
    private String user;
    private List<Long> rooms;

    public Event() {

    }

    public Event(String title, String startsAt, String endsAt, Boolean isGlobal, String user, List<Long> rooms) {
        this.title = title;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.isGlobal = isGlobal;
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

    public Boolean getGlobal() {
        return isGlobal;
    }

    public String getUser() {
        return user;
    }

    public List<Long> getRooms() {
        return rooms;
    }

    public LocalDateTime getStartTime() {
        System.out.println(startsAt);
        return LocalDateTime.parse(startsAt.replace(" ", "T"));
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.parse(endsAt.replace(" ", "T"));
    }
}
