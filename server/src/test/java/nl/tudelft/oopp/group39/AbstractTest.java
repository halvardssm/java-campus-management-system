package nl.tudelft.oopp.group39;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;
import nl.tudelft.oopp.group39.auth.filters.JwtFilter;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.booking.dao.BookingDao;
import nl.tudelft.oopp.group39.booking.repositories.BookingRepository;
import nl.tudelft.oopp.group39.booking.services.BookingService;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.event.controllers.EventController;
import nl.tudelft.oopp.group39.event.repositories.EventRepository;
import nl.tudelft.oopp.group39.event.services.EventService;
import nl.tudelft.oopp.group39.reservable.controllers.BikeController;
import nl.tudelft.oopp.group39.reservable.controllers.FoodController;
import nl.tudelft.oopp.group39.reservable.services.BikeService;
import nl.tudelft.oopp.group39.reservable.services.FoodService;
import nl.tudelft.oopp.group39.reservable.services.ReservableService;
import nl.tudelft.oopp.group39.reservation.controllers.ReservationController;
import nl.tudelft.oopp.group39.reservation.repositories.ReservationRepository;
import nl.tudelft.oopp.group39.reservation.services.ReservationAmountService;
import nl.tudelft.oopp.group39.reservation.services.ReservationService;
import nl.tudelft.oopp.group39.user.controllers.UserController;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractTest {
    protected User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN,
        null,
        null
    );
    protected User testUserStudent = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.STUDENT,
        null,
        null
    );

    protected final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .setTimeZone(TimeZone.getTimeZone(Constants.DEFAULT_TIMEZONE))
        .setDateFormat(Constants.DATE_FORMAT_DATE_TIME)
        .registerModules(
            new SimpleModule().addSerializer(
                LocalDate.class,
                new LocalDateSerializer(Constants.FORMATTER_DATE)
            ),
            new SimpleModule().addSerializer(
                LocalTime.class,
                new LocalTimeSerializer(Constants.FORMATTER_TIME)
            ),
            new SimpleModule().addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(Constants.FORMATTER_DATE_TIME)
            )
        )
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Autowired
    protected BikeController bikeController;
    @Autowired
    protected EventController eventController;
    @Autowired
    protected FoodController foodController;
    @Autowired
    protected ReservationController reservationController;
    @Autowired
    protected UserController userController;

    @Autowired
    protected BikeService bikeService;
    @Autowired
    protected BookingService bookingService;
    @Autowired
    protected EventService eventService;
    @Autowired
    protected FoodService foodService;
    @Autowired
    protected JwtFilter jwtFilter;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected ReservableService reservableService;
    @Autowired
    protected ReservationAmountService reservationAmountService;
    @Autowired
    protected ReservationService reservationService;
    @Autowired
    protected UserService userService;

    @Autowired
    protected BookingRepository bookingRepository;
    @Autowired
    protected EventRepository eventRepository;
    @Autowired
    protected ReservationRepository reservationRepository;

    @MockBean
    protected BookingDao mockBookingDao;

}
