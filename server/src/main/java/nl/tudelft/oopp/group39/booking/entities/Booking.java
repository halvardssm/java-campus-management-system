package nl.tudelft.oopp.group39.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.config.AbstractEntity;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;

@Entity
@Table(name = Booking.TABLE_NAME)
@JsonIgnoreProperties(allowSetters = true, value = {
    Booking.COL_USER,
    Booking.COL_ROOM
})
public class Booking extends AbstractEntity {
    public static final String TABLE_NAME = "bookings";
    public static final String MAPPED_NAME = "booking";
    public static final String COL_DATE = "date";
    public static final String COL_USER = "user";
    public static final String COL_ROOM = "room";

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = User.MAPPED_NAME)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = Room.MAPPED_NAME)
    @JsonManagedReference
    private Room room;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Booking() {
    }

    /**
     * Doc. TODO Chuck
     *
     * @param date      date
     * @param startTime startTime
     * @param endTime   endTime
     * @param user      user
     * @param room      room
     */
    public Booking(LocalDate date, LocalTime startTime, LocalTime endTime, User user, Room room) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.room = room;

    }

    /**
     * Converts booking to bookingDto.
     *
     * @return the converted booking
     */
    public BookingDto toDto() {
        return new BookingDto(
            date,
            startTime,
            endTime,
            user.getUsername(),
            room.getId()
        );
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
        return Objects.equals(getId(), booking.getId())
            && Objects.equals(getDate(), booking.getDate())
            && Objects.equals(getStartTime(), booking.getStartTime())
            && Objects.equals(getEndTime(), booking.getEndTime())
            && Objects.equals(getUser(), booking.getUser())
            && Objects.equals(getRoom(), booking.getRoom());
    }
}

