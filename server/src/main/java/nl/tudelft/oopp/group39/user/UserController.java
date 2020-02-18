package nl.tudelft.oopp.group39.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    /**
     * GET Endpoint to retrieve all users.
     *
     * @return randomly selected {@link User}.
     */
    @GetMapping("/user")
    public List<User> listUsers() {
        return service.listUsers();
    }

    /**
     * GET Endpoint to retrieve a user.
     *
     * @return randomly selected {@link User}.
     */
    @GetMapping("/user/{id}")
    public User readUser(@PathVariable String id) {
        return service.readUser(id);
    }

    /**
     * POST Endpoint to retrieve a user.
     *
     * @return randomly selected {@link User}.
     */
    @PostMapping("/user")
    public User postUser(@RequestBody User user) {
        return service.createUser(user);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@RequestBody User user, @PathVariable String id) {
        return service.updateUser(user, id);
    }

    @DeleteMapping("/user/{id}")
    void deleteEmployee(@PathVariable String id) {
        service.deleteUser(id);
    }
}
