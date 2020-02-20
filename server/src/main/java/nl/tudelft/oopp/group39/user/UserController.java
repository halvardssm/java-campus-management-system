package nl.tudelft.oopp.group39.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return service.updateUser(id, user);
    }

    /**
     * DELETE Endpoint to delete am user.
     */
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        service.deleteUser(id);
    }
}
