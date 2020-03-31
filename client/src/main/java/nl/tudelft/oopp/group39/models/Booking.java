package nl.tudelft.oopp.group39.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

public class Booking {

    private Integer id;
    private String date;
    private String startTime;
    private String endTime;
    private String user;
    private long room;

    public Booking() {

    }

    /**
     * Creates a booking.
     *
     * @param id        booking id
     * @param date      date of booking
     * @param startTime start time of booking
     * @param endTime   end time of booking
     * @param user start time of booking
     * @param room   end time of booking
     */
    public Booking(
        Integer id,
        String date,
        String startTime,
        String endTime,
        String user,
        int room
    ) {
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

    public int getRoom() {
        return (int) room;
    }
}
