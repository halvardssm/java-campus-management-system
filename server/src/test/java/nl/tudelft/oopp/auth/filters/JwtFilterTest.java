package nl.tudelft.oopp.auth.filters;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import nl.tudelft.oopp.auth.services.JwtService;
import nl.tudelft.oopp.role.entities.Role;
import nl.tudelft.oopp.role.enums.Roles;
import nl.tudelft.oopp.user.entities.User;
import nl.tudelft.oopp.user.repositories.UserRepository;
import nl.tudelft.oopp.user.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
class JwtFilterTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
    }

    @Test
    void doFilterInternal() throws ServletException, IOException {
        User testUser = new User(
            "test",
            "test@tudelft.nl",
            "test",
            null,
            new Role(Roles.STUDENT)
        );

        userService.createUser(testUser);

        String jwt = jwtService.encrypt(testUser);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo");
        request.addHeader(HttpHeaders.AUTHORIZATION, JwtService.HEADER_BEARER + jwt);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .satisfies(authentication -> {
                assertThat(authentication).isNotNull();
                assertThat(authentication.getName()).isEqualTo(testUser.getUsername());
            });
    }
}