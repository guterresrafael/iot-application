package iot.service.filter;

import javax.ejb.EJBAccessException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Rafael
 */
@Provider
public class ExceptionMapperHandler implements ExceptionMapper<Exception> {

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if ((exception instanceof ForbiddenException)
                || (exception instanceof EJBAccessException)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
