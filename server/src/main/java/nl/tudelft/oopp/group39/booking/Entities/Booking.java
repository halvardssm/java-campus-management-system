package nl.tudelft.oopp.group39.booking.Entities;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long roomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private long userId;
    private String location;

    public Booking() {
    }

    public Booking(long roomId, LocalDate date, LocalTime startTime, LocalTime endTime, long userId, String location) {
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.location = location;

    }

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        return getId() == booking.getId() &&
            getRoomId() == booking.getRoomId() &&
            getUserId() == booking.getUserId() &&
            getDate().equals(booking.getDate()) &&
            getStartTime().compareTo(booking.getStartTime()) == 0 &&
            getEndTime().compareTo(booking.getEndTime()) == 0 && //difference of time = 0
            getLocation().equals(booking.getLocation());
    }
}

