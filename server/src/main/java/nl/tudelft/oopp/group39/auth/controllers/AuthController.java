package nl.tudelft.oopp.group39.auth.controllers;

import nl.tudelft.oopp.group39.auth.dto.AuthRequestDto;
import nl.tudelft.oopp.group39.auth.dto.AuthResponseDto;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.config.exceptions.UnauthorizedException;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController extends AbstractController {
    public static final String REST_MAPPING = "/authenticate";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    /**
     * Endpoint to receive a JWT for the user if present.
     * Example of body:
     * {
     * "username": "test",
     * "password": "password"
     * }
     *
     * @param body An object with username and password
     * @return A JWT string
     * @throws UnauthorizedException Is thrown when the username or password is incorrect
     */
    @PostMapping(REST_MAPPING)
    public ResponseEntity<RestResponse<Object>> createToken(
        @RequestBody AuthRequestDto body
    ) throws UnauthorizedException {
        return restHandler(null, null, null, HttpStatus.UNAUTHORIZED, () -> {
            try {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    body.getUsername(),
                    body.getPassword()
                );

                authenticationManager.authenticate(token);
            } catch (Exception e) {
                throw new UnauthorizedException();
            }

            User user = userService.readUser(body.getUsername());

            String token = jwtService.encrypt(user);

            return new AuthResponseDto(token);
        });
    }
}
