package iot.service.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Rafael Guterres
 */
@Provider
public class SecurityInterceptor implements ContainerRequestFilter {

    @Context
    private SecurityContext securityContext;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        //@PermitAll
        if (method.isAnnotationPresent(PermitAll.class)) {
            return;
        }

        //@DenyAll
        if (method.isAnnotationPresent(DenyAll.class)) {
            throw new ForbiddenException();
        }

        //@RolesAllowed
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotationMethod = method.getAnnotation(RolesAllowed.class);
            RolesAllowed rolesAnnotationClass = method.getDeclaringClass().getAnnotation(RolesAllowed.class);
            Collection<String> rolesCollection = new ArrayList<>();
            rolesCollection.addAll(Arrays.asList(rolesAnnotationMethod.value()));
            rolesCollection.addAll(Arrays.asList(rolesAnnotationClass.value()));
            Set<String> roles = new HashSet<>(rolesCollection);
            for (String role : roles) {
                if (securityContext.isUserInRole(role)) {
                    return;
                }
            }
            throw new ForbiddenException();
        }
    }

}
