package nl.tudelft.oopp.group39.config.abstracts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractDao<E> {
    protected EntityManager entityManager;
    protected CriteriaBuilder builder;
    protected CriteriaQuery<E> query;
    protected Root<E> root;
    protected List<Predicate> predicates = new ArrayList<>();
    protected Map<String, String> params;

    /**
     * The initializer.
     *
     * @param em        the em
     * @param clazz     the clazz
     * @param newParams the new parameters
     */
    protected void init(EntityManager em, Class<E> clazz, Map<String, String> newParams) {
        predicates.clear();
        params = newParams;

        entityManager = em;
        builder = entityManager.getCriteriaBuilder();
        query = builder.createQuery(clazz);
        root = query.from(clazz);
        query.select(root);
    }

    /**
     * Gets the result from a query.
     *
     * @return result from a query
     */
    protected List<E> result() {
        query.where(builder.and(predicates.toArray(new Predicate[0])));
        TypedQuery<E> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    /**
     * Takes a String containing an id list.
     *
     * @param stringIds in the format of `1,3,4`
     * @return the list equivalent of the string
     */
    protected List<Long> stringLongToList(String stringIds) {
        return new ArrayList<>((
            Arrays.stream(stringIds.split(","))
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList())
        ));
    }

    /**
     * Puts numbers in a list.
     *
     * @param col the numbers
     * @param list the list where you want to put them in
     */
    protected void predicateLongInList(String col, String list) {
        predicates.add(root.get(col).in(stringLongToList(list)));
    }

    /**
     * Checks whether two strings are alike.
     *
     * @param col         the first string
     * @param likeString  the second string
     */
    protected void predicateLike(String col, String likeString) {
        predicates.add(builder.like(root.get(col), "%" + likeString + "%"));
    }

    /**
     * Checks whether two predicates are equal.
     *
     * @param col the first predicate
     * @param obj the second
     */
    protected void predicateEqual(String col, Object obj) {
        predicates.add(builder.equal(
            root.get(col),
            obj
        ));
    }

    /**
     * Checks whether three predicates are equal.
     *
     * @param col        the first predicate
     * @param colForeign the second predicate
     * @param obj        the third object
     */
    protected void predicateEqualForeign(String col, String colForeign, Object obj) {
        predicates.add(builder.equal(
            root.get(col).get(colForeign),
            obj
        ));
    }

    /**
     * Gets the result from the predicate.
     *
     * @param search   the thing that you want to search
     * @param table    the table where you want to search
     * @param subClazz the subclazz
     * @param <C>      the returned type
     */
    protected <C> void predicateInRelationManyOne(
        String search,
        String table,
        Class<C> subClazz
    ) {
        List<Long> idList = stringLongToList(search);

        CriteriaQuery<C> subBuilder = builder.createQuery(subClazz);
        Root<C> subRoot = subBuilder.from(subClazz);

        subBuilder
            .select(subRoot.get(table))
            .where(subRoot.get(AbstractEntity.COL_ID).in(idList));

        predicates.add(subRoot.in(entityManager.createQuery(subBuilder).getResultList()));
    }

    /**
     * Gets the result from the predicate.
     *
     * @param search   the thing that you want to search
     * @param table    the table where you want to search
     * @param col      the column
     * @param subClazz the subclazz
     * @param <C>      the returned type
     */
    protected <C> void predicateInRelationManyMany(
        String search,
        String table,
        String col,
        Class<C> subClazz
    ) {
        List<Long> idList = stringLongToList(search);

        CriteriaQuery<C> subBuilder = builder.createQuery(subClazz);
        Root<C> subRoot = subBuilder.from(subClazz);

        subBuilder
            .select(subRoot.get(table))
            .where(subRoot.get(table).get(col).in(idList));

        predicates.add(subRoot.in(entityManager.createQuery(subBuilder).getResultList()));
    }

    /**
     * Checks whether something is greater than something else.
     *
     * @param col the column
     * @param obj the object
     * @param <Y> the returned type
     */
    protected <Y extends Comparable<? super Y>> void predicateGreater(
        String col,
        Y obj
    ) {
        predicates.add(builder.greaterThanOrEqualTo(root.get(col), obj));
    }

    /**
     * Checks whether something is smaller than something else.
     *
     * @param col the column
     * @param obj the object
     * @param <Y> the returned type
     */
    protected <Y extends Comparable<? super Y>> void predicateSmaller(
        String col,
        Y obj
    ) {
        predicates.add(builder.lessThanOrEqualTo(root.get(col), obj));
    }

    /**
     * Checks whether the dates are equal.
     *
     * @param date     the date
     * @param colStart the starting column
     * @param colEnd   the ending column
     * @param <Y>      the returned type
     */
    protected <Y extends Comparable<? super Y>> void predicateDateEqual(
        String date,
        String colStart,
        String colEnd
    ) {
        LocalDate localDate = LocalDate.parse(date);

        predicates.add(builder.lessThanOrEqualTo(
            root.get(colStart).as(LocalDate.class),
            localDate
        ));

        predicates.add(builder.greaterThanOrEqualTo(
            root.get(colEnd).as(LocalDate.class),
            localDate
        ));
    }

    /**
     * Checks parameters.
     *
     * @param col      the column
     * @param function the function
     */
    protected void checkParam(String col, BiConsumer<String, String> function) {
        if (params.containsKey(col)) {
            function.accept(col, params.get(col));
        }
    }
}
