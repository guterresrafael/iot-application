package iot.core.repository;

import iot.core.entity.BaseEntity;
import iot.core.helper.Reflection;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/**
 *
 * @author Rafael Guterres
 * @param <T>
 * @param <I>
 */
public abstract class BaseRepository<T extends BaseEntity, I extends Serializable> {

    private final Class<T> clazz = Reflection.getGenericArgumentType(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public T load(I id) {
        T entity = getEntityManager().find(clazz, id);
        return entity;
    }

    @Transactional
    public void persist(T entity) {
        getEntityManager().persist(entity);
    }

    @Transactional
    public T merge(T entity) {
        return (T) getEntityManager().merge(entity);
    }

    @Transactional
    public void remove(I id) {
        T entity = load(id);
        if (entity != null) {
            getEntityManager().remove(entity);
        }
    }

    public Collection<T> find() {
        return find(null, null, null);
    }

    public Long count() {
        return count(null, null, null);
    }

    public Collection<T> find(Integer offset, Integer limit) {
        return find(null, offset, limit);
    }

    public Long count(Integer offset, Integer limit) {
        return count(null, offset, limit);
    }

    public Collection<T> find(Filter filter) {
        return find(filter, null, null);
    }

    public Long count(Filter filter) {
        return count(filter, null, null);
    }

    public Collection<T> find(Filter filter, Integer offset, Integer limit) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        Map<String, Object> parameters = new HashMap<>();
        if (filter != null) {
            CriteriaHelper.addWhere(criteriaBuilder, criteriaQuery, root, filter, parameters);
        }
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
        if (!parameters.isEmpty()) {
            addQueryParameters(query, parameters);
        }
        if (offset != null && limit != null) {
            return getResultList(query, offset, limit);
        } else {
            return getResultList(query);
        }
    }

    public Long count(Filter filter, Integer offset, Integer limit) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(criteriaBuilder.count(root));
        Map<String, Object> parameters = new HashMap<>();
        if (filter != null) {
            CriteriaHelper.addWhere(criteriaBuilder, criteriaQuery, root, filter, parameters);
        }
        TypedQuery<Long> query = getEntityManager().createQuery(criteriaQuery);
        if (offset != null && limit != null) {
            return getCount(query, offset, limit);
        } else {
            return getCount(query);
        }
    }

    public Collection<T> find(Collection<CriteriaFilter> filters, Collection<CriteriaFilter> orders, Integer offset, Integer limit) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        Map<String, Object> parameters = new HashMap<>();
        if (filters != null && !filters.isEmpty()) {
            CriteriaHelper.addWhere(criteriaBuilder, criteriaQuery, root, filters, parameters);
        }
        if (orders != null && !orders.isEmpty()) {
            CriteriaHelper.addOrderBy(criteriaBuilder, criteriaQuery, root, orders);
        }
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
        return getResultList(query, offset, limit);
    }

    public Long count(Collection<CriteriaFilter> filters, Collection<CriteriaFilter> orders, Integer offset, Integer limit) {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(criteriaBuilder.count(root));
        Map<String, Object> parameters = new HashMap<>();
        if (filters != null && !filters.isEmpty()) {
            CriteriaHelper.addWhere(criteriaBuilder, criteriaQuery, root, filters, parameters);
        }
        TypedQuery<Long> query = getEntityManager().createQuery(criteriaQuery);
        return getCount(query, offset, limit);
    }

    private void addQueryParameters(TypedQuery query, Map<String, Object> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                //query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    private Collection<T> getResultList(Query query) {
        Collection<T> entities = query.getResultList();
        return entities;
    }

    private Collection<T> getResultList(Query query, Integer offset, Integer limit) {
        Collection<T> entities = query.setFirstResult(offset)
                                .setMaxResults(limit)
                                .getResultList();
        return entities;
    }

    private Long getCount(TypedQuery<Long> query) {
        Long count = query.getSingleResult();
        return count;
    }

    private Long getCount(TypedQuery<Long> query, Integer offset, Integer limit) {
        Long count = query.setFirstResult(offset)
                          .setMaxResults(limit)
                          .getSingleResult();
        return count;
    }

    public Path getPath(Root root, String strPath) {
        Path path = root;
        String[] fields = strPath.split("\\.");
        for (String field : fields) {
            path = path.get(field);
        }
        return path;
    }
}
