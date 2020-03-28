package nl.tudelft.oopp.group39.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.config.abstracts.IEntity;

public interface Utils {
    static <T> Set<T> initSet(Set<T> set) {
        return set != null ? set : new HashSet<>();
    }

    /**
     * List of id to components set.
     *
     * @param list  the list of id
     * @param clazz the class to convert to
     * @param <E>   the Entity
     * @param <D>   the Dto
     * @return a set of the component type
     */
    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> Set<E>
    idsToComponentSet(List<Long> list, Class<E> clazz) {
        Set<E> result = new HashSet<>();
        if (list != null) {
            list.forEach(id -> {
                try {
                    result.add(idToEntity(id, clazz));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return result;
    }

    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> Set<E>
    idsToComponentSet(Set<Long> set, Class<E> clazz) {
        return idsToComponentSet(new ArrayList<>(set), clazz);
    }

    /**
     * Converts a list of entities to a list of dto.
     *
     * @param list the list of entities
     * @param <E>  the entity
     * @param <D>  the dto
     * @return the list of dto
     */
    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> List<D> listEntityToDto(
        List<E> list
    ) {
        List<D> result = new ArrayList<>();
        if (list != null) {
            list.forEach(entity -> result.add(entity.toDto()));
        }
        return result;
    }

    /**
     * Converts a list of dto to a list of entities.
     *
     * @param list the list of dto
     * @param <E>  the entity
     * @param <D>  the dto
     * @return the list of entities
     */
    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> List<E> listDtoToEntity(
        List<D> list
    ) {
        List<E> result = new ArrayList<>();
        if (list != null) {
            list.forEach(entity -> result.add(entity.toEntity()));
        }
        return result;
    }

    /**
     * Converts a set of entities to a set of dto.
     *
     * @param set the set of entities
     * @param <E> the entity
     * @param <D> the dto
     * @return the set of dto
     */
    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> Set<D> setEntityToDto(
        Set<E> set
    ) {
        Set<D> result = new HashSet<>();
        if (set != null) {
            set.forEach(entity -> result.add(entity.toDto()));
        }
        return result;
    }

    /**
     * Converts a set of dto to a set of entities.
     *
     * @param set the set of dto
     * @param <E> the entity
     * @param <D> the dto
     * @return the set of entities
     */
    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> Set<E> setDtoToEntity(
        Set<D> set
    ) {
        Set<E> result = new HashSet<>();
        if (set != null) {
            set.forEach(entity -> result.add(entity.toEntity()));
        }
        return result;
    }

    /**
     * Creates an entity from id.
     *
     * @param id    the id
     * @param clazz the class
     * @param <E>   the class
     * @return the entity
     */
    static <E extends IEntity> E idToEntity(Long id, Class<E> clazz) {
        E result = null;

        try {
            result = clazz.getDeclaredConstructor().newInstance();
            result.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
