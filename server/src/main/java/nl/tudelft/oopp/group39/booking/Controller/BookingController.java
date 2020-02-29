package nl.tudelft.oopp.group39.booking.Controller;

import nl.tudelft.oopp.group39.booking.Entities.Booking;
import nl.tudelft.oopp.group39.booking.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    //Moet alle incoming http requests handelen en doorverwijzen naar BookingService

    @Autowired
    private BookingService service;

    @GetMapping("")
    public List<Booking> listBookings() {
        return service.listBookings();
    }

    @DeleteMapping("/{id}")
    public void DeleteBooking(@PathVariable int id) {
        service.deleteBooking(id);
    } //wrm is @Pathvariable int en niet long?

    @PostMapping("")
    @ResponseBody
    public String AddBooking(@RequestBody Booking booking) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed) {
        service.createBooking(booking);//, open, closed);
        return "saved";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Booking ReadBooking(@PathVariable int id) {
        return service.readBooking(id);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Booking updateBooking(@RequestBody Booking updated, @PathVariable int id) {//, @RequestParam LocalTime open, @RequestParam LocalTime closed)
        return service.updateBooking(updated, id);
    }

}
