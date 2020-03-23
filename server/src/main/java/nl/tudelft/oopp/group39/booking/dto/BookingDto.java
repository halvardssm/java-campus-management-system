package nl.tudelft.oopp.group39.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class BookingDto {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String user;
    private Integer room;

    public BookingDto() {
    }

    /**
     * Constructor for BookingDto.
     *
     * @param date      date of the booking
     * @param startTime start time of the booking
     * @param endTime   end time of the booking
     * @param user      username
     * @param room      room id
     */
    public BookingDto(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String user,
        Integer room
    ) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.room = room;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }
}
