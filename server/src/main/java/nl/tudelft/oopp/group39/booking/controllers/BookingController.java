package nl.tudelft.oopp.group39.booking.controllers;

import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.config.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(BookingController.REST_MAPPING)
public class BookingController {
    public static final String REST_MAPPING = "/booking";

    @Autowired
    private BookingService bookingService;

    @GetMapping("")
    public List<Booking> listBookings() {
        return bookingService.listBookings();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteBooking(@PathVariable int id) {
        bookingService.deleteBooking(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> createBooking(@RequestBody Booking newBooking) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        return RestResponse.create(bookingService.createBooking(newBooking), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> readBooking(@PathVariable int id) {
        return RestResponse.create(bookingService.readBooking(id));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> updateBooking(@RequestBody Booking updated, @PathVariable int id) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed)
        return RestResponse.create(bookingService.updateBooking(updated, id));
    }

}
