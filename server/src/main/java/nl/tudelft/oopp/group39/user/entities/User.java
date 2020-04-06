package nl.tudelft.oopp.group39.user.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import nl.tudelft.oopp.group39.config.Utils;
import nl.tudelft.oopp.group39.event.entities.Event;
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
@JsonIgnoreProperties(allowSetters = true, value = {
    User.COL_BOOKINGS,
    User.COL_PASSWORD,
    User.COL_RESERVATIONS,
    User.COL_EVENTS
})
public class User implements UserDetails {
    public static final String TABLE_NAME = "users";
    public static final String MAPPED_NAME = "user";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_IMAGE = "image";
    public static final String COL_ROLE = "role";
    public static final String COL_BOOKINGS = "bookings";
    public static final String COL_RESERVATIONS = "reservations";
    public static final String COL_EVENTS = "reservations";

    @Id
    private String username;
    private String email;
    private String password;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @LazyGroup("lobs")
    private byte[] image;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    private Set<Booking> bookings = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    private Set<Reservation> reservations = new HashSet<>();
    @OneToMany(mappedBy = MAPPED_NAME, fetch = FetchType.EAGER)
    private Set<Event> events = new HashSet<>();

    /**
     * Creates a new User instance.
     */
    public User() {
    }

    /**
     * Create a new User instance.
     *
     * @param username Unique identifier as to be used in the database.
     * @param email    Email address of the user.
     * @param password Encrypted password of the user.
     * @param image    Image of the user.
     * @param role     Role of the user.
     */
    public User(
        String username,
        String email,
        String password,
        String image,
        Role role
    ) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setImage(image);
        setRole(role);
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Changes the username of the user.
     *
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Changes the password of the user.
     *
     * @param password the new password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the email of the user.
     *
     * @param email the new email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
    public Role getRole() {
        return role;
    }

    /**
     * Changes the role of the user.
     *
     * @param role the new role of the user
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    public String getImage() {
        return Utils.fromByteToString(image);
    }

    /**
     * Changes the image.
     *
     * @param image the new image
     */
    public void setImage(String image) {
        this.image = Utils.fromStringToByte(image);
    }

    /**
     * Gets the bookings of the user.
     *
     * @return the bookings of the user.
     */
    public Set<Booking> getBookings() {
        return bookings;
    }

    /**
     * Changes the bookings of the user.
     *
     * @param bookings the new bookings of the user
     */
    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * Gets the reservations of the user.
     *
     * @return the reservations of the user
     */
    public Set<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Changes the reservations of the user.
     *
     * @param reservations the new set of reservations of the user
     */
    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     * Gets the authorities of the user.
     *
     * @return a list with the roles of the user
     */
    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(getRole());
    }

    /**
     * Checks whether the account is not expired.
     *
     * @return true
     */
    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks whether the account is locked.
     *
     * @return true
     */
    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks whether the credentials are not expired.
     *
     * @return true
     */
    @Override
    @Transient
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks whether the user is enabled.
     *
     * @return true
     */
    @Override
    @Transient
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    /**
     * Checks whether two users are equal.
     *
     * @param o the other object
     * @return true if the two users are equal, false otherwise
     */
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
            && Objects.equals(getReservations(), user.getReservations())
            && Objects.equals(getEvents(), user.getEvents());
    }
}
