package nl.tudelft.oopp.group39.user.services;

import java.util.List;
import nl.tudelft.oopp.group39.config.exceptions.ExistsException;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.config.exceptions.NotNullException;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.enums.Role;
import nl.tudelft.oopp.group39.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    /**
     * List all users.
     *
     * @return a list of users {@link User}.
     */
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a user.
     *
     * @return user by id {@link User}.
     */
    public User readUser(String id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(
            User.MAPPED_NAME,
            id
        ));
    }

    /**
     * Create a user.
     *
     * @return the created user {@link User}.
     */
    public User createUser(User newUser) {
        try {
            readUser(newUser.getUsername());
            throw new ExistsException(User.MAPPED_NAME, newUser.getUsername());

        } catch (NotFoundException e) {
            newUser.setPassword(encryptPassword(newUser.getPassword()));
            mapRoleForUser(newUser);

            userRepository.save(newUser);

            return newUser;
        } catch (NullPointerException e) {
            throw new NotNullException(User.MAPPED_NAME);
        }
    }

    /**
     * Update a user.
     *
     * @return the updated user {@link User}.
     */
    public User updateUser(String id, User newUser) throws NotFoundException {
        return userRepository.findById(id)
            .map(user -> {
                mapRoleForUser(newUser);
                user.setEmail(newUser.getEmail());
                user.setRole(newUser.getRole());

                return userRepository.save(user);
            }).orElseThrow(() -> new NotFoundException(User.MAPPED_NAME, id));
    }

    /**
     * Delete a user {@link User}.
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    /**
     * Will map the roles of a user to the roles in the db.
     *
     * @param user A user to map roles for
     */
    protected void mapRoleForUser(User user) {
        try {
            Role role = Role.valueOf(user.getRole().getAuthority());
            user.setRole(role);
        } catch (NullPointerException | IllegalArgumentException e) {
            user.setRole(Role.STUDENT);
        }
    }

    /**
     * Encrypts a password with the local password encoder.
     *
     * @param password An unencrypted password
     * @return An encrypted password
     */
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = this.userRepository.findUserByUsername(username);

        if (user == null) {
            throw new NotFoundException(User.MAPPED_NAME, username);
        }

        return user;
    }
}
