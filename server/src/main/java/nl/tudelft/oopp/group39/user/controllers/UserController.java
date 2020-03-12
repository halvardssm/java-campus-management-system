package nl.tudelft.oopp.group39.user.controllers;

import nl.tudelft.oopp.group39.config.RestResponse;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.REST_MAPPING)
public class UserController {
    public static final String REST_MAPPING = "/user";

    @Autowired
    private UserService service;

    /**
     * GET Endpoint to retrieve all users.
     *
     * @return a list of users {@link User}.
     */
    @GetMapping("")
    public ResponseEntity<RestResponse<Object>> listUsers() {
        try {
            return RestResponse.create(service.listUsers());
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }

    }

    /**
     * POST Endpoint to retrieve an user.
     *
     * @return the created user {@link User}.
     */
    @PostMapping("")
    public ResponseEntity<RestResponse<Object>> createUser(@RequestBody User user) {
        try {
            return RestResponse.create(service.createUser(user), null, HttpStatus.CREATED);
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * GET Endpoint to retrieve an user.
     *
     * @return the requested user {@link User}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> readUser(@PathVariable String id) {
        try {
            return RestResponse.create(service.readUser(id));
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * PUT Endpoint to update an user.
     *
     * @return the updated user {@link User}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateUser(
        @RequestBody User user,
        @PathVariable String id
    ) {
        try {
            return RestResponse.create(service.updateUser(id, user));
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }

    /**
     * DELETE Endpoint to delete am user.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteEmployee(@PathVariable String id) {
        try {
            service.deleteUser(id);

            return RestResponse.create(null, null, HttpStatus.OK);
        } catch (Exception e) {
            return RestResponse.error(e.getMessage());
        }
    }
}
