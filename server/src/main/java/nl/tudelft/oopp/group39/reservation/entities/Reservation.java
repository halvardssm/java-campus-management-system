package nl.tudelft.oopp.group39.reservation.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import nl.tudelft.oopp.group39.config.Constants;
import nl.tudelft.oopp.group39.user.entities.User;

@Entity
@Table(name = Reservation.TABLE_NAME)
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = Reservation.COL_ID
)
public class Reservation {
    public static final String TABLE_NAME = "reservations";
    public static final String MAPPED_NAME = "reservation";
    public static final String COL_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(pattern = Constants.FORMAT_DATE_TIME)
    private LocalDateTime timeOfPickup;
    @ManyToOne
    @JoinColumn(name = User.MAPPED_NAME)
    private User user;
    @OneToMany(mappedBy = MAPPED_NAME)
    private Set<ReservationAmount> reservationAmounts = new HashSet<>();

    public Reservation() {
    }

    /**
     * Constructor of Reservation.
     *
     * @param timeOfPickup       the time of the pickup
     * @param user               the user
     * @param reservationAmounts all items in order
     */
    public Reservation(
        LocalDateTime timeOfPickup,
        User user,
        Set<ReservationAmount> reservationAmounts
    ) {
        setTimeOfPickup(timeOfPickup);
        setUser(user);
        this.reservationAmounts.addAll(reservationAmounts != null
                                       ? reservationAmounts
                                       : new HashSet<>());
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

    public Set<ReservationAmount> getReservationAmounts() {
        return reservationAmounts;
    }

    public void setReservationAmounts(Set<ReservationAmount> reservables) {
        this.reservationAmounts = reservables;
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
            && Objects.equals(getReservationAmounts(), that.getReservationAmounts());
    }
}
