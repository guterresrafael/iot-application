package iot.core.resource;

import iot.core.enumeration.Method;
import iot.core.enumeration.OrderBy;
import iot.core.enumeration.Param;
import iot.core.repository.CriteriaFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Rafael Guterres
 */
public class QueryString implements Serializable {

    private static final long serialVersionUID = -756050251522896230L;

    private static final String DELIMITER_PARAM_VALUE = ",";
    private static final String ORDERBY_ASC_OPERATOR = "+";
    private static final String ORDERBY_DESC_OPERATOR = "-";
    private static final String GREATER_OPERATOR = ">";
    private static final String LESS_OPERATOR = "<";
    private static final String NOT_OPERATOR = "!";
    private static final String LIKE_OPERATOR = "*";
    private static final String BETWEEN_OPERATOR = "...";
    private static final String BETWEEN_REGEX = "\\.\\.\\.";

    private Integer offset;
    private Integer limit;
    private Collection<String> fields;
    private Collection<CriteriaFilter> filters;
    private Collection<CriteriaFilter> orders;

    public QueryString() {
    }

    public QueryString(HttpServletRequest request) {
        setOffset(request.getParameter(Param.OFFSET.name().toLowerCase()));
        setLimit(request.getParameter(Param.LIMIT.name().toLowerCase()));
        setFieldList(request.getParameter(Param.FIELDS.name().toLowerCase()));
        setSortList(request.getParameter(Param.SORT.name().toLowerCase()));
        setFilterList(request);
    }

    public Integer getOffset() {
        return offset;
    }

    private void setOffset(String offset) {
        try {
            this.offset = Integer.parseInt(offset);
        } catch (NumberFormatException e) {
            this.offset = null;
        }
    }

    public Integer getLimit() {
        return limit;
    }

    private void setLimit(String limit) {
        try {
            this.limit = Integer.parseInt(limit);
        } catch (NumberFormatException e) {
            this.limit = null;
        }
    }

    public Collection<String> getFieldList() {
        return fields;
    }

    private void setFieldList(String fieldParams) {
        this.fields = new ArrayList<>();
        if (fieldParams != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(fieldParams, DELIMITER_PARAM_VALUE);
            while (stringTokenizer.hasMoreTokens()) {
                this.fields.add(stringTokenizer.nextToken());
            }
        }
    }

    public Collection<CriteriaFilter> getOrders() {
        return orders;
    }

    private void setSortList(String sortParams) {
        this.orders = new ArrayList<>();
        if (sortParams != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(sortParams, DELIMITER_PARAM_VALUE);
            while (stringTokenizer.hasMoreTokens()) {
                CriteriaFilter filter = new CriteriaFilter();
                String fieldParam = stringTokenizer.nextToken();
                String order = null;
                switch (fieldParam.substring(0, 1)) {
                    case ORDERBY_DESC_OPERATOR:
                        order = OrderBy.DESC.name();
                        fieldParam = fieldParam.substring(1);
                        break;
                    case ORDERBY_ASC_OPERATOR:
                        fieldParam = fieldParam.substring(1);
                        order = OrderBy.ASC.name();
                        break;
                    default:
                        break;
                }
                filter.setName(fieldParam);
                filter.setOrderBy(OrderBy.valueOf(order));
                this.orders.add(filter);
            }
        }
    }

    public Collection<CriteriaFilter> getFilters() {
        return filters;
    }

    private void setFilterList(HttpServletRequest request) {
        this.filters = new ArrayList<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            try {
                Param.valueOf(paramName.toUpperCase());
            } catch (IllegalArgumentException e) {
                Logger.getAnonymousLogger().warning(e.getMessage());
                String paramValue = request.getParameter(paramName);
                CriteriaFilter filter = new CriteriaFilter();
                filter.setName(paramName);
                filter.setValue(paramValue);
                this.defineFieldMethod(filter);
                this.filters.add(filter);
            }
        }
    }

    private void defineFieldMethod(CriteriaFilter filter) {
        this.defineFieldMethodWithEqualsBased(filter);
        this.defineFieldMethodWithComparableBased(filter);
        this.defineFieldMethodWithLikeBased(filter);
        this.defineFieldMethodDefault(filter);
    }

    private void defineFieldMethodWithEqualsBased(CriteriaFilter filter) {
        if (filter.getName() != null) {
            String paramName = filter.getName();
            String operatorLeft = paramName.substring(paramName.length() - 1);
            switch (operatorLeft) {
                case GREATER_OPERATOR:
                    filter.setMethod(Method.GREATER_OR_EQUAL);
                    break;
                case LESS_OPERATOR:
                    filter.setMethod(Method.LESS_OR_EQUAL);
                    break;
                case NOT_OPERATOR:
                    filter.setMethod(Method.NOT_EQUAL);
                    break;
                default:
                    break;
            }
            if (filter.getMethod() != null) {
                filter.setName(paramName.substring(0, paramName.length() - 1));
            }
        }
    }

    private void defineFieldMethodWithComparableBased(CriteriaFilter filter) {
        if (filter.getValue() == null || filter.getValue().toString().isEmpty()) {
            String[] fieldArray = null;
            if (filter.getName().contains(GREATER_OPERATOR)) {
                filter.setMethod(Method.GREATER);
                fieldArray = filter.getName().split(GREATER_OPERATOR);
            } else if (filter.getName().contains(LESS_OPERATOR)) {
                filter.setMethod(Method.LESS);
                fieldArray = filter.getName().split(LESS_OPERATOR);
            }
            if (fieldArray != null && fieldArray.length > 0) {
                filter.setName(fieldArray[0]);
                filter.setValue(fieldArray[1]);
            }
        } else if (filter.getValue().toString().contains(BETWEEN_OPERATOR)) {
            String[] fieldArray = null;
            filter.setMethod(Method.BETWEEN);
            fieldArray = filter.getValue().toString().split(BETWEEN_REGEX);
            filter.setValue(fieldArray[0]);
            filter.setFilter(new CriteriaFilter(filter.getName(), fieldArray[1]));
        }
    }

    private void defineFieldMethodWithLikeBased(CriteriaFilter filter) {
        if (filter.getValue() != null && filter.getValue().toString().contains(LIKE_OPERATOR)) {
            if (filter.getMethod() != null && filter.getMethod().equals(Method.NOT_EQUAL)) {
                filter.setMethod(Method.NOT_LIKE);
            } else {
                filter.setMethod(Method.LIKE);
            }
        }
    }

    private void defineFieldMethodDefault(CriteriaFilter filter) {
        if (filter.getMethod() == null) {
            filter.setMethod(Method.EQUAL);
        }
    }
}
