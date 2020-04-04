package nl.tudelft.oopp.group39.config.abstracts;

public class NotFoundException extends RuntimeException {
    /**
     * Creates an exception if the room to be found was not found.
     *
     * @param id the id of the room that was not found
     */
    public NotFoundException(String name, Object id) {
        super(name + " with id " + id + " wasn't found.");
    }

}
