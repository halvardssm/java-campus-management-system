package nl.tudelft.oopp.user.services;

import java.util.List;
import nl.tudelft.oopp.role.entities.Role;
import nl.tudelft.oopp.user.entities.User;
import nl.tudelft.oopp.user.exceptions.UserExistsException;
import nl.tudelft.oopp.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * Get an user.
     *
     * @return user by id {@link User}.
     */
    public User readUser(String id) throws UsernameNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
    }

    /**
     * Create an user.
     *
     * @return the created user {@link User}.
     */
    public User createUser(User newUser) {
        try {
            readUser(newUser.getUsername());
            throw new UserExistsException(newUser.getUsername());

        } catch (UsernameNotFoundException e) {
            newUser.setPassword(encryptPassword(newUser.getPassword()));
            mapRoleForUser(newUser);

            userRepository.save(newUser);

            return newUser;
        }
    }

    /**
     * Update an user.
     *
     * @return the updated user {@link User}.
     */
    public User updateUser(String id, User newUser) throws UsernameNotFoundException {
        return userRepository.findById(id)
            .map(user -> {
                newUser.setUsername(id);
                newUser.setPassword(encryptPassword(newUser.getPassword()));
                mapRoleForUser(newUser);
                return userRepository.save(newUser);
            }).orElseThrow(() -> new UsernameNotFoundException(id));
    }

    /**
     * Delete an user {@link User}.
     */
    public void deleteUser(String id) throws UsernameNotFoundException {
        readUser(id);
        userRepository.deleteById(id);
    }

    /**
     * Will map the roles of a user to the roles in the db.
     *
     * @param user A user to map roles for
     */
    private void mapRoleForUser(User user) {
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return user;
    }
}
