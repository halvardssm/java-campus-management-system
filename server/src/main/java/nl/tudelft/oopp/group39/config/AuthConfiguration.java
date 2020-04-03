package nl.tudelft.oopp.group39.config;

import nl.tudelft.oopp.group39.auth.controllers.AuthController;
import nl.tudelft.oopp.group39.auth.filters.JwtFilter;
import nl.tudelft.oopp.group39.booking.controllers.BookingController;
import nl.tudelft.oopp.group39.building.controllers.BuildingController;
import nl.tudelft.oopp.group39.event.controllers.EventController;
import nl.tudelft.oopp.group39.facility.controllers.FacilityController;
import nl.tudelft.oopp.group39.reservation.controllers.ReservationController;
import nl.tudelft.oopp.group39.room.controllers.RoomController;
import nl.tudelft.oopp.group39.user.controllers.UserController;
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
    @Autowired private UserService userService;
    @Autowired private JwtFilter jwtFilter;

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
            .antMatchers(HttpMethod.POST, AuthController.REST_MAPPING).permitAll()
            .antMatchers(HttpMethod.POST, UserController.REST_MAPPING).permitAll()
            .antMatchers(// Add here at the end the methods that should be available for the guest
                         HttpMethod.GET,
                         RoomController.REST_MAPPING,
                         FacilityController.REST_MAPPING,
                         BuildingController.REST_MAPPING,
                         EventController.REST_MAPPING,
                         BookingController.REST_MAPPING,
                         ReservationController.REST_MAPPING
            ).permitAll()
            .antMatchers("/**").permitAll()
            .antMatchers("/**/*").permitAll()
            .anyRequest().authenticated()
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

