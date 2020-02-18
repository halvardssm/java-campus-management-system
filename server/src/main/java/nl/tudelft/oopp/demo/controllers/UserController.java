package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.exceptions.UserNotFoundException;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository repository;

    /**
     * GET Endpoint to retrieve all users.
     *
     * @return randomly selected {@link User}.
     */
    @GetMapping("/user")
    @ResponseBody
    public List<User> listUsers() {
        return repository.findAll();
    }

    /**
     * GET Endpoint to retrieve a user.
     *
     * @return randomly selected {@link User}.
     */
    @GetMapping("/user/{id}")
    @ResponseBody
    public User readUser(@PathVariable String id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * POST Endpoint to retrieve a user.
     *
     * @return randomly selected {@link User}.
     */
    @PostMapping("/user")
    @ResponseBody
    public User postUser(@RequestBody User user) {
        repository.save(user);

        return user;
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newEmployee, @PathVariable String id) {

        return repository.findById(id)
            .map(user -> {
                user.setEmail(newEmployee.getEmail());
                user.setPassword(newEmployee.getPassword());
                user.setImage(newEmployee.getImage());
                user.setRole(newEmployee.getRole());
                return repository.save(user);
            }).orElseThrow(()->new UserNotFoundException(id));
    }

    @DeleteMapping("/user/{id}")
    void deleteEmployee(@PathVariable String id) {
        repository.deleteById(id);
    }
}
