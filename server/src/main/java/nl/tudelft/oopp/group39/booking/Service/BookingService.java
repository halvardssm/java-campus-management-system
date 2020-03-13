package nl.tudelft.oopp.group39.booking.Service;

import nl.tudelft.oopp.group39.booking.Entities.Booking;
import nl.tudelft.oopp.group39.booking.Exceptions.BookingExistsException;
import nl.tudelft.oopp.group39.booking.Exceptions.BookingNotFoundException;
import nl.tudelft.oopp.group39.booking.Repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> listBookings() {
        return bookingRepository.findAll();
    }

    public Booking readBooking(long id) throws BookingNotFoundException {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException((int) id));
    }

    public Booking deleteBooking(long id) throws BookingNotFoundException {
        try {
            Booking rf = readBooking(id);
            bookingRepository.delete(readBooking(id));
            return rf;
        } catch (BookingNotFoundException e) {
            throw new BookingNotFoundException((int) id);
        }
    }

    public Booking createBooking(Booking newBooking) {
        try {
            Booking booking = readBooking((int) newBooking.getId());
            throw new BookingExistsException((int) booking.getId());

        } catch (BookingNotFoundException e) {
            bookingRepository.save(newBooking);
            return newBooking;
        }
    }

    public Booking updateBooking(Booking newBooking, int id) throws BookingNotFoundException {
        return bookingRepository.findById((long) id)
            .map(booking -> {
                booking.setRoomId(newBooking.getRoomId());
                booking.setDate(newBooking.getDate());
                booking.setStartTime(newBooking.getStartTime());
                booking.setEndTime(newBooking.getEndTime());
                booking.setUserId(newBooking.getUserId());
                return bookingRepository.save(booking);
            }).orElseThrow(() -> new BookingNotFoundException(id));
    }

}
