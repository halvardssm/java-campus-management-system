package nl.tudelft.oopp.group39.reservation.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.user.entities.User;

@Entity
@Table(name = Reservation.TABLE_NAME)
public class Reservation {
    public static final String TABLE_NAME = "reservations";
    public static final String MAPPED_NAME = "reservation";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    private LocalDateTime timeOfPickup;
    @ManyToOne
    @JoinColumn(name = User.MAPPED_NAME)
    private User user;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = TABLE_NAME + "_" + Reservable.TABLE_NAME,
        joinColumns = {
            @JoinColumn(name = MAPPED_NAME, referencedColumnName = "id",
                nullable = false, updatable = false)},
        inverseJoinColumns = {
            @JoinColumn(name = Reservable.MAPPED_NAME, referencedColumnName = "id",
                nullable = false, updatable = false)})
    private Set<Reservable> reservables;

    public Reservation() {
    }

    /**
     * Constructor of Reservation.
     *
     * @param timeOfPickup the time of the pickup
     * @param user         the user
     * @param reservables  all items in order
     */
    public Reservation(LocalDateTime timeOfPickup, User user, Set<Reservable> reservables) {
        setTimeOfPickup(timeOfPickup);
        setUser(user);
        setReservables(reservables);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTimeOfPickup() {
        return timeOfPickup;
    }

    public void setTimeOfPickup(LocalDateTime timeOfPickup) {
        this.timeOfPickup = timeOfPickup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Reservable> getReservables() {
        return reservables;
    }

    public void setReservables(Set<Reservable> reservables) {
        this.reservables = reservables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getTimeOfPickup(), that.getTimeOfPickup())
            && Objects.equals(getUser(), that.getUser())
            && Objects.equals(getReservables(), that.getReservables());
    }
}
