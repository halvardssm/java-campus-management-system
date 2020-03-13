package nl.tudelft.oopp.group39.booking.Entities;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = Booking.TABLE_NAME)

public class Booking {
    public static final String TABLE_NAME = "bookings";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private long id;
    private long roomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private long userId;
    private long buildingId;

    public Booking() {
    }

    public Booking(long roomId, LocalDate date, LocalTime startTime, LocalTime endTime, long userId, long buildingId) {
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.buildingId = buildingId;

    }

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

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
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
            getBuildingId() == booking.getBuildingId();
    }
}

