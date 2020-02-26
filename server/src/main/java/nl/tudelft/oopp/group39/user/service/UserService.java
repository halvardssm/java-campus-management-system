package nl.tudelft.oopp.group39.user.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import nl.tudelft.oopp.group39.role.entity.Role;
import nl.tudelft.oopp.group39.role.enums.Roles;
import nl.tudelft.oopp.group39.role.service.RoleService;
import nl.tudelft.oopp.group39.user.entity.User;
import nl.tudelft.oopp.group39.user.exceptions.UserExistsException;
import nl.tudelft.oopp.group39.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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

    @Autowired
    private RoleService roleService;

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
            User user = readUser(newUser.getUsername());
            throw new UserExistsException(user.getUsername());

        } catch (UsernameNotFoundException e) {
            newUser.setPassword(encryptPassword(newUser.getPassword()));
            mapRolesForUser(newUser);

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
                mapRolesForUser(newUser);
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

    private void mapRolesForUser(User user) {
        List<GrantedAuthority> roles = new ArrayList<>();

        for (GrantedAuthority authority : user.getAuthorities()) {
            Role role = roleService.readRole(authority.getAuthority());

            if (role != null) {
                roles.add(role);
            }
        }

        user.setAuthorities(roles);
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @PostConstruct
    private void initUsers() {
        User user = new User(
            "admin",
            "admin@tudelft.nl",
            encryptPassword("pwd"),
            null,
            List.of(
                new Role(Roles.ROLE_ADMIN),
                new Role(Roles.ROLE_STAFF),
                new Role(Roles.ROLE_STUDENT)
            )
        );

        userRepository.saveAndFlush(user);
        System.out.println("[SEED] Admin user created");
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
