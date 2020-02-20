package nl.tudelft.oopp.group39.user;

import java.util.List;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    /**
     * GET Endpoint to retrieve all users.
     *
     * @return a list of users {@link User}.
     */
    @GetMapping("/")
    public List<User> listUsers() {
        return service.listUsers();
    }

    /**
     * POST Endpoint to retrieve an user.
     *
     * @return the created user {@link User}.
     */
    @PostMapping("/")
    public User postUser(@RequestBody User user) {
        return service.createUser(user);
    }

    /**
     *
     * POST Endpoint that creates a user without an image, and then returns said user.
     *
     * @param id id of the user
     * @param email email address of the user
     * @param password password of the user
     * @param role role of the user. Currently 3 roles(STUDENT, STAFF, ADMIN) exist.
     *
     * @return the requested user {@link User}
     */
    @PostMapping("/addUser")
    public User insertUser(@RequestParam String id,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam User.Role role) {
        if (role==null) role = User.Role.STUDENT;
        return service.insertUser(id,email,password,role);}

    /**
     * GET Endpoint to retrieve an user.
     *
     * @return the requested user {@link User}.
     */
    @GetMapping("/{id}")
    public User readUser(@PathVariable String id) {
        return service.readUser(id);
    }

    /**
     * PUT Endpoint to update an user.
     *
     * @return the updated user {@link User}.
     */
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable String id) {
        return service.updateUser(user, id);
    }

    /**
     * DELETE Endpoint to delete am user.
     */
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        service.deleteUser(id);
    }
}
