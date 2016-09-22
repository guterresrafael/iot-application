package iot.core.resource;

import iot.core.entity.BaseEntity;
import iot.core.helper.ResponseBuilder;
import iot.core.helper.Reflection;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import iot.core.role.ApplicationRole;
import iot.core.service.BaseService;

/**
 *
 * @author Rafael Guterres
 * @param <T>
 * @param <I>
 */
public abstract class BaseResource<T extends BaseEntity, I extends Serializable> {

    private static final Integer PARAM_OFFSET_DEFAULT_VALUE = 0;
    private static final Integer PARAM_LIMIT_DEFAULT_VALUE = 20;

    private final Class<T> clazz = Reflection.getGenericArgumentType(getClass());

    @Context
    private SecurityContext securityContext;

    private KeycloakSecurityContext keycloakSecurityContext;

    protected abstract BaseService<T, I> getService();

    protected Integer getOffsetDefaultValue() {
        return PARAM_OFFSET_DEFAULT_VALUE;
    }

    protected Integer getLimitDefaultValue() {
        return PARAM_LIMIT_DEFAULT_VALUE;
    }

    public Collection<T> getEntities(HttpServletRequest request) {
        try {
            Collection<T> entities;
            QueryString queryString = new QueryString(request);

            Integer offset = null;
            if (queryString.getOffset() != null) {
                queryString.getOffset();
            } else {
                getOffsetDefaultValue();
            }

            Integer limit = null;
            if (queryString.getLimit() != null) {
                queryString.getLimit();
            } else {
                getLimitDefaultValue();
            }

            if (!queryString.getFilters().isEmpty()) {
                entities = getService().find(queryString.getFilters(),
                                             queryString.getOrders(),
                                             offset, limit);
                if (entities.isEmpty()) {
                    throw new WebApplicationException(ResponseBuilder.notFound());
                }
                return getEntitiesFromQueryStringCustomFilters(entities, queryString);
            } else {
                entities = getService().find(offset, limit);
                if (entities.isEmpty()) {
                    throw new WebApplicationException(ResponseBuilder.notFound());
                }
                return entities;
            }
        } catch (IllegalArgumentException e) {
            throw new WebApplicationException(ResponseBuilder.badRequest(e));
        }
    }

    public T getEntityById(I id) {
        T entity = getService().load(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity;
    }

    public Response postEntity(T entity) {
        try {
            return ResponseBuilder.ok(getService().save(entity));
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
    }

    public Response putEntity(I id, T entity) {
        try {
            return ResponseBuilder.ok(getService().save(entity));
        } catch (Exception e) {
            return ResponseBuilder.conflict(e);
        }
    }

    public Response deleteEntity(I id) {
        try {
            getService().delete(id);
        } catch (Exception e) {
            return ResponseBuilder.conflict(e);
        }
        return ResponseBuilder.deleted();
    }

    private Collection<T> getEntitiesFromQueryStringCustomFilters(Collection<T> entities, QueryString queryString) {
        try {
            Collection<Map<String, Object>> entitiesMap = getEntitiesMapList(entities, queryString);
            Collection<T> entitiesCustomFields = getEntitiesCustomFields(entitiesMap);
            if (!entitiesCustomFields.isEmpty()) {
                return entitiesCustomFields;
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
        }
        return null;
    }

    private Collection<Map<String, Object>> getEntitiesMapList(Collection<T> entities, QueryString queryString) throws IllegalArgumentException, IllegalAccessException {
        Collection<Map<String, Object>> entitiesMap = new ArrayList<>();
        for (T entity : entities) {
            Map<String, Object> entityMap = new HashMap<>();
            Collection<Field> entityFields = new ArrayList<>();
            Reflection.getAllFields(entityFields, entity.getClass());
            for (String fieldParam : queryString.getFieldList()) {
                for (Field entityField : entityFields) {
                    entityField.setAccessible(true);
                    if (entityField.getName().equals(fieldParam)) {
                        entityMap.put(fieldParam, entityField.get(entity));
                        break;
                    }
                }
            }
            entitiesMap.add(entityMap);
        }
        return entitiesMap;
    }

    private Collection<T> getEntitiesCustomFields(Collection<Map<String, Object>> entitiesMap) throws IllegalArgumentException, IllegalAccessException {
        Collection<T> entities = new ArrayList<>();
        for (Map<String, Object> entityMap : entitiesMap) {
            T entity = Reflection.instantiateClass(clazz);
            Collection<Field> entityFields = new ArrayList<>();
            Reflection.getAllFields(entityFields, entity.getClass());
            for (Map.Entry<String, Object> entry : entityMap.entrySet()) {
                for (Field entityField : entityFields) {
                    entityField.setAccessible(true);
                    if (entityField.getName().equals(entry.getKey())) {
                        entityField.set(entity, entry.getValue());
                        break;
                    }
                }
            }
            entities.add(entity);
        }
        return entities;
    }

    private KeycloakSecurityContext getKeycloakSecurityContext() {
        if (keycloakSecurityContext == null) {
            KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) securityContext.getUserPrincipal();
            keycloakSecurityContext = keycloakPrincipal.getKeycloakSecurityContext();
        }
        return keycloakSecurityContext;
    }

    public String getUsername() {
        return getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    public boolean isUserAdmin() {
        return getKeycloakSecurityContext().getToken().getRealmAccess().isUserInRole(ApplicationRole.ADMIN);
    }

    private void validateAccess() {
        if (!isUserAdmin()) {
            throw new ForbiddenException();
        }
    }
}
