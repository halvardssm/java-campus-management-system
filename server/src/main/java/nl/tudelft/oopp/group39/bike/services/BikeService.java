package nl.tudelft.oopp.group39.bike.services;

import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.bike.entities.Bike;
import nl.tudelft.oopp.group39.bike.repositories.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BikeService {
    public static final String EXCEPTION_BIKE_NOT_FOUND = "Bike %d not found";

    @Autowired
    private BikeRepository bikeRepository;

    /**
     * List all bikes.
     *
     * @return a list of bikes {@link Bike}.
     */
    public List<Bike> listBikes() {
        return bikeRepository.findAll();
    }

    /**
     * Get an bike.
     *
     * @return bike by id {@link Bike}.
     */
    public Bike readBike(Integer id) throws NotFoundException {
        return bikeRepository.findById(id).orElseThrow(()
            -> new NotFoundException(String.format(EXCEPTION_BIKE_NOT_FOUND, id)));
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
    public Bike updateBike(Integer id, Bike newBike) throws NotFoundException {
        return bikeRepository.findById(id)
            .map(bike -> {
                newBike.setId(id);
                bike = newBike;

                return bikeRepository.save(bike);
            })
            .orElseThrow(() -> new NotFoundException(String.format(EXCEPTION_BIKE_NOT_FOUND, id)));
    }

    /**
     * Delete an bike {@link Bike}.
     */
    public void deleteBike(Integer id) {
        bikeRepository.deleteById(id);
    }
}
