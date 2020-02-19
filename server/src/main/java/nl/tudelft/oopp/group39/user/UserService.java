package nl.tudelft.oopp.group39.user;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    /**
     * List all users.
     *
     * @return a list of users {@link User}.
     */
    public List<User> listUsers() {
        List<User> users = new ArrayList<>();
        repository.findAll().forEach(users::add);

        return users;
    }

    /**
     * Get an user.
     *
     * @return user by id {@link User}.
     */
    public User readUser(String id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Create an user.
     *
     * @return the created user {@link User}.
     */
    public User createUser(User newUser) {
        try {
            User user = readUser(newUser.getId());
            throw new UserExistsException(user.getId());
        } catch (UserNotFoundException e) {
            repository.save(newUser);

            return newUser;
        }
    }

    /**
     * Update an user.
     *
     * @return the updated user {@link User}.
     */
    public User updateUser(User newEmployee, String id) throws UserNotFoundException {
        return repository.findById(id)
            .map(user -> {
                user.setEmail(newEmployee.getEmail());
                user.setPassword(newEmployee.getPassword());
                user.setImage(newEmployee.getImage());
                user.setRole(newEmployee.getRole());
                return repository.save(user);
            }).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Delete an user {@link User}.
     */
    public void deleteUser(String id) throws UserNotFoundException {
        readUser(id);
        repository.deleteById(id);
    }
}
