package nl.tudelft.oopp.group39.booking.services;

import java.util.List;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.exceptions.BookingNotFoundException;
import nl.tudelft.oopp.group39.booking.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    /**
     * List all bookings.
     *
     * @return a list of bookings {@link Booking}.
     */
    public List<Booking> listBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get a booking.
     *
     * @return booking by id {@link Booking}.
     */
    public Booking readBooking(Integer id) throws BookingNotFoundException {
        return bookingRepository.findById(id).orElseThrow(()
            -> new BookingNotFoundException(id));
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
     * Update a booking.
     *
     * @return the updated booking {@link Booking}.
     */
    public Booking updateBooking(Booking newBooking, Integer id) throws BookingNotFoundException {
        return bookingRepository.findById(id)
            .map(booking -> {
                booking.setDate(newBooking.getDate());
                booking.setStartTime(newBooking.getStartTime());
                booking.setEndTime(newBooking.getEndTime());
                booking.setUser(newBooking.getUser());
                booking.setRoom(newBooking.getRoom());
                return bookingRepository.save(newBooking);
            })
            .orElseThrow(()
                -> new BookingNotFoundException(id));
    }

    /**
     * Delete a booking {@link Booking}.
     */
    public Booking deleteBooking(Integer id) throws BookingNotFoundException {
        try {
            Booking rf = readBooking(id);
            bookingRepository.delete(readBooking(id));
            return rf;
        } catch (BookingNotFoundException e) {
            throw new BookingNotFoundException(id);
        }
    }

}
