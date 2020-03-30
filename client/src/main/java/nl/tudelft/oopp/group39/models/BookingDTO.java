package nl.tudelft.oopp.group39.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDTO {

    private Integer id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String user;
    private Integer room;

    public BookingDTO() {
    }

    /**
     * Creates a booking.
     *
     * @param date      date of booking
     * @param startTime start time of booking
     * @param endTime   end time of booking
     * @param user      user that made the booking
     * @param room      room that is booked
     */
    public BookingDTO(
            Integer id,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String user,
            Integer room) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.room = room;
    }

    public Integer getId() { return id; }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getUser() { return user; }

    public Integer getRoom() { return room; }
}
