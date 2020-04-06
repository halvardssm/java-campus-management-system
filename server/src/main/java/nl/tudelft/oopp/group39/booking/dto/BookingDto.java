package nl.tudelft.oopp.group39.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.user.entities.User;
import org.springframework.stereotype.Component;

@Component
public class BookingDto extends AbstractDto<Booking, BookingDto> {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String user;
    private Long room;

    /**
     * Constructor for BookingDto.
     */
    public BookingDto() {
    }

    /**
     * Constructor for BookingDto.
     *
     * @param id        id
     * @param date      date of the booking
     * @param startTime start time of the booking
     * @param endTime   end time of the booking
     * @param user      username
     * @param room      room id
     */
    public BookingDto(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String user,
        Long room
    ) {
        setId(id);
        setDate(date);
        setStartTime(startTime);
        setEndTime(endTime);
        setUser(user);
        setRoom(room);
    }

    /**
     * Gets the date of the booking.
     *
     * @return the date of the booking
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Changes the date of the booking.
     *
     * @param date the new date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the starting time of the booking.
     *
     * @return the starting time of the booking
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Changes the starting time of the booking.
     *
     * @param startTime the new starting time
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the ending time of the booking.
     *
     * @return the ending time of the booking
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Changes the ending time of the booking.
     *
     * @param endTime the new ending time
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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
     * Changes the user of the booking.
     *
     * @param user the new user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the room that is booked.
     *
     * @return the room that is booked
     */
    public Long getRoom() {
        return room;
    }

    /**
     * Changes the room that is booked.
     *
     * @param room the new room that is booked
     */
    public void setRoom(Long room) {
        this.room = room;
    }

    /**
     * Changes a BookingDto to a Booking object.
     *
     * @return a booking object
     */
    @Override
    public Booking toEntity() {
        User user1 = new User();
        user1.setUsername(getUser());
        Room room1 = Utils.idToEntity(getRoom(), Room.class);
        return new Booking(
            getId(),
            getDate(),
            getStartTime(),
            getEndTime(),
            user1,
            room1
        );
    }
}
