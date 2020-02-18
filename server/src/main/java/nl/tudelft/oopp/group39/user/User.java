package nl.tudelft.oopp.group39.user;

import java.sql.Blob;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.LazyGroup;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private Role role;
    private String token;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("lobs")
    private Blob image;

    public User() {
    }

    /**
     * Create a new User instance.
     *
     * @param id       Unique identifier as to be used in the database.
     * @param email    Email address of the user.
     * @param password Encrypted password of the user.
     */
    public User(String id, String email, String password) {
        new User(id, email, password, Role.STUDENT);
    }

    /**
     * Create a new User instance.
     *
     * @param id       Unique identifier as to be used in the database.
     * @param email    Email address of the user.
     * @param password Encrypted password of the user.
     * @param role     Role of the user.
     */
    public User(String id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.role = role;
        this.token = UUID.fromString(id).toString();
        this.image = null;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Blob getImage() {
        return this.image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

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
            && this.token.equals(user.token)
            && this.image.equals(user.image);
    }

    public enum Role {
        STUDENT, STAFF, ADMIN
    }
}
