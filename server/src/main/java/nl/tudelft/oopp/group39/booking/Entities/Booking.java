package nl.tudelft.oopp.group39.booking.Entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class Booking {
    private long id;
    private long bookingId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private long userId;

    public Booking() {
    }

    public Booking(long id, long bookingId, LocalDate date, LocalTime startTime, LocalTime endTime, long userId) {
        this.id = id;
        this.bookingId = bookingId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;

    }

    @Id
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
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
            getBookingId() == booking.getBookingId() &&
            getUserId() == booking.getUserId() &&
            getDate().equals(booking.getDate()) &&
            getStartTime().equals(booking.getStartTime()) &&
            getEndTime().equals(booking.getEndTime());
    }
}
