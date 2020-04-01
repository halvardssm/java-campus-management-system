package nl.tudelft.oopp.group39.reservation.controllers;

import static nl.tudelft.oopp.group39.reservation.controllers.ReservationController.REST_MAPPING;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import nl.tudelft.oopp.group39.AbstractControllerTest;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.dto.ReservationAmountDto;
import nl.tudelft.oopp.group39.reservation.dto.ReservationDto;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.user.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest extends AbstractControllerTest {
    private final Reservation testReservation = new Reservation(
        null,
        LocalDateTime.now(ZoneId.of("Europe/Paris")),
        LocalDateTime.now(ZoneId.of("Europe/Paris")).plusHours(2),
        null,
        null,
        null
    );
    private final ReservationAmount testReservationAmount = new ReservationAmount(
        null,
        2,
        null,
        null
    );
    private final Reservable testReservable = new Reservable(null, 5.4, null, null);
    private final ReservationAmountDto testReservationAmountDto =
        new ReservationAmountDto(null, 3, null);
    private final ReservationDto testReservationDto = new ReservationDto(
        null,
        LocalDateTime.now(ZoneId.of("Europe/Paris")),
        LocalDateTime.now(ZoneId.of("Europe/Paris")).plusHours(2),
        testUser.getUsername(),
        null,
        new HashSet<>(List.of(testReservationAmountDto))
    );

    private String jwt;

    @BeforeEach
    void setUp() {
        User user = userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);

        testReservation.setUser(user);
        Reservation reservation = reservationService.createReservation(testReservation);
        testReservation.setId(reservation.getId());
        testReservationAmount.setReservation(reservation);

        Reservable reservable = reservableService.createReservable(testReservable);
        testReservable.setId(reservable.getId());

        testReservationAmount.setReservable(reservable);
        testReservationAmountDto.setReservable(reservable.getId());

        ReservationAmount reservationAmount
            = reservationAmountService.createReservation(testReservationAmount);
        testReservationAmount.setId(reservationAmount.getId());
    }

    @Test
    void listReservations() throws Exception {
        mockMvc.perform(get(REST_MAPPING))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isArray())
            .andExpect(jsonPath("$.body", hasSize(1)))
            .andExpect(jsonPath(
                "$.body[0]." + Reservation.COL_ID,
                is(testReservation.getId().intValue())
            ))
            .andExpect(jsonPath(
                "$.body[0]." + Reservation.COL_TIME_OF_PICKUP,
                is(testReservation.getTimeOfPickup().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body[0]." + Reservation.COL_TIME_OF_DELIVERY,
                is(testReservation.getTimeOfDelivery().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body[0]." + Reservation.COL_USER + "." + User.COL_USERNAME,
                is(testUser.getUsername())
            ))
            .andExpect(jsonPath("$.body[0]." + Reservation.COL_RESERVATION_AMOUNTS).isArray())
            .andExpect(jsonPath("$.body[0]." + Reservation.COL_RESERVATION_AMOUNTS, hasSize(1)));
    }

    @Test
    void deleteAndCreateReservation() throws Exception {
        mockMvc.perform(delete(REST_MAPPING + "/" + testReservation.getId())
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").doesNotExist());

        String json = objectMapper.writeValueAsString(testReservationDto);

        mockMvc.perform(post(REST_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.body." + Reservation.COL_ID).isNumber())
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_TIME_OF_PICKUP,
                is(testReservationDto.getTimeOfPickup().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_TIME_OF_DELIVERY,
                is(testReservationDto.getTimeOfDelivery().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_USER,
                is(testUser.getUsername())
            ))
            .andExpect(jsonPath("$.body." + Reservation.COL_RESERVATION_AMOUNTS).isArray())
            .andDo((reservation) -> {
                String responseString = reservation.getResponse().getContentAsString();
                JsonNode productNode = new ObjectMapper().readTree(responseString);
                testReservation.setId(productNode.get("body").get("id").longValue());
            });
    }

    @Test
    void readReservation() throws Exception {
        mockMvc.perform(get(REST_MAPPING + "/" + testReservation.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_ID,
                is(testReservation.getId().intValue())
            ))
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_TIME_OF_PICKUP,
                is(testReservation.getTimeOfPickup().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_TIME_OF_DELIVERY,
                is(testReservation.getTimeOfDelivery().format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_USER,
                is(testUser.getUsername())
            ))
            .andExpect(jsonPath("$.body." + Reservation.COL_RESERVATION_AMOUNTS).isArray())
            .andExpect(jsonPath("$.body." + Reservation.COL_RESERVATION_AMOUNTS, hasSize(1)));
    }

    @Test
    void updateReservation() throws Exception {
        testReservationDto.setTimeOfPickup(testReservationDto.getTimeOfPickup().plusDays(3));
        testReservationDto.setTimeOfDelivery(testReservationDto.getTimeOfPickup().plusDays(5));

        String json = objectMapper.writeValueAsString(testReservationDto);

        mockMvc.perform(put(REST_MAPPING + "/" + testReservation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.body").isMap())
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_TIME_OF_PICKUP,
                is(testReservationDto.getTimeOfPickup()
                    .format(Constants.FORMATTER_DATE_TIME))
            ))
            .andExpect(jsonPath(
                "$.body." + Reservation.COL_TIME_OF_DELIVERY,
                is(testReservationDto.getTimeOfDelivery()
                    .format(Constants.FORMATTER_DATE_TIME))
            ));
    }

    @Test
    void testError() {
        assertEquals(
            "java.lang.NullPointerException",
            reservationController.create(null, null).getBody().getError()
        );

        assertEquals(
            "Reservation with id 0 wasn't found.",
            reservationController.read(0L).getBody().getError()
        );

        assertEquals(
            "Reservation with id 0 wasn't found.",
            reservationController.update(null, 0L, null).getBody().getError()
        );
    }
}