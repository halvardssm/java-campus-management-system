package nl.tudelft.oopp.group39.models;

public class Booking {

    private Integer id;
    private String date;
    private String startTime;
    private String endTime;
    private User user;
    private Room room;

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
        User user,
        Room room) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.room = room;
    }

    public Integer getId() { return id; }

    public String getDate() {
        return date;
    }

    public String getStartTime() { return startTime; }

    public String getEndTime() { return endTime; }

    public User getUser() { return user; }

    public Room getRoom() { return room; }
}
