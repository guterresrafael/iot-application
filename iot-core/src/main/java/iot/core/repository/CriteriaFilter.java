package iot.core.repository;

import iot.core.enumeration.Method;
import iot.core.enumeration.OrderBy;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

/**
 *
 * @author Rafael Guterres
 */
public class CriteriaFilter implements Serializable {

    private static final long serialVersionUID = 1473768917303205143L;

    private static final String DATE_SEPARATOR_CHARACTER = "-";

    private String name;
    private Object value;
    private Class<?> clazz;
    private Method method;
    private OrderBy orderBy;
    private CriteriaFilter filter;

    public CriteriaFilter() {
    }

    public CriteriaFilter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public CriteriaFilter(String name, Object value, Method method) {
        this.name = name;
        this.value = value;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public CriteriaFilter getFilter() {
        return filter;
    }

    public void setFilter(CriteriaFilter field) {
        this.filter = field;
    }

    public Comparable getValueComparable() {
        if (this.clazz != null && this.value instanceof Comparable) {
            return (Comparable) this.value;
        }
        return getValueComparableFromParse();
    }

    private Comparable getValueComparableFromParse() {
        try {
            String[] dateArray = ((String) this.value).split(DATE_SEPARATOR_CHARACTER);
            LocalDate localDate = LocalDate.of(Integer.parseInt(dateArray[0]),
                                               Integer.parseInt(dateArray[1]),
                                               Integer.parseInt(dateArray[2]));
            return (Comparable) Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
        }
        try {
            return (Comparable) Long.parseLong((String) this.value);
        } catch (Exception e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
        }
        return (Comparable) this.value;
    }
}
