package nl.tudelft.oopp.group39.reservable.services;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.reservable.dao.ReservableDao;
import nl.tudelft.oopp.group39.reservable.entities.Bike;
import nl.tudelft.oopp.group39.reservable.repositories.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BikeService {

    @Autowired
    private BikeRepository bikeRepository;
    @Autowired
    private ReservableDao<Bike> reservableDao;

    /**
     * List all bikes.
     *
     * @return a list of bikes {@link Bike}.
     */
    public List<Bike> listBikes(Map<String, String> params) {
        return reservableDao.listReservables(params, Bike.class);
    }

    /**
     * Get an bike.
     *
     * @return bike by id {@link Bike}.
     */
    public Bike readBike(Long id) {
        return bikeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Bike.MAPPED_NAME, id));
    }

    /**
     * Create an bike.
     *
     * @return the created bike {@link Bike}.
     */
    public Bike createBike(Bike bike) throws IllegalArgumentException {
        return bikeRepository.save(bike);
    }

    /**
     * Update an bike.
     *
     * @return the updated bike {@link Bike}.
     */
    public Bike updateBike(Long id, Bike newBike) {
        return bikeRepository.findById(id)
            .map(bike -> {
                newBike.setId(id);
                bike = newBike;

                return bikeRepository.save(bike);
            })
            .orElseThrow(() -> new NotFoundException(Bike.MAPPED_NAME, id));
    }

    /**
     * Delete an bike {@link Bike}.
     */
    public void deleteBike(Long id) {
        bikeRepository.deleteById(id);
    }
}
