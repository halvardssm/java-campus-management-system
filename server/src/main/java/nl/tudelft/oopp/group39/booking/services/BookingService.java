package nl.tudelft.oopp.group39.booking.services;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.booking.dao.BookingDao;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.exceptions.BookingNotFoundException;
import nl.tudelft.oopp.group39.booking.repositories.BookingRepository;
import nl.tudelft.oopp.group39.room.entities.Room;
import nl.tudelft.oopp.group39.room.services.RoomService;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    /**
     * List all bookings.
     *
     * @return a list of bookings {@link Booking}.
     */
    public List<Booking> listBookings(Map<String, String> params) {
        return bookingDao.listBookings(params);
    }

    /**
     * Get a booking.
     *
     * @return booking by id {@link Booking}.
     */
    public Booking readBooking(Long id) throws BookingNotFoundException {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new BookingNotFoundException(id));
    }

    /**
     * Create a booking.
     *
     * @return the created booking {@link Booking}.
     */
    public Booking createBooking(Booking newBooking) throws IllegalArgumentException {
        return bookingRepository.save(newBooking);
    }

    /**
     * Create a booking.
     *
     * @return the created booking {@link Booking}.
     */
    public Booking createBooking(BookingDto newBooking)
        throws IllegalArgumentException {
        User user = userService.readUser(newBooking.getUser());
        Room room = roomService.readRoom(newBooking.getRoom());
        Booking booking = new Booking(
            null,
            newBooking.getDate(),
            newBooking.getStartTime(),
            newBooking.getEndTime(),
            user,
            room
        );

        return createBooking(booking);
    }

    /**
     * Update a booking.
     *
     * @return the updated booking {@link Booking}.
     */
    public Booking updateBooking(Booking newBooking, Long id) throws BookingNotFoundException {
        return bookingRepository.findById(id)
            .map(booking -> {
                booking.setDate(newBooking.getDate());
                booking.setStartTime(newBooking.getStartTime());
                booking.setEndTime(newBooking.getEndTime());
                booking.setUser(newBooking.getUser());
                booking.setRoom(newBooking.getRoom());
                return bookingRepository.save(newBooking);
            })
            .orElseThrow(() -> new BookingNotFoundException(id));
    }

    /**
     * Update a booking.
     *
     * @return the updated booking {@link Booking}.
     */
    public Booking updateBooking(BookingDto newBooking, Long id)
        throws BookingNotFoundException {
        User user = userService.readUser(newBooking.getUser());
        Room room = roomService.readRoom(newBooking.getRoom());

        Booking booking = new Booking(
            null,
            newBooking.getDate(),
            newBooking.getStartTime(),
            newBooking.getEndTime(),
            user,
            room
        );

        return updateBooking(booking, id);
    }

    /**
     * Delete a booking {@link Booking}.
     */
    public Booking deleteBooking(Long id) throws BookingNotFoundException {
        try {
            Booking rf = readBooking(id);
            bookingRepository.delete(readBooking(id));
            return rf;
        } catch (BookingNotFoundException e) {
            throw new BookingNotFoundException(id);
        }
    }
}
