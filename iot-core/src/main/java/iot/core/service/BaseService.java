package iot.core.service;

import iot.core.entity.BaseEntity;
import iot.core.repository.Filter;
import iot.core.repository.CriteriaFilter;
import iot.core.repository.BaseRepository;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Rafael Guterres
 * @param <T>
 * @param <I>
 */
public abstract class BaseService<T extends BaseEntity, I extends Serializable> {

    protected abstract BaseRepository<T, I> getRepository();

    public T load(I id) {
        return getRepository().load(id);
    }

    public T save(T entity) {
        if (entity != null && ((BaseEntity) entity).getId() != null) {
            getRepository().persist(entity);
        } else {
            return getRepository().merge(entity);
        }
        return entity;
    }

    public void delete(I id) {
        getRepository().remove(id);
    }

    public Collection<T> find() {
        return getRepository().find();
    }

    public Long count() {
        return getRepository().count();
    }

    public Collection<T> find(Integer offset, Integer limit) {
        return getRepository().find(offset, limit);
    }

    public Long count(Integer offset, Integer limit) {
        return getRepository().count(offset, limit);
    }

    public Collection<T> find(Filter filter) {
        return getRepository().find(filter);
    }

    public Long count(Filter filter) {
        return getRepository().count(filter);
    }

    public Collection<T> find(Filter filter, Integer offset, Integer limit) {
        return getRepository().find(filter, offset, limit);
    }

    public Long count(Filter filter, Integer offset, Integer limit) {
        return getRepository().count(filter, offset, limit);
    }

    public Collection<T> find(Collection<CriteriaFilter> filters, Collection<CriteriaFilter> orders, Integer offset, Integer limit) {
        return getRepository().find(filters, orders, offset, limit);
    }

    public Long count(Collection<CriteriaFilter> filters, Collection<CriteriaFilter> orders, Integer offset, Integer limit) {
        return getRepository().count(filters, orders, offset, limit);
    }
}
