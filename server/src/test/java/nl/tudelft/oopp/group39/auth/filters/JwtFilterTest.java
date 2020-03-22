package nl.tudelft.oopp.group39.auth.filters;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
class JwtFilterTest {
    private final User testUser = new User(
        "test",
        "test@tudelft.nl",
        "test",
        null,
        Role.ADMIN,
        null,
        null
    );
    private String jwt;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        userService.createUser(testUser);
        jwt = jwtService.encrypt(testUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        userService.deleteUser(testUser.getUsername());
    }

    @Test
    void doFilterInternal() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "/foo");
        request.addHeader(HttpHeaders.AUTHORIZATION, Constants.HEADER_BEARER + jwt);

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