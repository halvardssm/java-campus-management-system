package nl.tudelft.oopp.group39.config.abstracts;

import java.io.Serializable;

public abstract class AbstractDto<E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>>
    implements Serializable, IEntity {

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract E toEntity();
}
