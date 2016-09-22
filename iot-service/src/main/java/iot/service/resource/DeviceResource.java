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
import javax.ws.rs.core.SecurityContext;
import org.jboss.resteasy.links.AddLinks;
import org.jboss.resteasy.links.LinkResource;
import iot.core.role.ApplicationRole;
import iot.core.resource.BaseResource;
import iot.core.service.BaseService;
import iot.domain.entity.Device;
import iot.domain.entity.User;
import iot.domain.service.DeviceService;
import iot.domain.service.UserService;
import iot.service.path.ResourcePath;
import iot.service.role.DeviceRole;

/**
 *
 * @author Rafael Guterres
 */
@Path(ResourcePath.DEVICES)
@RolesAllowed(ApplicationRole.ADMIN)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class DeviceResource extends BaseResource<Device, Long> {

    @Context
    private SecurityContext securityContext;

    @Inject
    private DeviceService deviceService;

    @Inject
    private UserService userService;

    @Override
    protected BaseService<Device, Long> getService() {
        return deviceService;
    }

    @GET
    @Path("/")
    @RolesAllowed(DeviceRole.READ)
    @AddLinks
    @LinkResource(Device.class)
    @Override
    public Collection<Device> getEntities(@Context HttpServletRequest request) {
        return super.getEntities(request);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed(DeviceRole.READ)
    @AddLinks
    @LinkResource
    @Override
    public Device getEntityById(@PathParam("id") Long id) {
        return super.getEntityById(id);
    }

    @POST
    @Path("/")
    @RolesAllowed(DeviceRole.WRITE)
    @LinkResource
    @Override
    public Response postEntity(Device entity) {
        return super.postEntity(entity);
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed(DeviceRole.WRITE)
    @LinkResource
    @Override
    public Response putEntity(@PathParam("id") Long id, Device entity) {
        return super.putEntity(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(DeviceRole.WRITE)
    @LinkResource(Device.class)
    @Override
    public Response deleteEntity(@PathParam("id") Long id) {
        return super.deleteEntity(id);
    }

    @GET
    @Path("/{id}/users")
    @RolesAllowed(DeviceRole.READ)
    @AddLinks
    @LinkResource(value = Device.class, rel = "users")
    Collection<User> getUsers(@PathParam("id") Long deviceId) {
        return userService.findUsersByDevicesId(deviceId);
    }
}
