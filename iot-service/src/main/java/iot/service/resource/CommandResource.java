package iot.service.resource;

import java.util.Collection;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.links.AddLinks;
import org.jboss.resteasy.links.LinkResource;
import iot.core.role.ApplicationRole;
import iot.core.resource.BaseResource;
import iot.core.service.BaseService;
import iot.domain.entity.Command;
import iot.domain.service.CommandService;
import iot.service.role.CommandRole;
import iot.service.path.ResourcePath;

/**
 *
 * @author Rafael Guterres
 */
@Path(ResourcePath.COMMANDS)
@RolesAllowed(ApplicationRole.ADMIN)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class CommandResource extends BaseResource<Command, Long> {

    @Inject
    private CommandService commandService;

    @Override
    protected BaseService<Command, Long> getService() {
        return commandService;
    }

    @GET
    @Path("/")
    @RolesAllowed(CommandRole.READ)
    @AddLinks
    @LinkResource(value = Command.class)
    @Override
    public Collection<Command> getEntities(@Context HttpServletRequest request) {
        return super.getEntities(request);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed(CommandRole.READ)
    @AddLinks
    @LinkResource
    @Override
    public Command getEntityById(@PathParam("id") Long id) {
        return super.getEntityById(id);
    }

    @POST
    @Path("/")
    @RolesAllowed(CommandRole.WRITE)
    @LinkResource
    @Override
    public Response postEntity(Command entity) {
        return super.postEntity(entity);
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed(CommandRole.WRITE)
    @LinkResource
    @Override
    public Response putEntity(@PathParam("id") Long id, Command entity) {
        return super.putEntity(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(CommandRole.WRITE)
    @LinkResource(value = Command.class)
    @Override
    public Response deleteEntity(@PathParam("id") Long id) {
        return super.deleteEntity(id);
    }
}
