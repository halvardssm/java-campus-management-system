package nl.tudelft.oopp.group39.reservation.controllers;

import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.reservation.dto.ReservationDto;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    @GetMapping
    public ResponseEntity<RestResponse<Object>> listReservations(
        @RequestParam Map<String,String> filters
    ) {
        return RestResponse.create(Utils.listEntityToDto(
            reservationService.filterReservations(filters)
        ));
    }

    /**
     * POST Endpoint to retrieve an reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    @PostMapping
    public ResponseEntity<RestResponse<Object>> createReservation(
        @RequestBody ReservationDto reservation
    ) {
        try {
            return RestResponse.create(
                reservationService.createReservation(reservation).toDto(),
                null,
                HttpStatus.CREATED
            );
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * GET Endpoint to retrieve an reservation.
     *
     * @return the requested reservation {@link Reservation}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> readReservation(@PathVariable Long id) {
        try {
            return RestResponse.create(
                reservationService.readReservation(id)
                .toDto());
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * PUT Endpoint to update an reservation.
     *
     * @return the updated reservation {@link Reservation}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateReservation(
        @PathVariable Long id,
        @RequestBody ReservationDto reservation
    ) {
        try {
            return RestResponse.create(
                reservationService.updateReservation(id, reservation).toDto()
            );
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * DELETE Endpoint to delete am reservation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
