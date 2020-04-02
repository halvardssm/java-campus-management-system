package nl.tudelft.oopp.group39.booking.model;

import java.util.Objects;

public class Booking {

    private Integer id;
    private String date;
    private String startTime;
    private String endTime;
    private String user;
    private Integer room;

    public Booking() {
    }

    /**
     * Creates a booking.
     *
     * @param id        booking id
     * @param date      date of booking
     * @param startTime start time of booking
     * @param endTime   end time of booking
     * @param user      user that made the booking
     * @param room      room that is booked
     */
    public Booking(
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id)
            && Objects.equals(date, booking.date)
            && Objects.equals(startTime, booking.startTime)
            && Objects.equals(endTime, booking.endTime)
            && Objects.equals(user, booking.user)
            && Objects.equals(room, booking.room);
    }
}
