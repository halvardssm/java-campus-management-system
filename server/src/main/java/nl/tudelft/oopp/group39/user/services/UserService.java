package nl.tudelft.oopp.group39.user.services;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.config.exceptions.ExistsException;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.config.exceptions.NotNullException;
import nl.tudelft.oopp.group39.user.dao.UserDao;
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
    @Autowired
    private UserDao userDao;

    /**
     * List all users.
     *
     * @return a list of users {@link User}.
     */
    public List<User> listUsers(Map<String, String> params) throws IllegalArgumentException {
        return userDao.filter(params);
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
    public User createUser(User newUser, Boolean isAdmin) {
        try {
            readUser(newUser.getUsername());
            throw new ExistsException(User.MAPPED_NAME, newUser.getUsername());

        } catch (NotFoundException e) {


            mapRoleForUser(newUser, isAdmin);

            newUser.setPassword(encryptPassword(newUser.getPassword()));

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
    public User updateUser(String id, User newUser, Boolean isAdmin) throws NotFoundException {
        return userRepository.findById(id)
            .map(user -> {
                mapRoleForUser(newUser, isAdmin);
                user.setEmail(newUser.getEmail());
                user.setRole(newUser.getRole());

                if (newUser.getImage() != null) {
                    user.setImage(user.getImage());
                }

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
     * @param user    A user to map roles for
     * @param isAdmin If the request is done by an admin
     */
    public void mapRoleForUser(User user, Boolean isAdmin) {
        try {
            Role role = Role.valueOf(user.getRole().getAuthority());

            if (!isAdmin) {
                if (user.getRole() == Role.ADMIN
                    || (user.getRole() == Role.STAFF && !user.getEmail().endsWith("@tudelft.nl"))
                ) {
                    role = Role.STUDENT;
                }
            }

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
