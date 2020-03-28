package nl.tudelft.oopp.group39.config;

import java.io.Serializable;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractDto<E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>>
    implements Serializable {
    public abstract E toEntity();
}
