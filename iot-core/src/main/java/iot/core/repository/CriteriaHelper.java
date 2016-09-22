package iot.core.repository;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Rafael Guterres
 */
public final class CriteriaHelper implements Serializable {

    private static final long serialVersionUID = -4367708806996213062L;

    private static final String LIKE_PARAM_VALUE = "*";
    private static final String LIKE_CRITERIA_VALUE = "%";

    private CriteriaHelper() {
    }

    public static void addWhere(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
                                Root root, Filter filter, Map<String, Object> parameters) {
        Collection<Predicate> predicates = new ArrayList<>();
        Method[] methods = filter.getClass().getMethods();
        for (final Method method : methods) {
            if (method.getName().startsWith("get")
                    && method.getParameterTypes().length == 0
                    && !method.getName().equals("getClass")) {
                CriteriaFilter criteriaFilter = getCriteriaFilter(filter, method);
                if (criteriaFilter != null) {
                    parameters.put(criteriaFilter.getName(), criteriaFilter.getValue());
                    CriteriaHelper.addPredicate(predicates, criteriaBuilder, root, criteriaFilter);
                }
            }
        }
        addPredicates(criteriaBuilder, criteriaQuery, predicates);
    }

    public static void addWhere(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery,
                                Root<?> root, Collection<CriteriaFilter> filters,
                                Map<String, Object> parameters) {
        Collection<Predicate> predicates = new ArrayList<>();
        for (CriteriaFilter filter : filters) {
            parameters.put(filter.getName(), filter.getValue());
            CriteriaHelper.addPredicate(predicates, criteriaBuilder, root, filter);
        }
        addPredicates(criteriaBuilder, criteriaQuery, predicates);
    }

    private static void addPredicate(Collection<Predicate> predicates, CriteriaBuilder criteriaBuilder,
                                     Root root, CriteriaFilter filter) {
        addPredicateWithValueBased(predicates, criteriaBuilder, root, filter);
        addPredicateWithoutValueBased(predicates, criteriaBuilder, root, filter);
        addPredicateWithComparableBased(predicates, criteriaBuilder, root, filter);
    }

    private static void addPredicateWithValueBased(Collection<Predicate> predicates, CriteriaBuilder criteriaBuilder,
                                                   Root root, CriteriaFilter filter) {
        switch (filter.getMethod()) {
            case EQUAL:
                predicates.add(criteriaBuilder.equal(root.get(filter.getName()), filter.getValue()));
                break;
            case NOT_EQUAL:
                predicates.add(criteriaBuilder.notEqual(root.get(filter.getName()), filter.getValue()));
                break;
            case LIKE:
                predicates.add(criteriaBuilder.like(root.get(filter.getName()), addLikeChar(filter.getValue())));
                break;
            case NOT_LIKE:
                predicates.add(criteriaBuilder.notLike(root.get(filter.getName()), addLikeChar(filter.getValue())));
                break;
            default:
                break;
        }
    }

    private static void addPredicateWithoutValueBased(Collection<Predicate> predicates, CriteriaBuilder criteriaBuilder,
                                                      Root root, CriteriaFilter filter) {
        switch (filter.getMethod()) {
            case IS_NULL:
                predicates.add(criteriaBuilder.isNull(root.get(filter.getName())));
                break;
            case IS_NOT_NULL:
                predicates.add(criteriaBuilder.isNotNull(root.get(filter.getName())));
                break;
            case IS_FALSE:
                predicates.add(criteriaBuilder.isFalse(root.get(filter.getName())));
                break;
            case IS_TRUE:
                predicates.add(criteriaBuilder.isTrue(root.get(filter.getName())));
                break;
            case IS_EMPTY:
                predicates.add(criteriaBuilder.isEmpty(root.get(filter.getName())));
                break;
            default:
                break;
        }
    }

    private static void addPredicateWithComparableBased(Collection<Predicate> predicates, CriteriaBuilder criteriaBuilder,
                                                        Root root, CriteriaFilter filter) {
        switch (filter.getMethod()) {
            case GREATER:
                predicates.add(criteriaBuilder.greaterThan(root.get(filter.getName()), filter.getValueComparable()));
                break;
            case GREATER_OR_EQUAL:
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getName()), filter.getValueComparable()));
                break;
            case LESS:
                predicates.add(criteriaBuilder.lessThan(root.get(filter.getName()), filter.getValueComparable()));
                break;
            case LESS_OR_EQUAL:
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(filter.getName()), filter.getValueComparable()));
                break;
            case BETWEEN:
                predicates.add(criteriaBuilder.between(root.get(filter.getName()), filter.getValueComparable(), filter.getFilter().getValueComparable()));
                break;
            default:
                break;
        }
    }

    public static void addOrderBy(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery,
                                  Root root, Collection<CriteriaFilter> orders) {
        //TODO: implementar suporte a ordenacao
    }

    private static void addPredicates(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, Collection<Predicate> predicates) {
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
        }
    }

    private static String addLikeChar(Object value) {
        return value.toString().replace(LIKE_PARAM_VALUE, LIKE_CRITERIA_VALUE);
    }

    private static CriteriaFilter getCriteriaFilter(Filter filter, Method method) {
        try {
            Object filterValue = method.invoke(filter);
            if (filterValue != null) {
                CriteriaFilter criteriaField = new CriteriaFilter();
                criteriaField.setName(Introspector.decapitalize(method.getName().substring(3)));
                criteriaField.setValue(method.invoke(filter));
                criteriaField.setClazz(method.getReturnType());
                if (method.isAnnotationPresent(CriteriaMethod.class)) {
                    criteriaField.setMethod(method.getAnnotation(CriteriaMethod.class).method());
                } else {
                    criteriaField.setMethod(iot.core.enumeration.Method.EQUAL);
                }
                return criteriaField;
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            return null;
        }
        return null;
    }

}
