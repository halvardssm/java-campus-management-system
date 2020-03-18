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

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }
}
