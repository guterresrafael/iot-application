package iot.service.resource;

import java.util.Collection;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
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
import iot.domain.entity.Device;
import iot.domain.entity.User;
import iot.domain.service.UserService;
import iot.domain.service.DeviceService;
import iot.service.role.UserRole;
import iot.service.path.ResourcePath;

/**
 *
 * @author Rafael Guterres
 */
@Path(ResourcePath.USERS)
@RolesAllowed(ApplicationRole.ADMIN)
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class UserResource extends BaseResource<User, Long> {

    @Inject
    private UserService userService;

    @Inject
    private DeviceService deviceService;

    @Override
    protected BaseService<User, Long> getService() {
        return userService;
    }

    @GET
    @Path("/")
    @RolesAllowed(ApplicationRole.ADMIN)
    @AddLinks
    @LinkResource(User.class)
    @Override
    public Collection<User> getEntities(@Context HttpServletRequest request) {
        return super.getEntities(request);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed(UserRole.READ)
    @AddLinks
    @LinkResource
    @Override
    public User getEntityById(@PathParam("id") Long id) {
        User user = super.getEntityById(id);
        if (!isUserAdmin() && !getUsername().equals(user.getLogin())) {
            throw new ForbiddenException();
        }
        return user;
    }

    @POST
    @Path("/")
    @RolesAllowed(UserRole.WRITE)
    @LinkResource
    @Override
    public Response postEntity(User entity) {
        return super.postEntity(entity);
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed(UserRole.WRITE)
    @LinkResource
    @Override
    public Response putEntity(@PathParam("id") Long id, User entity) {
        return super.putEntity(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(UserRole.WRITE)
    @LinkResource(User.class)
    @Override
    public Response deleteEntity(@PathParam("id") Long id) {
        return super.deleteEntity(id);
    }

    @GET
    @Path("/{id}/devices")
    @RolesAllowed(UserRole.READ)
    @AddLinks
    @LinkResource(value = User.class, rel = "devices")
    public List<Device> getDevices(@PathParam("id") Long userId) {
        return deviceService.findDevicesByUserId(userId);
    }
}
