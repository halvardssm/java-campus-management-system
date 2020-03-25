package nl.tudelft.oopp.group39.auth.controllers;

import nl.tudelft.oopp.group39.auth.dto.AuthRequestDto;
import nl.tudelft.oopp.group39.auth.dto.AuthResponseDto;
import nl.tudelft.oopp.group39.auth.exceptions.UnauthorizedException;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.config.RestResponse;
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
public class AuthController {
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
    public ResponseEntity<RestResponse<AuthResponseDto>> createToken(
        @RequestBody AuthRequestDto body
    )
        throws UnauthorizedException {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                body.getUsername(),
                body.getPassword()
            );

            authenticationManager.authenticate(token);
        } catch (Exception e) {
            return RestResponse.create(
                null,
                UnauthorizedException.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED
            );
        }

        User user = userService.readUser(body.getUsername());

        String token = jwtService.encrypt(user);

        return RestResponse.create(new AuthResponseDto(token));
    }
}
