package nl.tudelft.oopp.demo.entities;

import org.hibernate.annotations.LazyGroup;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.sql.Blob;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;

    private String email;

    private String password;

    private String role;

    private String token;

    @Lob
    @Basic( fetch = FetchType.LAZY )
    @LazyGroup( "lobs" )
    private Blob image;

    private void createUser(String id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.role = role;
        this.token = UUID.fromString(id).toString();
    }

    public User() {}

    /**
     * Create a new User instance.
     *
     * @param id       Unique identifier as to be used in the database.
     * @param email    Email address of the user.
     * @param password Encrypted password of the user.
     */
    public User(String id, String email, String password) {
        createUser(id, email, password, "student");
    }

    /**
     * Create a new User instance.
     *
     * @param id       Unique identifier as to be used in the database.
     * @param email    Email address of the user.
     * @param password Encrypted password of the user.
     * @param role     Role of the user.
     */
    public User(String id, String email, String password, String role) {
        createUser(id, email, password, role);
    }

    public String getId() { return this.id; }

    public String getEmail() { return this.email; }

    public String getPassword() { return this.password; }

    public String getRole() { return this.role; }

    public String getToken() { return this.token; }

    public void setId(String id) { this.id = id; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public void setRole(String role) { this.role = role; }

    public void setToken(String token) { this.token = token; }

    public boolean passwordIsValid(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return this.id.equals(user.id)
                && this.email.equals(user.email)
                && this.password.equals(user.password)
                && this.role.equals(user.role)
                && this.token.equals(user.token);
    }
}
