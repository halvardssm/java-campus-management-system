package nl.tudelft.oopp.group39.reservable.services;

import java.util.List;
import java.util.Map;
import nl.tudelft.oopp.group39.config.exceptions.NotFoundException;
import nl.tudelft.oopp.group39.reservable.dao.ReservableDao;
import nl.tudelft.oopp.group39.reservable.entities.Food;
import nl.tudelft.oopp.group39.reservable.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodService {
    public static final String EXCEPTION_FOOD_NOT_FOUND = "Food %d not found";

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private ReservableDao<Food> reservableDao;

    /**
     * List all foods.
     *
     * @return a list of foods {@link Food}.
     */
    public List<Food> listFoods(Map<String, String> params) {
        return reservableDao.listReservables(params, Food.class);
    }

    /**
     * Get an food.
     *
     * @return food by id {@link Food}.
     */
    public Food readFood(Long id) {
        return foodRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(Food.MAPPED_NAME, id));
    }

    /**
     * Create an food.
     *
     * @return the created food {@link Food}.
     */
    public Food createFood(Food food) throws IllegalArgumentException {
        return foodRepository.save(food);
    }

    /**
     * Update an food.
     *
     * @return the updated food {@link Food}.
     */
    public Food updateFood(Long id, Food newFood) {
        return foodRepository.findById(id)
            .map(food -> {
                newFood.setId(id);
                food = newFood;

                return foodRepository.save(food);
            })
            .orElseThrow(() -> new NotFoundException(Food.MAPPED_NAME, id));
    }

    /**
     * Delete an food {@link Food}.
     */
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }
}
