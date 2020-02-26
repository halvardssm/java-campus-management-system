package nl.tudelft.oopp.group39.auth.controller;

import nl.tudelft.oopp.group39.auth.entity.JwtRequest;
import nl.tudelft.oopp.group39.auth.entity.JwtResponse;
import nl.tudelft.oopp.group39.auth.exception.BadAuthException;
import nl.tudelft.oopp.group39.auth.service.JwtService;
import nl.tudelft.oopp.group39.user.entity.User;
import nl.tudelft.oopp.group39.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
     *
     * @param jwtRequest An object with username and password
     * @return A JWT string
     * @throws BadAuthException Is thrown when the username or password is incorrect
     */
    @PostMapping(REST_MAPPING)
    public ResponseEntity<?> createToken(@RequestBody JwtRequest jwtRequest)
        throws BadAuthException {
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                jwtRequest.getUsername(),
                jwtRequest.getPassword()
            );

            authenticationManager.authenticate(token);
        } catch (Exception e) {
            throw new BadAuthException();
        }

        User user = userService.readUser(jwtRequest.getUsername());

        String jwt = jwtService.encrypt(user);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
