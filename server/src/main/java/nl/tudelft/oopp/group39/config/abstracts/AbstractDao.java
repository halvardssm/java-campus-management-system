package nl.tudelft.oopp.group39.config.abstracts;

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

    protected void init(EntityManager em, Class<E> clazz, Map<String, String> newParams) {
        predicates.clear();
        params = newParams;

        entityManager = em;
        builder = entityManager.getCriteriaBuilder();
        query = builder.createQuery(clazz);
        root = query.from(clazz);
        query.select(root);
    }

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

    protected void predicateLongInList(String col, String list) {
        predicates.add(root.get(col).in(stringLongToList(list)));
    }

    protected void predicateLike(String col, String likeString) {
        predicates.add(builder.like(root.get(col), "%" + likeString + "%"));
    }

    protected void predicateEqual(String col, Object obj) {
        predicates.add(builder.equal(
            root.get(col),
            obj
        ));
    }

    protected <C> void predicateInRelation(
        String table,
        Class<C> subClazz,
        String search
    ) {
        List<Long> idList = stringLongToList(search);

        CriteriaQuery<C> subBuilder = builder.createQuery(subClazz);
        Root<C> subRoot = subBuilder.from(subClazz);

        subBuilder
            .select(subRoot.get(table))
            .where(subRoot.get(AbstractEntity.COL_ID).in(idList));

        predicates.add(subRoot.in(entityManager.createQuery(subBuilder).getResultList()));
    }

    protected <Y extends Comparable<? super Y>> void predicateGreater(
        String col,
        Y obj
    ) {
        predicates.add(builder.greaterThanOrEqualTo(root.get(col), obj));
    }

    protected <Y extends Comparable<? super Y>> void predicateSmaller(
        String col,
        Y obj
    ) {
        predicates.add(builder.lessThanOrEqualTo(root.get(col), obj));
    }

    protected void checkParam(String col, BiConsumer<String, String> function) {
        if (params.containsKey(col)) {
            function.accept(col, params.get(col));
        }
    }
}
