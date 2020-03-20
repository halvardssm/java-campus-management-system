package nl.tudelft.oopp.group39.user.entities;

import static nl.tudelft.oopp.group39.config.Utils.initSet;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.sql.Blob;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.booking.entities.Booking;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.user.enums.Role;
import org.hibernate.annotations.LazyGroup;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = User.TABLE_NAME)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = User.COL_USERNAME
)
public class User implements UserDetails {
    public static final String TABLE_NAME = "users";
    public static final String MAPPED_NAME = "user";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_IMAGE = "image";
    public static final String COL_ROLE = "role";
    public static final String COL_BOOKINGS = "bookings";

    @Id
    private String username;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @LazyGroup("lobs")
    @JsonIgnore
    private Blob image;
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Booking> bookings = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Reservation> reservations = new HashSet<>();

    public User() {
    }

    /**
     * Create a new User instance.
     *
     * @param username     Unique identifier as to be used in the database.
     * @param email        Email address of the user.
     * @param password     Encrypted password of the user.
     * @param role         Role of the user.
     * @param image        Image of the user.
     * @param bookings     Bookings of user.
     * @param reservations Reservations of user.
     */
    public User(
        String username,
        String email,
        String password,
        Blob image,
        Role role,
        Set<Booking> bookings,
        Set<Reservation> reservations
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.image = image;
        this.bookings.addAll(initSet(bookings));
        this.reservations.addAll(initSet(reservations));
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

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername())
            && Objects.equals(getEmail(), user.getEmail())
            && Objects.equals(getPassword(), user.getPassword())
            && Objects.equals(getImage(), user.getImage())
            && getRole() == user.getRole()
            && Objects.equals(getBookings(), user.getBookings())
            && Objects.equals(getReservations(), user.getReservations());
    }
}
