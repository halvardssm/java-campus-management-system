package nl.tudelft.oopp.group39.auth.filters;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import nl.tudelft.oopp.group39.AbstractTest;
import nl.tudelft.oopp.group39.config.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtFilterTest extends AbstractTest {
    private String jwt;

    @BeforeEach
    void setUp() {
        userService.createUser(testUser, true);
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