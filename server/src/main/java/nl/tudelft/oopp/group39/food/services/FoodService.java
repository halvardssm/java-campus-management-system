package nl.tudelft.oopp.group39.food.services;

import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.oopp.group39.food.entities.Food;
import nl.tudelft.oopp.group39.food.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodService {
    public static final String EXCEPTION_FOOD_NOT_FOUND = "Food %d not found";

    @Autowired
    private FoodRepository foodRepository;

    /**
     * List all foods.
     *
     * @return a list of foods {@link Food}.
     */
    public List<Food> listFoods() {
        return foodRepository.findAll();
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
