package nl.tudelft.oopp.group39.config;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = AbstractEntity.COL_ID
)
public abstract class AbstractEntity<E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>>
    implements Serializable {

    public static final String COL_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract D toDto();
}
