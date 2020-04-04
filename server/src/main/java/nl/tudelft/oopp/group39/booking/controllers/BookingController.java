package nl.tudelft.oopp.group39.booking.controllers;

import java.util.Map;
import nl.tudelft.oopp.group39.booking.dto.BookingDto;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BookingController.REST_MAPPING)
public class BookingController extends AbstractController {
    public static final String REST_MAPPING = "/booking";

    @Autowired
    private BookingService bookingService;

    /**
     * GET endpoint to retrieve all bookings.
     *
     * @return a list of bookings {@link Booking}.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(@RequestParam Map<String, String> params) {
        return restHandler(() -> Utils.listEntityToDto(bookingService.listBookings(params)));
    }

    /**
     * POST endpoint to create a booking.
     *
     * @return the created booking {@link Booking}.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @RequestBody BookingDto newBooking
    ) {
        return restHandler(
            header,
            HttpStatus.CREATED,
            newBooking::getUser,
            () -> bookingService.createBooking(newBooking).toDto()
        );
    }

    /**
     * GET endpoint to retrieve the booking.
     *
     * @return the requested booking {@link Booking}.
     */
    @GetMapping(PATH_ID)
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler(() -> bookingService.readBooking(id).toDto());
    }

    /**
     * PUT endpoint to update the booking.
     *
     * @return the updated booking {@link Booking}.
     */
    @PutMapping(PATH_ID)
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @RequestBody BookingDto updated,
        @PathVariable Long id
    ) {
        return restHandler(
            header,
            updated::getUser,
            () -> bookingService.updateBooking(updated, id).toDto()
        );
    }

    /**
     * DELETE endpoint to delete the booking.
     */
    @DeleteMapping(PATH_ID)
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @PathVariable Long id
    ) {
        return restHandler(
            header,
            () -> bookingService.readBooking(id).getUser().getUsername(),
            () -> {
                bookingService.deleteBooking(id);

                return null;
            }
        );
    }
}
