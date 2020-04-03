package nl.tudelft.oopp.group39.reservation.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservation.entities.Reservation;
import nl.tudelft.oopp.group39.reservation.entities.ReservationAmount;
import nl.tudelft.oopp.group39.user.entities.User;
import nl.tudelft.oopp.group39.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationDao {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    /**
     * Filter for reservations.
     *
     * @param filters filters to be used.
     * @return the filtered values.
     */
    public List<Reservation> reservationFilter(Map<String, String> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Reservation> rcq = cb.createQuery(Reservation.class);
        Root<Reservation> reservation = rcq.from(Reservation.class);

        Set<String> keys = filters.keySet();

        List<Predicate> allPredicates = new ArrayList<>();

        if (keys.contains(Reservation.COL_TIME_OF_PICKUP)) {
            allPredicates.add(cb.equal(
                reservation.get(Reservation.COL_TIME_OF_PICKUP).as(LocalDate.class),
                LocalDate.parse(
                    filters.get(Reservation.COL_TIME_OF_PICKUP).split("T")[0])
            ));
        }
        if (keys.contains(Reservation.COL_TIME_OF_DELIVERY)) {
            allPredicates.add(cb.greaterThanOrEqualTo(
                reservation.get(Reservation.COL_TIME_OF_DELIVERY),
                LocalDateTime.parse(filters.get(Reservation.COL_TIME_OF_DELIVERY))));
        }
        if (keys.contains(Reservation.COL_ROOM)) {
            allPredicates.add(cb.equal(
                reservation.get(User.MAPPED_NAME),
                userRepository.getOne((filters.get(User.MAPPED_NAME)))
            ));
        }
        if (keys.contains(User.MAPPED_NAME)) {
            allPredicates.add(cb.equal(
                reservation.get(User.MAPPED_NAME),
                userRepository.getOne((filters.get(User.MAPPED_NAME)))
            ));
        }
        if (keys.contains(Reservable.MAPPED_NAME)) {
            List<Integer> fvals = new ArrayList<>((Arrays.stream(
                filters.get(
                    Reservable.MAPPED_NAME)
                    .split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList())
            ));

            CriteriaQuery<ReservationAmount> recq = cb.createQuery(ReservationAmount.class);
            Root<ReservationAmount> reservationAmount = recq.from(ReservationAmount.class);

            recq.select(reservationAmount.get(Reservation.MAPPED_NAME))
                .where((reservationAmount.get(Reservable.MAPPED_NAME)
                    .get(Reservable.COL_ID))
                    .in(fvals));

            allPredicates.add(reservation.in(em.createQuery(recq).getResultList()));
        }
        rcq.where(cb.and(allPredicates.toArray(new Predicate[0])));
        TypedQuery<Reservation> query = em.createQuery(rcq);
        return query.getResultList();
    }
}
