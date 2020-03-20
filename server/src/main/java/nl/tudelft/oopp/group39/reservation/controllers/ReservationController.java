package nl.tudelft.oopp.group39.reservation.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.models.ReservationDto;
import nl.tudelft.oopp.group39.reservation.services.ReservationService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ReservationController.REST_MAPPING)
public class ReservationController {
    public static final String REST_MAPPING = "/reservation";

    @Autowired
    private ReservationService reservationService;

    /**
     * GET Endpoint to retrieve all reservations.
     *
     * @return a list of reservations {@link Reservation}.
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listReservations() {
        return RestResponse.create(reservationService.listReservations());
    }

    /**
     * POST Endpoint to retrieve an reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    @PostMapping("")
    public ResponseEntity<RestResponse<Object>> createReservation(
        @RequestBody ReservationDto reservation
    ) {
        try {
            return RestResponse.create(
                reservationService.createReservation(reservation),
                null,
                HttpStatus.CREATED
            );
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * GET Endpoint to retrieve an reservation.
     *
     * @return the requested reservation {@link Reservation}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> readReservation(@PathVariable Integer id) {
        try {
            return RestResponse.create(reservationService.readReservation(id));
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * PUT Endpoint to update an reservation.
     *
     * @return the updated reservation {@link Reservation}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateReservation(
        @PathVariable Integer id,
        @RequestBody Reservation reservation
    ) {
        try {
            return RestResponse.create(reservationService.updateReservation(id, reservation));
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * DELETE Endpoint to delete am reservation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
