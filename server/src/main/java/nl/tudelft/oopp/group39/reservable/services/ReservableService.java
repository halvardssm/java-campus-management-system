package nl.tudelft.oopp.group39.reservable.services;

import java.util.List;
import java.util.Map;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.reservable.dao.ReservableDao;
import nl.tudelft.oopp.group39.reservable.entities.Reservable;
import nl.tudelft.oopp.group39.reservable.exceptions.ReservableNotFoundException;
import nl.tudelft.oopp.group39.reservable.repositories.ReservableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservableService {
    public static final String EXCEPTION_RESERVABLE_NOT_FOUND = "Reservable %d not found";

    @Autowired
    private ReservableRepository reservableRepository;
    @Autowired
    private ReservableDao reservableDao;

    /**
     * List all reservables.
     *
     * @return a list of reservables {@link Reservable}.
     */
    public List<Reservable> listReservables(Map<String, String> params) {
        return reservableDao.listReservables(params, Reservable.class);
    }

    /**
     * Get an reservable.
     *
     * @return reservable by id {@link Reservable}.
     */
    public Reservable readReservable(Long id) {
        return reservableRepository.findById(id)
            .orElseThrow(() -> new ReservableNotFoundException(id));
    }

    /**
     * Create an reservable.
     *
     * @return the created reservable {@link Reservable}.
     */
    public Reservable createReservable(Reservable reservable) throws IllegalArgumentException {
        return reservableRepository.save(reservable);
    }

    /**
     * Update an reservable.
     *
     * @return the updated reservable {@link Reservable}.
     */
    public Reservable updateReservable(Long id, Reservable newReservable)
        throws NotFoundException {
        return reservableRepository.findById(id)
            .map(reservable -> {
                newReservable.setId(id);
                reservable = newReservable;

                return reservableRepository.save(reservable);
            })
            .orElseThrow(() -> new NotFoundException(String.format(
                EXCEPTION_RESERVABLE_NOT_FOUND,
                id
            )));
    }

    /**
     * Delete an reservable {@link Reservable}.
     */
    public void deleteReservable(Long id) {
        reservableRepository.deleteById(id);
    }
}
