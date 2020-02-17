package nl.tudelft.oopp.demo.controllers;

import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("user")
    @ResponseBody
    public List<User> listUsers() {
        return repository.findAll();
    }

    /**
     * GET Endpoint to retrieve a user.
     *
     * @return randomly selected {@link User}.
     */
    @GetMapping("user/{id}")
    @ResponseBody
    public User readUser(String id) {
        return repository.findById(id).get();
    }

    /**
     * POST Endpoint to retrieve a user.
     *
     * @return randomly selected {@link User}.
     */
    @PostMapping("user")
    @ResponseBody
    public User postUser() {
        User u1 = new User(
                "test",
                "test@tudelft.nl",
                "Albert-Einstein"
        );

        repository.save(u1);

        return u1;
    }
}
