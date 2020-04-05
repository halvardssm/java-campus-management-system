package nl.tudelft.oopp.group39.user.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.config.abstracts.AbstractController;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.REST_MAPPING)
public class UserController extends AbstractController {
    public static final String REST_MAPPING = "/user";
    public static final String REST_MAPPING_ROLE = "/roles";

    @Autowired
    private UserService service;

    /**
     * GET Endpoint to retrieve all users.
     *
     * @return a list of users {@link User}.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> list(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @RequestParam Map<String, String> params
    ) {
        return restHandler(
            header,
            null,
            () -> service.listUsers(params)
        );
    }

    @GetMapping(REST_MAPPING_ROLE)
    public ResponseEntity<RestResponse<Object>> listUserRoles() {
        List<Role> enums = Arrays.asList(Role.values());
        return RestResponse.create(enums);
    }

    /**
     * POST Endpoint to create user.
     *
     * @return the created user {@link User}.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> create(
        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String header,
        @RequestBody User user
    ) {
        return restHandler(HttpStatus.CREATED, () -> {
            Boolean isAdmin = false;
            String token = getTokenFromHeader(header);

            if (token != null) {
                String username = jwtService.decryptUsername(getTokenFromHeader(header));

                isAdmin = service.readUser(username).getRole() == Role.ADMIN;
            }

            return service.createUser(user, isAdmin);
        });
    }

    /**
     * GET Endpoint to retrieve user.
     *
     * @return the requested user {@link User}.
     */
    @GetMapping(PATH_ID)
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> read(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @PathVariable String id
    ) {
        return restHandler(
            header,
            () -> service.readUser(id).getUsername(),
            () -> service.readUser(id)
        );
    }

    /**
     * PUT Endpoint to update user.
     *
     * @return the updated user {@link User}.
     */
    @PutMapping(PATH_ID)
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> update(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @PathVariable String id,
        @RequestBody User user
    ) {
        return restHandler(
            header,
            () -> service.readUser(id).getUsername(),
            () -> {
                Boolean isAdmin = false;
                String token = getTokenFromHeader(header);

                if (token != null) {
                    String username = jwtService.decryptUsername(getTokenFromHeader(header));

                    isAdmin = service.readUser(username).getRole() == Role.ADMIN;
                }

                return service.updateUser(id, user, isAdmin);
            }
        );
    }

    /**
     * DELETE Endpoint to delete user.
     */
    @DeleteMapping(PATH_ID)
    @ResponseBody
    public ResponseEntity<RestResponse<Object>> delete(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String header,
        @PathVariable String id
    ) {
        return restHandler(
            header,
            () -> service.readUser(id).getUsername(),
            () -> {
                service.deleteUser(id);

                return null;
            }
        );
    }
}
