package iot.core.helper;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rafael Guterres
 */
public final class ResponseBuilder implements Serializable {

    private static final long serialVersionUID = -4360335602897527345L;

    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final String BASIC_REALM = "Basic realm=\"api\"";
    private static final String ERROR = "error";

    private ResponseBuilder() {
    }

    public static Response ok() {
        return Response.status(Response.Status.OK).build();
    }

    public static Response ok(Object entity) {
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    public static Response ok(Collection<Object> entities) {
        return Response.ok(entities).build();
    }

    public static Response created() {
        return Response.status(Response.Status.CREATED).build();
    }

    public static Response created(Object entity) {
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    public static Response accepted() {
        return Response.status(Response.Status.ACCEPTED).build();
    }

    public static Response accepted(Object entity) {
        return Response.status(Response.Status.ACCEPTED).entity(entity).build();
    }

    public static Response deleted() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public static Response notModified() {
        return Response.status(Response.Status.NOT_MODIFIED).build();
    }

    public static Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public static Response badRequest(Exception e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(getError(e)).build();
    }

//    public static Response badRequest(ConstraintViolationException e) {
//        return Response.status(Response.Status.BAD_REQUEST).entity(getError(e)).build();
//    }

    public static Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).header(WWW_AUTHENTICATE, BASIC_REALM).build();
    }

    public static Response forbidden() {
        return Response.status(Response.Status.FORBIDDEN).entity(getError(Response.Status.FORBIDDEN.name())).build();
    }

    public static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public static Response timeout() {
        return Response.status(Response.Status.REQUEST_TIMEOUT).build();
    }

    public static Response conflict() {
        return Response.status(Response.Status.CONFLICT).build();
    }

    public static Response conflict(Exception e) {
        return Response.status(Response.Status.CONFLICT).entity(getError(e)).build();
    }

    public static Response notImplemented() {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    public static Response redirect(String uri) {
        try {
            return Response.seeOther(new URI(uri)).build();
        } catch (URISyntaxException e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
            return null;
        }
    }

    private static Map<String, String> getError(Exception e) {
        return getError(e.getMessage());
    }

    private static Map<String, String> getError(String message) {
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, message);
        return error;
    }

//    private static Map<String, String> getError(ConstraintViolationException e) {
//        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
//        Map<String, String> error = new HashMap<>();
//        for (ConstraintViolation<?> violation : violations) {
//            error.put(violation.getPropertyPath().toString(), violation.getMessage());
//        }
//        return error;
//    }
}
