package nl.tudelft.oopp.user.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Blob;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import nl.tudelft.oopp.user.enums.Role;
import org.hibernate.annotations.LazyGroup;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = User.TABLE_NAME)
public class User implements UserDetails {
    public static final String TABLE_NAME = "users";

    @Id
    private String username;
    private String email;
    private String password;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @LazyGroup("lobs")
    private Blob image;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    /**
     * Create a new User instance.
     *
     * @param username Unique identifier as to be used in the database.
     * @param email    Email address of the user.
     * @param password Encrypted password of the user.
     * @param role     Role of the user.
     * @param image    Image of the user.
     */
    public User(
        String username,
        String email,
        String password,
        Blob image,
        Role role
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.image = image;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Blob getImage() {
        return this.image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(getRole());
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return getUsername().equals(user.getUsername())
            && getEmail().equals(user.getEmail())
            && getPassword().equals(user.getPassword())
            && Objects.equals(getImage(), user.getImage())
            && getRole().equals(user.getRole());
    }
}
