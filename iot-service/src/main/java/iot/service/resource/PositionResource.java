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
import iot.core.helper.ResponseBuilder;
import iot.core.resource.BaseResource;
import iot.core.service.BaseService;
import iot.domain.entity.Position;
import iot.domain.service.PositionService;
import iot.service.path.ResourcePath;
import iot.service.role.PositionRole;

/**
 *
 * @author Rafael Guterres
 */
@Path(ResourcePath.POSITIONS)
@RolesAllowed(ApplicationRole.ADMIN)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class PositionResource extends BaseResource<Position, Long> {

    private static final String GOOGLE_MAPS_LINK = "https://maps.google.com?q=%s,%s";

    @Inject
    private PositionService positionService;

    @Override
    protected BaseService<Position, Long> getService() {
        return positionService;
    }

    @GET
    @Path("/")
    @RolesAllowed(PositionRole.READ)
    @AddLinks
    @LinkResource(Position.class)
    @Override
    public Collection<Position> getEntities(@Context HttpServletRequest request) {
        return super.getEntities(request);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed(PositionRole.READ)
    @AddLinks
    @LinkResource
    @Override
    public Position getEntityById(@PathParam("id") Long id) {
        return super.getEntityById(id);
    }

    @POST
    @Path("/")
    @RolesAllowed(PositionRole.WRITE)
    @LinkResource
    @Override
    public Response postEntity(Position entity) {
        return super.postEntity(entity);
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed(PositionRole.WRITE)
    @AddLinks
    @LinkResource
    @Override
    public Response putEntity(@PathParam("id") Long id, Position entity) {
        return super.putEntity(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(PositionRole.WRITE)
    @LinkResource(Position.class)
    @Override
    public Response deleteEntity(@PathParam("id") Long id) {
        return super.deleteEntity(id);
    }

    @GET
    @Path("/{id}/googlemaps")
    @RolesAllowed(PositionRole.READ)
    @LinkResource(value = Position.class, rel = "googlemaps")
    Response getGoogleMapsLink(@PathParam("id") Long id) {
        Position position = getService().load(id);
        String googleMapsLink = String.format(GOOGLE_MAPS_LINK, position.getLatitude(), position.getLongitude());
        return ResponseBuilder.redirect(googleMapsLink);
    }
}
