package nl.tudelft.oopp.group39.config.abstracts;

import java.io.Serializable;

public abstract class AbstractDto<E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>>
    implements Serializable, IEntity {

    protected Long id;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Changes the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Changes a Dto object to an entity.
     *
     * @return the entity object
     */
    public abstract E toEntity();
}
