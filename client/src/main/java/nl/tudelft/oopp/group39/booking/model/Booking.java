package nl.tudelft.oopp.group39.booking.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

import java.util.Objects;
import javax.print.DocFlavor;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.user.model.User;

public class Booking {
    private Integer id;
    private String date;
    private String startTime;
    private String endTime;
    private String user;
    private Long room;

    /**
     * Creates a booking.
     */
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
        Long room) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.room = room;
    }

    /**
     * Gets the id of the booking.
     *
     * @return the booking id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the date of the booking.
     *
     * @return the booking date
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the starting time of the booking.
     *
     * @return the starting time of the booking.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the booking.
     *
     * @return the end time of the booking
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Gets the user who made the booking.
     *
     * @return the user who made the booking
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the id of the room that is booked.
     *
     * @return the id of the room
     */
    public Long getRoom() {
        return room;
    }

    /**
     * Gets the name of the room that is booked.
     *
     * @return the name of the room
     */
    public String getRoomName() throws JsonProcessingException {
        Room roomObject = ServerCommunication.getRoom(room);
        return roomObject.getName();
    }

    /**
     * Gets the location of the room that is booked.
     *
     * @return the location of the room
     */
    public String getLocation() throws JsonProcessingException {
        Room roomObject = ServerCommunication.getRoom(room);
        return roomObject.getBuildingObject().getLocation();
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
