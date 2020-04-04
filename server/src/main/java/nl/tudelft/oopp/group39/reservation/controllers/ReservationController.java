package nl.tudelft.oopp.group39.reservation.controllers;

import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.reservation.dto.ReservationDto;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.services.ReservationService;
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
@RequestMapping(ReservationController.REST_MAPPING)
public class ReservationController extends AbstractController {
    public static final String REST_MAPPING = "/reservation";

    @Autowired
    private ReservationService reservationService;

    /**
     * GET Endpoint to retrieve all reservations.
     *
     * @return a list of reservations {@link Reservation}.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(@RequestParam Map<String, String> params) {
        return restHandler((p) -> Utils.listEntityToDto(reservationService.filterReservations(params)));
    }

    /**
     * POST Endpoint to retrieve an reservation.
     *
     * @return the created reservation {@link Reservation}.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @RequestBody ReservationDto reservation
    ) {
        return restHandler(
            header,
            Utils.safeNull((p) -> reservation.getUser()),
            HttpStatus.CREATED,
            (p) -> reservationService.createReservation(reservation).toDto()
        );
    }

    /**
     * GET Endpoint to retrieve a reservation.
     *
     * @return the requested reservation {@link Reservation}.
     */
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(@PathVariable Long id) {
        return restHandler((p) -> reservationService.readReservation(id).toDto());
    }

    /**
     * PUT Endpoint to update a reservation.
     *
     * @return the updated reservation {@link Reservation}.
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @PathVariable Long id,
        @RequestBody ReservationDto reservation
    ) {
        return restHandler(
            header,
            Utils.safeNull((p) -> reservation.getUser()),
            (p) -> reservationService.updateReservation(id, reservation).toDto()
        );
    }

    /**
     * DELETE Endpoint to delete a reservation.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> deleteReservation(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @PathVariable Long id
    ) {
        return restHandler(
            header,
            reservationService.readReservation(id).getUser().getUsername(),
            (p) -> {
                reservationService.deleteReservation(id);

                return null;
            }
        );
    }
}
