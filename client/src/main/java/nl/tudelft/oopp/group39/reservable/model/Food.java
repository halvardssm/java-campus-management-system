package nl.tudelft.oopp.group39.reservable.model;


public class Food extends Reservable {
    private String name;
    private String description;

    /**
     * Creates food.
     */
    public Food() {
        super();
    }

    /**
     * Creates a food entity.
     *
     * @param id id of the food
     * @param name name of the food
     * @param desc description of the food
     * @param price price of the food
     * @param building the building this food is available in
     */
    public Food(Long id, String name, String desc, double price, Long building) {
        super(id,price,building);
        this.name = name;
        this.description = desc;
    }

    /**
     * Gets the name of the food.
     *
     * @return the name of the food
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the food.
     *
     * @return the description of the food
     */
    public String getDescription() {
        return description;
    }
}
