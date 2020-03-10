package nl.tudelft.oopp.auth.controllers;

import nl.tudelft.oopp.auth.entities.AuthRequest;
import nl.tudelft.oopp.auth.entities.AuthResponse;
import nl.tudelft.oopp.auth.exceptions.UnauthorizedException;
import nl.tudelft.oopp.auth.services.JwtService;
import nl.tudelft.oopp.config.RestResponse;
import nl.tudelft.oopp.user.entities.User;
import nl.tudelft.oopp.user.services.UserService;
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
     * @param authRequest An object with username and password
     * @return A JWT string
     * @throws UnauthorizedException Is thrown when the username or password is incorrect
     */
    @PostMapping(REST_MAPPING)
    public ResponseEntity<RestResponse<AuthResponse>> createToken(@RequestBody AuthRequest authRequest)
        throws UnauthorizedException {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
            );

            authenticationManager.authenticate(token);
        } catch (Exception e) {
            return RestResponse.create(
                null,
                new UnauthorizedException().getMessage(),
                HttpStatus.UNAUTHORIZED
            );
        }

        User user = userService.readUser(authRequest.getUsername());

        String jwt = jwtService.encrypt(user);

        return RestResponse.create(new AuthResponse(jwt));
    }
}
