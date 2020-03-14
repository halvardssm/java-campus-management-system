package nl.tudelft.oopp.group39.booking.Entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import nl.tudelft.oopp.group39.room.entities.Room;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = Booking.TABLE_NAME)

public class Booking {
    public static final String TABLE_NAME = "bookings";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer roomId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer userId;
    private Integer buildingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public Booking() {
    }

    public Booking(Integer roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Integer userId, Integer buildingId, Room room) {
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.buildingId = buildingId;
        this.room = room;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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
            getBuildingId() == booking.getBuildingId() &&
            getRoom() == booking.getRoom();
    }
}

