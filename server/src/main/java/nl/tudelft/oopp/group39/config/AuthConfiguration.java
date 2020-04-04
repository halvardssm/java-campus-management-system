package nl.tudelft.oopp.group39.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.tudelft.oopp.group39.auth.controllers.AuthController;
import nl.tudelft.oopp.group39.auth.filters.JwtFilter;
import nl.tudelft.oopp.group39.booking.controllers.BookingController;
import nl.tudelft.oopp.group39.building.controllers.BuildingController;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.event.controllers.EventController;
import nl.tudelft.oopp.group39.facility.controllers.FacilityController;
import nl.tudelft.oopp.group39.reservable.controllers.BikeController;
import nl.tudelft.oopp.group39.reservable.controllers.FoodController;
import nl.tudelft.oopp.group39.reservation.controllers.ReservationController;
import nl.tudelft.oopp.group39.room.controllers.RoomController;
import nl.tudelft.oopp.group39.user.controllers.UserController;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
    private static String[] REQUESTS_AUTHENTICATION = {
        AuthController.REST_MAPPING,
        UserController.REST_MAPPING
    };
    private static String[] REQUESTS_PUBLIC = {
        BookingController.REST_MAPPING,
        BuildingController.REST_MAPPING,
        EventController.REST_MAPPING,
        FacilityController.REST_MAPPING,
        BikeController.REST_MAPPING,
        FoodController.REST_MAPPING,
        ReservationController.REST_MAPPING,
        RoomController.REST_MAPPING
    };
    private static String[] REQUESTS_ONLY_AUTHENTICATED = {
        UserController.REST_MAPPING,
        BookingController.REST_MAPPING,
        ReservationController.REST_MAPPING,
    };

    @Autowired
    private UserService userService;

    @Autowired
    private JwtFilter jwtFilter;

    private String[] addIdToRequests(String... strings) {
        List<String> result = new ArrayList<>();
        for (String string : strings) {
            result.add(string + AbstractController.PATH_ID);
        }
        return result.toArray(String[]::new);
    }

    private String[] generateRequests(String... strings) {
        List<String> result = new ArrayList<>(List.of(strings));
        result.addAll(List.of(addIdToRequests(strings)));
        return result.toArray(String[]::new);
    }

    /**
     * Configures the authentication.
     *
     * @throws Exception if there is an exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, generateRequests(REQUESTS_PUBLIC)).permitAll()
            .antMatchers(HttpMethod.POST, REQUESTS_AUTHENTICATION).permitAll()
            .antMatchers(HttpMethod.GET, generateRequests(UserController.REST_MAPPING))
            .authenticated()
            .antMatchers(
                HttpMethod.POST,
                Arrays.copyOfRange(
                    REQUESTS_ONLY_AUTHENTICATED,
                    1,
                    REQUESTS_ONLY_AUTHENTICATED.length
                )
            ).authenticated()
            .antMatchers(HttpMethod.PUT, addIdToRequests(REQUESTS_ONLY_AUTHENTICATED))
            .authenticated()
            .antMatchers(HttpMethod.DELETE, addIdToRequests(REQUESTS_ONLY_AUTHENTICATED))
            .authenticated()
            .anyRequest().hasAuthority(Role.ADMIN.name())
            .and().exceptionHandling()
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Configures the authentication globally.
     *
     * @throws Exception if an error occurs when adding the {@link UserDetailsService}
     *                   based authentication
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * Manages the authentication.
     *
     * @throws Exception if there is an exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Encodes the password.
     *
     * @return the encoded password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

