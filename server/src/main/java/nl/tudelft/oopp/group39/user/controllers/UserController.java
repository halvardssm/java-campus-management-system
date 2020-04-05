package nl.tudelft.oopp.group39.user.controllers;

import java.util.Arrays;
import java.util.List;
import nl.tudelft.oopp.group39.config.RestResponse;
//import nl.tudelft.oopp.group39.event.enums.EventTypes;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.repositories.UserRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.REST_MAPPING)
public class UserController {
    public static final String REST_MAPPING = "/user";
    public static final String REST_MAPPING_ROLE = "/roles";

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;

    /**
     * GET Endpoint to retrieve all users.
     *
     * @return a list of users {@link User}.
     */
    @GetMapping
    public ResponseEntity<RestResponse<Object>> listUsers() {
        return RestResponse.create(service.listUsers());
    }

    @GetMapping("/filter")
    public ResponseEntity<RestResponse<Object>> filterUsersTemp(@RequestParam String name, @RequestParam String role) {
        return RestResponse.create(repository.filterUsers(name, role));
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
    public ResponseEntity<RestResponse<Object>> createUser(@RequestBody User user) {
        try {
            return RestResponse.create(service.createUser(user), null, HttpStatus.CREATED);
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * GET Endpoint to retrieve user.
     *
     * @return the requested user {@link User}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> readUser(@PathVariable String id) {
        try {
            return RestResponse.create(service.readUser(id));
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * PUT Endpoint to update user.
     *
     * @return the updated user {@link User}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> updateUser(
        @PathVariable String id,
        @RequestBody User user
    ) {
        try {
            return RestResponse.create(service.updateUser(id, user));
        } catch (Exception e) {
            return RestResponse.error(e);
        }
    }

    /**
     * DELETE Endpoint to delete user.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Object>> deleteUser(@PathVariable String id) {
        service.deleteUser(id);

        return RestResponse.create(null, null, HttpStatus.OK);
    }
}
