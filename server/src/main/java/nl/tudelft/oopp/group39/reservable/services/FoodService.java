package nl.tudelft.oopp.group39.reservable.services;

import java.util.List;
import java.util.Map;
import javassist.NotFoundException;
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
    private ReservableDao reservableDao;

    /**
     * List all foods.
     *
     * @return a list of foods {@link Food}.
     */
    public List<Food> listFoods(Map<String, String> params) {

        return params.isEmpty()
            ? foodRepository.findAll()
            : reservableDao.listReservables(params, Food.class);
    }

    /**
     * Get an food.
     *
     * @return food by id {@link Food}.
     */
    public Food readFood(Integer id) throws NotFoundException {
        return foodRepository.findById(id).orElseThrow(()
            -> new NotFoundException(String.format(EXCEPTION_FOOD_NOT_FOUND, id)));
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
    public Food updateFood(Integer id, Food newFood) throws NotFoundException {
        return foodRepository.findById(id)
            .map(food -> {
                newFood.setId(id);
                food = newFood;

                return foodRepository.save(food);
            })
            .orElseThrow(() -> new NotFoundException(String.format(EXCEPTION_FOOD_NOT_FOUND, id)));
    }

    /**
     * Delete an food {@link Food}.
     */
    public void deleteFood(Integer id) {
        foodRepository.deleteById(id);
    }
}
