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

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

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
