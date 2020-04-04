package nl.tudelft.oopp.group39.config.abstracts;

import java.util.function.Supplier;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractController {
    public static final String EXCEPTION_ACCESS_DENIED = "This user cannot access this resource";

    @Autowired
    protected JwtService jwtService;
    @Autowired
    private UserService userService;

    /**
     * Retrieves the token from the header.
     *
     * @param header the full header starting with Bearer
     * @return the token
     */
    protected String getTokenFromHeader(String header) {
        if (header != null && header.startsWith(Constants.HEADER_BEARER)) {
            return header.substring(7);
        }
        return null;
    }

    /**
     * Retrieves the username from the header.
     *
     * @param header the header
     * @return the username
     */
    protected String getUsernameFromAuthHeader(String header) {
        String token = getTokenFromHeader(header);
        return jwtService.decryptUsername(token);
    }


    protected Boolean isValidUserRequest(String header, Role role, String user) {
        String username = getUsernameFromAuthHeader(header);

        if (username != null) {
            return (username.equals(user))
                || userService.readUser(username).getRole() == role;
        }

        return false;
    }

    protected void validateUserRequest(String header, String user) throws AccessDeniedException {
        if (!isValidUserRequest(header, Role.ADMIN, user)) {
            throw new AccessDeniedException(EXCEPTION_ACCESS_DENIED);
        }
    }

    /**
     * Handles a request and returns the response.
     *
     * @param header   the header of the request
     * @param status   the http status
     * @param username the username of the object in the request
     * @param fn       the callback function
     * @return the response
     */
    public ResponseEntity<RestResponse<Object>> restHandler(
        String header,
        HttpStatus status,
        Supplier<String> username,
        Supplier<Object> fn
    ) {
        try {
            if (header != null) {
                validateUserRequest(header, Utils.safeNull(username));
            }

            Object result = fn.get();

            return RestResponse.create(result, null, status);
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * Handles a request and returns the response.
     *
     * @param header   the header of the request
     * @param status   the http status
     * @param username the username of the object in the request
     * @param fn       the callback function
     * @return the response
     */
    public ResponseEntity<RestResponse<Object>> restHandler(
        String header,
        HttpStatus status,
        HttpStatus error,
        Supplier<String> username,
        Supplier<Object> fn
    ) {
        try {
            if (header != null) {
                validateUserRequest(header, Utils.safeNull(username));
            }

            Object result = fn.get();

            return RestResponse.create(result, null, status);
        } catch (Exception e) {
            return RestResponse.error(e, error);
        }
    }

    /**
     * Handles a request and returns the response.
     *
     * @param header   the header of the request
     * @param username the username of the object in the request
     * @param fn       the callback function
     * @return the response
     */
    public ResponseEntity<RestResponse<Object>> restHandler(
        String header,
        Supplier<String> username,
        Supplier<Object> fn
    ) {
        return restHandler(header, HttpStatus.OK, username, fn);
    }

    /**
     * Handles a request and returns the response.
     *
     * @param status the http status
     * @param fn     the callback function
     * @return the response
     */
    public ResponseEntity<RestResponse<Object>> restHandler(
        HttpStatus status,
        Supplier<Object> fn
    ) {
        return restHandler(null, status, null, fn);
    }

    /**
     * Handles a request and returns the response.
     *
     * @param fn the callback function
     * @return the response
     */
    public ResponseEntity<RestResponse<Object>> restHandler(
        Supplier<Object> fn
    ) {
        return restHandler(null, HttpStatus.OK, null, fn);
    }
}
