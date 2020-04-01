package nl.tudelft.oopp.group39.reservable.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDto {

    private Integer id;
    private String date;
    private String startTime;
    private String endTime;
    private String user;
    private Integer room;

    public BookingDto() {
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
    public BookingDto(
            Integer id,
            String date,
            String startTime,
            String endTime,
            String user,
            Integer room) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.room = room;
    }

    public Integer getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getUser() {
        return user;
    }

    public Integer getRoom() {
        return room;
    }
}
