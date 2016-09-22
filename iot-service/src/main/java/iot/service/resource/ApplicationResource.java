package iot.service.resource;

import iot.service.path.ResourcePath;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.links.AddLinks;
import org.jboss.resteasy.links.LinkResource;
import iot.service.entity.Application;
import iot.service.path.ApplicationPath;
import iot.core.helper.ResponseBuilder;
import iot.core.role.ApplicationRole;
import iot.domain.entity.Command;
import iot.domain.entity.Device;
import iot.domain.entity.Position;
import iot.domain.entity.User;

/**
 *
 * @author Rafael Guterres
 */
@Path(ApplicationPath.APPLICATION)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ApplicationResource {

    @GET
    @Path("/")
    @PermitAll
    @AddLinks
    Response getApplication() {
        return ResponseBuilder.ok();
    }

    @GET
    @Path(ApplicationPath.DASHBOARD)
    @RolesAllowed({ApplicationRole.DASHBOARD})
    @LinkResource(rel = "list", value = Application.class)
    Response getDashboard() {
        return ResponseBuilder.ok();
    }

    @GET
    @Path(ResourcePath.USERS)
    @PermitAll
    @AddLinks
    @LinkResource(rel = "list", value = Application.class)
    Response getResource(User user) {
        return ResponseBuilder.ok();
    }

    @GET
    @Path(ResourcePath.DEVICES)
    @PermitAll
    @AddLinks
    @LinkResource(rel = "list", value = Application.class)
    Response getResource(Device device) {
        return ResponseBuilder.ok();
    }

    @GET
    @Path(ResourcePath.POSITIONS)
    @PermitAll
    @AddLinks
    @LinkResource(rel = "list", value = Application.class)
    Response getResource(Position position) {
        return ResponseBuilder.ok();
    }

    @GET
    @Path(ResourcePath.COMMANDS)
    @PermitAll
    @AddLinks
    @LinkResource(rel = "list", value = Application.class)
    Response getResource(Command command) {
        return ResponseBuilder.ok();
    }
}
