package nl.tudelft.oopp.group39.booking.controllers;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BookingController.REST_MAPPING)
public class BookingController {
    public static final String REST_MAPPING = "/booking";

    @Autowired
    private BookingService bookingService;

    /**
     * GET Endpoint to retrieve all bookings.
     *
     * @return a list of bookings {@link Booking}.
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listBookings(
        @RequestParam Map<String, String> params
    ) {
        List<Booking> bookings = bookingService.listBookings(params);

        return RestResponse.create(Utils.listEntityToDto(bookings));
    }

    /**
     * POST Endpoint to create booking.
     *
     * @return the created booking {@link Booking}.
     */
    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createBooking(@RequestBody BookingDto newBooking) {
        try {
            return RestResponse.create(
                bookingService.createBooking(newBooking), null, HttpStatus.CREATED
            );
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * GET Endpoint to retrieve booking.
     *
     * @return the requested booking {@link Booking}.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> readBooking(@PathVariable Long id) {
        try {
            Booking booking = bookingService.readBooking(id);

            BookingDto bookingDto = booking.toDto();

            return RestResponse.create(bookingDto);
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * PUT Endpoint to update booking.
     *
     * @return the updated booking {@link Booking}.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateBooking(
        @RequestBody BookingDto updated,
        @PathVariable Long id
    ) {
        try {
            return RestResponse.create(bookingService.updateBooking(updated, id).toDto());
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * DELETE Endpoint to delete booking.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
