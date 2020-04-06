package nl.tudelft.oopp.group39.event.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class Event {
    private Long id;
    private String title;
    private String startsAt;
    private String endsAt;
    private Boolean global;
    private String user;
    @JsonDeserialize
    private List<Long> rooms;

    /**
     * Sets the field to true if this event is for every user.
     *
     * @param isGlobal true if you want the event to hold for every user, false otherwise
     * @throws IOException if there is a problem with connecting to the database
     */
    @JsonProperty("isGlobal")
    public void setGlobal(Boolean isGlobal) throws IOException {
        this.global = isGlobal;
    }

    /**
     * Creates an event.
     */
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

    /**
     * Gets the id of the event.
     *
     * @return the id of the event
     */
    public Long getId() {
        return id;
    }

    /**
     * Changes the id of the event.
     *
     * @param id the new id of the event
     */
    public void setId(Long id) {
        this.id = id;
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
     * Gets the starting time of the event.
     *
     * @return the starting time of the event
     */
    public String getStartsAt() {
        return startsAt;
    }

    /**
     * Gets the ending time of the event.
     *
     * @return the ending time of the event
     */
    public String getEndsAt() {
        return endsAt;
    }

    /**
     * Checks whether this event is global.
     *
     * @return true if the event is global, false otherwise
     */
    public Boolean isGlobal() {
        return global;
    }

    /**
     * Gets the netid of the user who created the event.
     *
     * @return the netid of the user who created the event
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the rooms where the event is affected by.
     *
     * @return the rooms where the event is affected by
     */
    public List<Long> getRooms() {
        return rooms;
    }

    /**
     * Gets the starting time of the event.
     *
     * @return the starting time of the event
     */
    @JsonIgnore
    public LocalDateTime getStartTime() {
        return LocalDateTime.parse(startsAt.replace(" ", "T"));
    }

    /**
     * Gets the ending time of the event.
     *
     * @return the ending time of the event
     */
    @JsonIgnore
    public LocalDateTime getEndTime() {
        return LocalDateTime.parse(endsAt.replace(" ", "T"));
    }

    /**
     * Checks if the event is a full day.
     *
     * @return boolean true if the event is a full day, false otherwise
     */
    @JsonIgnore
    public boolean isFullDay() {
        return this.getStartTime().toLocalTime().equals(LocalTime.of(0, 0, 0))
            && this.getEndTime().toLocalTime().equals(LocalTime.of(23, 59, 59));
    }

    /**
     * Checks if two bookings are equal.
     *
     * @param o the other object
     * @return  true if the two bookings are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return getTitle().equals(event.getTitle())
            && getStartsAt().equals(event.getStartsAt())
            && Objects.equals(getEndsAt(), event.getEndsAt())
            && global.equals(event.global)
            && getUser().equals(event.getUser())
            && Objects.equals(getRooms(), event.getRooms());
    }
}
