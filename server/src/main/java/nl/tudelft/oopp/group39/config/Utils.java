package nl.tudelft.oopp.group39.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import nl.tudelft.oopp.group39.config.abstracts.AbstractDto;
import nl.tudelft.oopp.group39.config.abstracts.AbstractEntity;
import nl.tudelft.oopp.group39.config.abstracts.IEntity;

public interface Utils {
    static LocalDateTime parseDateTime(String string) {
        return LocalDateTime.parse(string, Constants.FORMATTER_DATE_TIME);
    }

    static LocalDate parseDate(String string) {
        return LocalDate.parse(string, Constants.FORMATTER_DATE);
    }

    static LocalTime parseTime(String string) {
        return LocalTime.parse(string, Constants.FORMATTER_TIME);
    }

    /**
     * A wrapper for handling null pointer exceptions.
     *
     * @param fn the function
     * @return the result or null
     */
    static <T> T safeNull(Function<Object, T> fn) {
        try {
            return fn.apply(null);
        } catch (NullPointerException e) {
            return null;
        }
    }

    static <T> Set<T> initSet(Set<T> set) {
        return set != null ? set : new HashSet<>();
    }

    static <T> List<T> initList(List<T> list) {
        return list != null ? list : new ArrayList<>();
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
    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> Set<E> idsToComponentSet(
        List<Long> list,
        Class<E> clazz
    ) {
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

    static <E extends AbstractEntity<E, D>, D extends AbstractDto<E, D>> Set<E> idsToComponentSet(
        Set<Long> set,
        Class<E> clazz
    ) {
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

        if (id != null) {
            try {
                result = clazz.getDeclaredConstructor().newInstance();
                result.setId(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Creates an entity from id.
     *
     * @param entity the the entity
     * @param <E>    the class
     * @return the id
     */
    static <E extends IEntity> Long entityToId(E entity) {
        return entity != null ? entity.getId() : null;
    }

    /**
     * Takes a set of entities and gives back a list of their ids.
     *
     * @param set the set
     * @param <E> the entity type
     * @return the list of ids
     */
    static <E extends IEntity> List<Long> entitiesToIds(Set<E> set) {
        List<Long> list = new ArrayList<>();

        set.forEach(entity -> list.add(entity.getId()));

        return list;
    }
}
